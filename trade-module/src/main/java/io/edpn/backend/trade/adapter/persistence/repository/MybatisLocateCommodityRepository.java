package io.edpn.backend.trade.adapter.persistence.repository;

import io.edpn.backend.trade.adapter.persistence.entity.MybatisLocateCommodityEntity;
import io.edpn.backend.trade.adapter.persistence.entity.MybatisStationEntity;
import io.edpn.backend.trade.adapter.persistence.entity.MybatisValidatedCommodityEntity;
import io.edpn.backend.trade.adapter.persistence.filter.MybatisLocateCommodityFilter;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

public interface MybatisLocateCommodityRepository {
    
    @Select("""
            <script>
            SELECT
            timestamp, commodity_id, station_id, system_id, stock, demand, buy_price, sell_price,
            ST_3DDistance(coordinates_geom, ST_MakePoint(#{xCoordinate}, #{yCoordinate}, #{zCoordinate})) as distance,
            planetary,
            require_odyssey,
            fleet_carrier
            FROM locate_commodity_view
            INNER JOIN validated_commodity_view vcv
            ON commodity_id = vcv.id
            WHERE
            vcv.display_name =#{commodityDisplayName}
            <if test='!includePlanetary'>AND planetary = false </if>
            <if test='!includeOdyssey'>AND require_odyssey = false </if>
            <if test='!includeFleetCarriers'>AND fleet_carrier = false </if>
            AND stock >= #{minSupply}
            AND demand >= #{minDemand}
            AND max_landing_pad_size >= #{maxLandingPadSize}
            ORDER BY distance
            </script>"""
    )
    @Results(id = "findCommodityResultMap", value = {
            @Result(property = "priceUpdatedAt", column = "timestamp", javaType = LocalDateTime.class),
            @Result(property = "validatedCommodity", column = "commodity_id", javaType = MybatisValidatedCommodityEntity.class,
                    one = @One(select = "io.edpn.backend.trade.adapter.persistence.repository.MybatisValidatedCommodityRepository.findById")),
            @Result(property = "station", column = "station_id", javaType = MybatisStationEntity.class,
                    one = @One(select = "io.edpn.backend.trade.adapter.persistence.repository.MybatisStationRepository.findById")),
            @Result(property = "supply", column = "stock", javaType = Long.class),
            @Result(property = "demand", column = "demand", javaType = Long.class),
            @Result(property = "buyPrice", column = "buy_price", javaType = Long.class),
            @Result(property = "sellPrice", column = "sell_price", javaType = Long.class),
            @Result(property = "distance", column = "distance", javaType = Double.class)

    })
    List<MybatisLocateCommodityEntity> locateCommodityByFilter(MybatisLocateCommodityFilter mybatisLocateCommodityFilterPersistence);
    
}
