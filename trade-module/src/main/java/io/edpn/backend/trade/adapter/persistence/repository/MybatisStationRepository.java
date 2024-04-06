package io.edpn.backend.trade.adapter.persistence.repository;

import io.edpn.backend.mybatisutil.UuidTypeHandler;
import io.edpn.backend.trade.adapter.persistence.entity.MybatisStationEntity;
import io.edpn.backend.trade.adapter.persistence.entity.MybatisSystemEntity;
import io.edpn.backend.trade.adapter.persistence.filter.MybatisFindStationFilter;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MybatisStationRepository {

    @Select("SELECT * FROM station WHERE id = #{id}")
    @Results(id = "stationResultMap", value = {
            @Result(property = "id", column = "id", javaType = UUID.class, typeHandler = UuidTypeHandler.class),
            @Result(property = "marketId", column = "market_id", javaType = Long.class),
            @Result(property = "name", column = "name", javaType = String.class),
            @Result(property = "arrivalDistance", column = "arrival_distance", javaType = Double.class),
            @Result(property = "system", column = "system_id", javaType = MybatisSystemEntity.class,
                    one = @One(select = "io.edpn.backend.trade.adapter.persistence.repository.MybatisSystemRepository.findById")),
            @Result(property = "planetary", column = "planetary", javaType = boolean.class),
            @Result(property = "requireOdyssey", column = "require_odyssey", javaType = boolean.class),
            @Result(property = "fleetCarrier", column = "fleet_carrier", javaType = boolean.class),
            @Result(property = "maxLandingPadSize", column = "max_landing_pad_size", javaType = Integer.class),
            @Result(property = "marketUpdatedAt", column = "market_updated_at", javaType = LocalDateTime.class)
    })
    Optional<MybatisStationEntity> findById(@Param("id") UUID id);

    @Update("UPDATE station SET market_id = #{marketId}, name = #{name}, arrival_distance = #{arrivalDistance}, system_id = #{system.id}, planetary = #{planetary}, " +
            "require_odyssey = #{requireOdyssey}, fleet_carrier = #{fleetCarrier}, max_landing_pad_size = #{maxLandingPadSize}, " +
            "market_updated_at = #{marketUpdatedAt} WHERE id = #{id}")
    void update(MybatisStationEntity station);

    @Select("""
            <script>
            SELECT * FROM station
            WHERE 1 = 1
            <if test='hasRequiredOdyssey != null'>AND require_odyssey IS NULL != #{hasRequiredOdyssey}</if>
            <if test='hasLandingPadSize != null'>AND (
                            max_landing_pad_size IS NULL != #{hasLandingPadSize} OR
                            (max_landing_pad_size = -1) !=  #{hasLandingPadSize}
            </if>
            <if test='hasPlanetary != null'>AND planetary IS NULL != #{hasPlanetary}</if>
            <if test='hasArrivalDistance != null'>AND arrival_distance IS NULL != #{hasArrivalDistance}</if>
            </script>
            """)
    @ResultMap("stationResultMap")
    List<MybatisStationEntity> findByFilter(MybatisFindStationFilter filter);

    @Select({
            "INSERT INTO station (id, market_id, name, arrival_distance, system_id, planetary, require_odyssey, fleet_carrier, max_landing_pad_size, market_updated_at) ",
            "VALUES (#{id}, #{marketId}, #{name}, #{arrivalDistance}, #{system.id}, #{planetary}, #{requireOdyssey}, #{fleetCarrier}, #{maxLandingPadSize}, #{marketUpdatedAt})",
            "ON CONFLICT (name, system_id)",
            "DO UPDATE SET",
            "market_id = COALESCE(station.market_id, EXCLUDED.market_id),",
            "arrival_distance = COALESCE(station.arrival_distance, EXCLUDED.arrival_distance),",
            "planetary = COALESCE(station.planetary, EXCLUDED.planetary),",
            "require_odyssey = COALESCE(station.require_odyssey, EXCLUDED.require_odyssey),",
            "fleet_carrier = COALESCE(station.fleet_carrier, EXCLUDED.fleet_carrier),",
            "max_landing_pad_size = COALESCE(station.max_landing_pad_size, EXCLUDED.max_landing_pad_size),",
            "market_updated_at = COALESCE(station.market_updated_at, EXCLUDED.market_updated_at)",
            "RETURNING *"
    })
    @ResultMap("stationResultMap")
    MybatisStationEntity createOrUpdateOnConflict(MybatisStationEntity station);
}