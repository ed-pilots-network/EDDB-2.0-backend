package io.edpn.backend.trade.adapter.persistence.repository;

import io.edpn.backend.trade.adapter.persistence.entity.MybatisSingleHopEntity;
import io.edpn.backend.trade.adapter.persistence.entity.MybatisStationEntity;
import io.edpn.backend.trade.adapter.persistence.entity.MybatisValidatedCommodityEntity;
import io.edpn.backend.trade.adapter.persistence.filter.MybatisLocateSingleHopTradeFilter;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MybatisLocateSingleHopTradeRouteRepository {
    @Select("""
            <script>
            WITH stations_in_range AS MATERIALIZED (SELECT station.id,
                                              system_id,
                                              arrival_distance
                                       FROM station
                                                INNER JOIN system ON station.system_id = system.id
                                       WHERE st_3ddwithin((select coordinates_geom
                                                           from system
                                                           where system.name = #{sellToSystemName}), coordinates_geom, #{maxRouteDistance})
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
                                         AND #{maxArrivalDistance} >= arrival_distance),
            
                 stations_in_system AS (select station.id,
                                               station.system_id,
                                               station.arrival_distance
                                        FROM station
                                                 INNER JOIN system ON station.system_id = system.id AND system.name = #{sellToSystemName}),
            
                 commodity_list AS (
                     SELECT id, display_name, is_rare
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
                                         sell_price,
                                         coordinates_geom,
                                         arrival_distance,
                                         demand
                                  FROM latest_market_datum sell
                                           INNER JOIN stations_in_system ON station_id = stations_in_system.id
                                           INNER JOIN system on stations_in_system.system_id = system.id
                                           INNER JOIN commodity ON commodity_id = commodity.id AND display_name IS NOT null
                                  WHERE (demand = 0 OR demand > #{minDemand})
                                    AND commodity.is_rare = false
                                    AND sell_price > mean_price
                                    AND timestamp >= (now() - INTERVAL '1 hour' * #{maxPriceAgeHours}))
            
            SELECT buy_market.commodity_id,
                   buy_market.buy_station_id,
                   buy_market.buy_price,
                   buy_market.stock,
                   sell_market.sell_station_id,
                   sell_market.sell_price,
                   sell_market.demand,
                   (sell_market.sell_price - buy_market.buy_price)                          AS profit,
                   st_3ddistance(buy_market.coordinates_geom, sell_market.coordinates_geom) AS route_distance
            FROM buy_market
                     INNER JOIN sell_market ON buy_market.commodity_id = sell_market.commodity_id
                AND st_3ddwithin(sell_market.coordinates_geom, buy_market.coordinates_geom, #{maxRouteDistance})
                AND buy_market.buy_station_id != sell_market.sell_station_id
                AND 100 >= buy_market.rn
            WHERE (sell_market.sell_price - buy_market.buy_price) > 0
            ORDER BY profit DESC, buy_market.arrival_distance + sell_arrival_distance
            LIMIT 50
            </script>""")
    @Results(id = "findSingleHopTradeResultMap", value = {
            @Result(property = "commodityEntity", column = "commodity_id", javaType = MybatisValidatedCommodityEntity.class,
                    one = @One(select = "io.edpn.backend.trade.adapter.persistence.repository.MybatisValidatedCommodityRepository.findById")),
            @Result(property = "buyFromStationEntity", column = "buy_station_id", javaType = MybatisStationEntity.class,
                    one = @One(select = "io.edpn.backend.trade.adapter.persistence.repository.MybatisStationRepository.findById")),
            @Result(property = "buyPrice", column = "buy_price", javaType = Long.class),
            @Result(property = "stock", column = "stock", javaType = Long.class),
            @Result(property = "sellToStationEntity", column = "sell_station_id", javaType = MybatisStationEntity.class,
                    one = @One(select = "io.edpn.backend.trade.adapter.persistence.repository.MybatisStationRepository.findById")),
            @Result(property = "sellPrice", column = "sell_price", javaType = Long.class),
            @Result(property = "demand", column = "demand", javaType = Long.class),
            @Result(property = "profit", column = "profit", javaType = Long.class),
            @Result(property = "routeDistance", column = "route_distance", javaType = Double.class),
    })
    List<MybatisSingleHopEntity> findBestBuyWithinRangeOfSystem(MybatisLocateSingleHopTradeFilter locateSingleHopTradeFilter);
    
    @Select("""
            <script>
            WITH stations_in_range AS MATERIALIZED (SELECT station.id,
                                              system_id,
                                              arrival_distance
                                       FROM station
                                                INNER JOIN system ON station.system_id = system.id
                                       WHERE st_3ddwithin((select coordinates_geom
                                                           from system
                                                           where system.name = #{sellToSystemName}), coordinates_geom, #{maxRouteDistance})
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
                                         AND #{maxArrivalDistance} >= arrival_distance),
            
                 reference_station AS (select station.id,
                                               station.system_id,
                                               station.arrival_distance
                                        FROM station
                                                 INNER JOIN system ON station.system_id = system.id AND system.name = #{sellToSystemName}
                                        WHERE station.name = #{sellToStationName}),
            
                 commodity_list AS (
                     SELECT id, display_name, is_rare
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
                                  AND mean_prce > buy_price),
            
                 sell_market AS (SELECT commodity_id,
                                         station_id                                                             as sell_station_id,
                                         arrival_distance                                                       AS sell_arrival_distance,
                                         sell_price,
                                         coordinates_geom,
                                         arrival_distance,
                                         demand
                                  FROM latest_market_datum sell
                                           INNER JOIN reference_station ON station_id = reference_station.id
                                           INNER JOIN system on reference_station.system_id = system.id
                                           INNER JOIN commodity ON commodity_id = commodity.id AND display_name IS NOT null
                                  WHERE (demand = 0 OR demand > #{minDemand})
                                    AND commodity.is_rare = false
                                    AND sell_price > mean_price
                                    AND timestamp >= (now() - INTERVAL '1 hour' * #{maxPriceAgeHours}))
            
            SELECT buy_market.commodity_id,
                   buy_market.buy_station_id,
                   buy_market.buy_price,
                   buy_market.stock,
                   sell_market.sell_station_id,
                   sell_market.sell_price,
                   sell_market.demand,
                   (sell_market.sell_price - buy_market.buy_price)                          AS profit,
                   st_3ddistance(buy_market.coordinates_geom, sell_market.coordinates_geom) AS route_distance
            FROM buy_market
                     INNER JOIN sell_market ON buy_market.commodity_id = sell_market.commodity_id
                AND st_3ddwithin(sell_market.coordinates_geom, buy_market.coordinates_geom, #{maxRouteDistance})
                AND buy_market.buy_station_id != sell_market.sell_station_id
                AND 100 >= buy_market.rn
            WHERE (sell_market.sell_price - buy_market.buy_price) > 0
            ORDER BY profit DESC, buy_market.arrival_distance + sell_arrival_distance
            LIMIT 50
            </script>""")
    @ResultMap("findSingleHopTradeResultMap")
    List<MybatisSingleHopEntity> findBestBuyWithinRangeOfStation(MybatisLocateSingleHopTradeFilter locateSingleHopTradeFilter);
    
    @Select("""
            <script>
            WITH stations_in_range AS MATERIALIZED (SELECT station.id,
                                              system_id,
                                              arrival_distance
                                       FROM station
                                                INNER JOIN system ON station.system_id = system.id
                                       WHERE st_3ddwithin((select coordinates_geom
                                                           from system
                                                           where system.name = #{buyFromSystemName}), coordinates_geom, #{maxRouteDistance})
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
                                         AND #{maxArrivalDistance} >= arrival_distance),
            
                 reference_station AS (select station.id,
                                               station.system_id,
                                               station.arrival_distance
                                        FROM station
                                                 INNER JOIN system ON station.system_id = system.id AND system.name = #{buyFromSystemName}),
            
                 commodity_list AS (
                     SELECT id, display_name, is_rare
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
                                       buy_price,
                                       coordinates_geom,
                                       arrival_distance,
                                       stock
                                FROM latest_market_datum buy
                                         INNER JOIN reference_station ON station_id = reference_station.id
                                         INNER JOIN system ON reference_station.system_id = system.id
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
                                         ROW_NUMBER() OVER (PARTITION BY commodity_id ORDER BY buy_price DESC) AS rn
                                  FROM latest_market_datum sell
                                           INNER JOIN stations_in_range ON station_id = stations_in_range.id
                                           INNER JOIN system on stations_in_range.system_id = system.id
                                           INNER JOIN commodity ON commodity_id = commodity.id AND display_name IS NOT null
                                  WHERE (demand = 0 OR demand > #{minDemand})
                                    AND commodity.is_rare = false
                                    AND sell_price > mean_price
                                    AND timestamp >= (now() - INTERVAL '1 hour' * #{maxPriceAgeHours}))
            
            SELECT buy_market.commodity_id,
                   buy_market.buy_station_id,
                   buy_market.buy_price,
                   buy_market.stock,
                   sell_market.sell_station_id,
                   sell_market.sell_price,
                   sell_market.demand,
                   (sell_market.sell_price - buy_market.buy_price)                          AS profit,
                   st_3ddistance(buy_market.coordinates_geom, sell_market.coordinates_geom) AS route_distance
            FROM buy_market
                     INNER JOIN sell_market ON buy_market.commodity_id = sell_market.commodity_id
                AND st_3ddwithin(sell_market.coordinates_geom, buy_market.coordinates_geom, #{maxRouteDistance})
                AND buy_market.buy_station_id != sell_market.sell_station_id
                AND 100 >= sell_market.rn
            WHERE (sell_market.sell_price - buy_market.buy_price) > 0
            ORDER BY profit DESC, buy_market.arrival_distance + sell_arrival_distance
            LIMIT 50
            </script>""")
    @ResultMap("findSingleHopTradeResultMap")
    List<MybatisSingleHopEntity> findBestSellWithinRangeOfSystem(MybatisLocateSingleHopTradeFilter locateSingleHopTradeFilter);
    
    @Select("""
            <script>
            WITH stations_in_range AS MATERIALIZED (SELECT station.id,
                                              system_id,
                                              arrival_distance
                                       FROM station
                                                INNER JOIN system ON station.system_id = system.id
                                       WHERE st_3ddwithin((select coordinates_geom
                                                           from system
                                                           where system.name = #{buyFromSystemName}), coordinates_geom, #{maxRouteDistance})
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
                                         AND #{maxArrivalDistance} >= arrival_distance),
            
                 reference_station AS (select station.id,
                                               station.system_id,
                                               station.arrival_distance
                                        FROM station
                                                 INNER JOIN system ON station.system_id = system.id AND system.name = #{buyFromSystemName}
                                        WHERE station.name = #{buyFromStationName}),
            
                 commodity_list AS (
                     SELECT id, display_name, is_rare
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
                                       buy_price,
                                       coordinates_geom,
                                       arrival_distance,
                                       stock
                                FROM latest_market_datum buy
                                         INNER JOIN reference_station ON station_id = reference_station.id
                                         INNER JOIN system ON reference_station.system_id = system.id
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
                                         ROW_NUMBER() OVER (PARTITION BY commodity_id ORDER BY buy_price DESC) AS rn
                                  FROM latest_market_datum sell
                                           INNER JOIN stations_in_range ON station_id = stations_in_range.id
                                           INNER JOIN system on stations_in_range.system_id = system.id
                                           INNER JOIN commodity ON commodity_id = commodity.id AND display_name IS NOT null
                                  WHERE (demand = 0 OR demand > #{minDemand})
                                    AND commodity.is_rare = false
                                    AND sell_price > mean_price
                                    AND timestamp >= (now() - INTERVAL '1 hour' * #{maxPriceAgeHours}))
            
            SELECT buy_market.commodity_id,
                   buy_market.buy_station_id,
                   buy_market.buy_price,
                   buy_market.stock,
                   sell_market.sell_station_id,
                   sell_market.sell_price,
                   sell_market.demand,
                   (sell_market.sell_price - buy_market.buy_price)                          AS profit,
                   st_3ddistance(buy_market.coordinates_geom, sell_market.coordinates_geom) AS route_distance
            FROM buy_market
                     INNER JOIN sell_market ON buy_market.commodity_id = sell_market.commodity_id
                AND st_3ddwithin(sell_market.coordinates_geom, buy_market.coordinates_geom, #{maxRouteDistance})
                AND buy_market.buy_station_id != sell_market.sell_station_id
                AND 100 >= sell_market.rn
            WHERE (sell_market.sell_price - buy_market.buy_price) > 0
            ORDER BY profit DESC, buy_market.arrival_distance + sell_arrival_distance
            LIMIT 50
            </script>""")
    @ResultMap("findSingleHopTradeResultMap")
    List<MybatisSingleHopEntity> findBestSellWithinRangeOfStation(MybatisLocateSingleHopTradeFilter locateSingleHopTradeFilter);
    
    @Select("""
            <script>
            WITH buy_system AS MATERIALIZED (SELECT station.id,
                                              system_id,
                                              arrival_distance
                                        FROM station
                                                INNER JOIN system ON station.system_id = system.id
                                        WHERE system.name = #{buyFromSystemName}
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
                                            AND #{maxArrivalDistance} >= arrival_distance),
            
                 sell_system AS (select station.id,
                                               station.system_id,
                                               station.arrival_distance
                                        FROM station
                                                 INNER JOIN system ON station.system_id = system.id
                                        WHERE system.name = #{sellToSystemName}
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
                                            AND #{maxArrivalDistance} >= arrival_distance),
            
                 commodity_list AS (
                     SELECT id, display_name, is_rare
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
                                       buy_price,
                                       coordinates_geom,
                                       arrival_distance,
                                       stock
                                FROM latest_market_datum buy
                                         INNER JOIN buy_system ON station_id = buy_system.id
                                         INNER JOIN system ON buy_system.system_id = system.id
                                         INNER JOIN commodity_list ON commodity_id = commodity_list.id AND display_name IS NOT null
                                WHERE stock > #{minSupply}
                                  AND commodity_list.is_rare = false
                                  AND timestamp >= (now() - INTERVAL '1 hour' * #{maxPriceAgeHours})
                                  AND buy_price IS NOT null),
            
                 sell_market AS (SELECT commodity_id,
                                         station_id                                                             as sell_station_id,
                                         arrival_distance                                                       AS sell_arrival_distance,
                                         sell_price,
                                         coordinates_geom,
                                         arrival_distance,
                                         demand
                                  FROM latest_market_datum sell
                                           INNER JOIN sell_system ON station_id = sell_system.id
                                           INNER JOIN system on sell_system.system_id = system.id
                                           INNER JOIN commodity ON commodity_id = commodity.id AND display_name IS NOT null
                                  WHERE (demand = 0 OR demand > #{minDemand})
                                    AND commodity.is_rare = false
                                    AND timestamp >= (now() - INTERVAL '1 hour' * #{maxPriceAgeHours}))
            
            SELECT buy_market.commodity_id,
                   buy_market.buy_station_id,
                   buy_market.buy_price,
                   buy_market.stock,
                   sell_market.sell_station_id,
                   sell_market.sell_price,
                   sell_market.demand,
                   (sell_market.sell_price - buy_market.buy_price)                          AS profit,
                   st_3ddistance(buy_market.coordinates_geom, sell_market.coordinates_geom) AS route_distance
            FROM buy_market
                     INNER JOIN sell_market ON buy_market.commodity_id = sell_market.commodity_id
                     AND buy_market.buy_station_id != sell_market.sell_station_id
            WHERE (sell_market.sell_price - buy_market.buy_price) > 0
            ORDER BY profit DESC, buy_market.arrival_distance + sell_arrival_distance
            LIMIT 50
            </script>""")
    @ResultMap("findSingleHopTradeResultMap")
    List<MybatisSingleHopEntity> findBestTradeBetweenSystems(MybatisLocateSingleHopTradeFilter locateSingleHopTradeFilter);
    
    @Select("""
            <script>
            WITH buy_station AS MATERIALIZED (SELECT station.id,
                                              system_id,
                                              arrival_distance
                                        FROM station
                                                INNER JOIN system ON station.system_id = system.id AND system.name = #{buyFromSystemName}
                                        WHERE station.name = #{buyFromStationName}),
            
                 sell_station AS (select station.id,
                                               station.system_id,
                                               station.arrival_distance
                                        FROM station
                                                 INNER JOIN system ON station.system_id = system.id AND system.name = #{sellToSystemName}
                                        WHERE station.name = #{sellToStationName}),
            
                 commodity_list AS (
                     SELECT id, display_name, is_rare
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
                                       buy_price,
                                       coordinates_geom,
                                       arrival_distance,
                                       stock
                                FROM latest_market_datum buy
                                         INNER JOIN buy_station ON station_id = buy_station.id
                                         INNER JOIN system ON buy_station.system_id = system.id
                                         INNER JOIN commodity_list ON commodity_id = commodity_list.id AND display_name IS NOT null
                                WHERE stock > #{minSupply}
                                  AND commodity_list.is_rare = false
                                  AND timestamp >= (now() - INTERVAL '1 hour' * #{maxPriceAgeHours})
                                  AND buy_price IS NOT null),
            
                 sell_market AS (SELECT commodity_id,
                                         station_id                                                             as sell_station_id,
                                         arrival_distance                                                       AS sell_arrival_distance,
                                         sell_price,
                                         coordinates_geom,
                                         arrival_distance,
                                         demand
                                  FROM latest_market_datum sell
                                           INNER JOIN sell_station ON station_id = sell_station.id
                                           INNER JOIN system on sell_station.system_id = system.id
                                           INNER JOIN commodity ON commodity_id = commodity.id AND display_name IS NOT null
                                  WHERE (demand = 0 OR demand > #{minDemand})
                                    AND commodity.is_rare = false
                                    AND timestamp >= (now() - INTERVAL '1 hour' * #{maxPriceAgeHours}))
            
            SELECT buy_market.commodity_id,
                   buy_market.buy_station_id,
                   buy_market.buy_price,
                   buy_market.stock,
                   sell_market.sell_station_id,
                   sell_market.sell_price,
                   sell_market.demand,
                   (sell_market.sell_price - buy_market.buy_price)                          AS profit,
                   st_3ddistance(buy_market.coordinates_geom, sell_market.coordinates_geom) AS route_distance
            FROM buy_market
                     INNER JOIN sell_market ON buy_market.commodity_id = sell_market.commodity_id
                     AND buy_market.buy_station_id != sell_market.sell_station_id
            WHERE (sell_market.sell_price - buy_market.buy_price) > 0
            ORDER BY profit DESC, buy_market.arrival_distance + sell_arrival_distance
            LIMIT 50
            </script>""")
    @ResultMap("findSingleHopTradeResultMap")
    List<MybatisSingleHopEntity> findBestTradeBetweenStations(MybatisLocateSingleHopTradeFilter locateSingleHopTradeFilter);

}
