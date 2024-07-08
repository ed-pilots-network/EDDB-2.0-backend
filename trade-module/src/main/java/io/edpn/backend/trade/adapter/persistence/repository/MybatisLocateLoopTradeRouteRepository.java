package io.edpn.backend.trade.adapter.persistence.repository;

import io.edpn.backend.trade.adapter.persistence.entity.MybatisLoopTradeEntity;
import io.edpn.backend.trade.adapter.persistence.entity.MybatisStationEntity;
import io.edpn.backend.trade.adapter.persistence.entity.MybatisValidatedCommodityEntity;
import io.edpn.backend.trade.adapter.persistence.filter.MybatisLocateLoopTradeFilter;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MybatisLocateLoopTradeRouteRepository {
    @Select("""
            <script>
            WITH stations_in_range AS (SELECT station.id,
                                              system_id,
                                              arrival_distance
                                       FROM public.station
                                                INNER JOIN system ON station.system_id = system.id
                                       WHERE st_3ddwithin(st_makepoint(#{xCoordinate}, #{yCoordinate}, #{zCoordinate}), coordinates_geom, 300)
                                        <if test='!includeFleetCarriers'>
                                         AND station.fleet_carrier = #{includeFleetCarriers}
                                        </if>
                                        <if test='!includeOdyssey'>
                                         AND station.require_odyssey = #{includeOdyssey}
                                        </if>
                                        <if test='!includeSurfaceStations'>
                                         AND station.planetary = #{includeSurfaceStations}
                                        </if>
                                        <if test='maxLandingPadSize == "LARGE"'>AND max_landing_pad_size = 'LARGE'</if>
                                        <if test='maxLandingPadSize == "MEDIUM"'>AND max_landing_pad_size IN ('MEDIUM', 'LARGE')</if>
                                         AND #{maxArrivalDistance} >= arrival_distance
                           ),
            
                 commodity_list AS (SELECT id,
                                           display_name,
                                           is_rare
                                    FROM commodity
                                    <if test='commodityDisplayNames.size() > 0'>
                                        WHERE display_name IN
                                        <foreach item='item' collection='commodityDisplayNames' open='(' separator=',' close=')'>
                                            #{item}
                                        </foreach>
                                    </if>
                                    <if test='commodityDisplayNames.size() == 0'>
                                    WHERE display_name IS NOT NULL
                                    </if>
                         ),
            
                 buy_market AS (SELECT commodity_id,
                                       station_id                                                           as buy_station_id,
                                       arrival_distance                                                     AS buy_arrival_distance,
                                       MIN(buy_price) OVER (PARTITION BY commodity_id)                      AS min_buy_price,
                                       buy_price,
                                       coordinates_geom,
                                       arrival_distance,
                                       stock,
                                       ROW_NUMBER() OVER (PARTITION BY commodity_id ORDER BY buy_price ASC) AS rn
                                FROM latest_market_datum buy
                                         INNER JOIN stations_in_range ON station_id = stations_in_range.id
                                         INNER JOIN system ON stations_in_range.system_id = system.id
                                         INNER JOIN commodity_list ON commodity_id = commodity_list.id AND display_name IS NOT null
                                WHERE stock > #{minSupply}
                                  AND commodity_list.is_rare = false
                                  AND timestamp >= (now() - INTERVAL '1 hour' * #{maxPriceAgeHours})
                                  AND mean_price > buy_price),
            
                 sell_market AS (SELECT commodity_id,
                                        station_id                                                             as sell_station_id,
                                        arrival_distance                                                       AS sell_arrival_distance,
                                        MAX(sell_price) OVER (PARTITION BY commodity_id)                       AS max_sell_price,
                                        sell_price,
                                        coordinates_geom,
                                        arrival_distance,
                                        demand,
                                        ROW_NUMBER() OVER (PARTITION BY commodity_id ORDER BY sell_price DESC) AS rn
                                 FROM latest_market_datum sell
                                          INNER JOIN stations_in_range ON station_id = stations_in_range.id
                                          INNER JOIN system on stations_in_range.system_id = system.id
                                          INNER JOIN commodity_list ON commodity_id = commodity_list.id AND display_name IS NOT null
                                 WHERE (demand = 0 OR demand > #{minDemand})
                                   AND commodity_list.is_rare = false
                                   AND sell_price > mean_price
                                   AND timestamp >= (now() - INTERVAL '1 hour' * #{maxPriceAgeHours}))
            
            SELECT buy_market.commodity_id,
                   buy_market.buy_station_id,
                   buy_market.buy_price,
                   buy_market.stock,
                   sell_market.sell_station_id,
                   sell_market.sell_price,
                   sell_market.demand,
                   (sell_market.sell_price - buy_market.buy_price)                                                          AS profit,
                   st_3ddistance(buy_market.coordinates_geom, sell_market.coordinates_geom)                                 AS route_distance,
                   st_3ddistance(st_makepoint(#{xCoordinate}, #{yCoordinate}, #{zCoordinate}), buy_market.coordinates_geom) AS distance_from_reference
            
            FROM buy_market
                     INNER JOIN sell_market ON buy_market.commodity_id = sell_market.commodity_id
                AND st_3ddwithin(sell_market.coordinates_geom, buy_market.coordinates_geom, #{maxRouteDistance})
                AND 100 >= buy_market.rn AND 100 >= sell_market.rn
                AND buy_market.buy_station_id != sell_market.sell_station_id
            WHERE (sell_market.sell_price - buy_market.buy_price) > 5000
            ORDER BY profit DESC, buy_market.arrival_distance + sell_arrival_distance
            LIMIT 50
            </script>""")
    @Results(id = "findLoopTradeResultMap", value = {
            @Result(property = "firstTripEntity.commodityEntity", column = "commodity_id", javaType = MybatisValidatedCommodityEntity.class,
                    one = @One(select = "io.edpn.backend.trade.adapter.persistence.repository.MybatisValidatedCommodityRepository.findById")),
            @Result(property = "firstTripEntity.buyFromStationEntity", column = "buy_station_id", javaType = MybatisStationEntity.class,
                    one = @One(select = "io.edpn.backend.trade.adapter.persistence.repository.MybatisStationRepository.findById")),
            @Result(property = "firstTripEntity.buyPrice", column = "buy_price", javaType = Long.class),
            @Result(property = "firstTripEntity.stock", column = "stock", javaType = Long.class),
            @Result(property = "firstTripEntity.sellToStationEntity", column = "sell_station_id", javaType = MybatisStationEntity.class,
                    one = @One(select = "io.edpn.backend.trade.adapter.persistence.repository.MybatisStationRepository.findById")),
            @Result(property = "firstTripEntity.sellPrice", column = "sell_price", javaType = Long.class),
            @Result(property = "firstTripEntity.demand", column = "demand", javaType = Long.class),
            @Result(property = "firstTripEntity.profit", column = "profit", javaType = Long.class),
            @Result(property = "firstTripEntity.routeDistance", column = "route_distance", javaType = Double.class),
            @Result(property = "distanceFromCommander", column = "distance_from_reference", javaType = Double.class)
    })
    List<MybatisLoopTradeEntity> locateFirstHalfLoop(MybatisLocateLoopTradeFilter mybatisLocateLoopTradeFilter);
}
