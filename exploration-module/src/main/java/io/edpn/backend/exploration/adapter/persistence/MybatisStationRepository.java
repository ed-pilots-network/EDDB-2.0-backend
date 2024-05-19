package io.edpn.backend.exploration.adapter.persistence;

import io.edpn.backend.exploration.adapter.persistence.entity.MybatisStationEntity;
import io.edpn.backend.exploration.adapter.persistence.entity.MybatisSystemEntity;
import io.edpn.backend.mybatisutil.StringDoubleMapTypeHandler;
import io.edpn.backend.mybatisutil.StringIntegerMapTypeHandler;
import io.edpn.backend.mybatisutil.StringListToArrayTypeHandler;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.apache.ibatis.type.JdbcType.ARRAY;

public interface MybatisStationRepository {

    @Select("""
            WITH upsert AS (
                INSERT INTO station (id, market_id, name, type, distance_from_star, system_id, landing_pads, economy, economies, services, government, odyssey, updated_at)
                    VALUES (#{id}, #{marketId}, #{name}, #{type}, #{distanceFromStar}, #{system.id}, #{landingPads}, #{economy}, #{economies}, #{services}, #{government}, #{odyssey}, #{updatedAt})
                    ON CONFLICT (name, system_id)
                        DO UPDATE SET
                            market_id = COALESCE(EXCLUDED.market_id, station.market_id),
                            type = COALESCE(EXCLUDED.type, station.type),
                            distance_from_star = COALESCE(EXCLUDED.distance_from_star, station.distance_from_star),
                            landing_pads = COALESCE(EXCLUDED.landing_pads, station.landing_pads),
                            economy = COALESCE(EXCLUDED.economy, station.economy),
                            economies = COALESCE(EXCLUDED.economies, station.economies),
                            services = COALESCE(EXCLUDED.services, station.services),
                            government = COALESCE(EXCLUDED.government, station.government),
                            odyssey = COALESCE(EXCLUDED.odyssey, station.odyssey),
                            updated_at = COALESCE(EXCLUDED.updated_at, station.updated_at)
                            WHERE station.updated_at IS NULL OR station.updated_at < EXCLUDED.updated_at
                    RETURNING *
            )
            SELECT *
            FROM upsert
            UNION ALL
            SELECT *
            FROM station
            WHERE NOT EXISTS (SELECT id FROM upsert)
              AND name = #{name}
              AND system_id = #{system.id}
                        """)
    @ResultMap("stationResultMap")
    MybatisStationEntity insertOrUpdateOnConflict(MybatisStationEntity station);

    @Select({
            "SELECT s.id, s.market_id, s.name, s.type, s.distance_from_star, s.system_id, s.landing_pads, s.economy, s.economies, s.services, s.government, s.odyssey, s.updated_at ",
            "FROM station s ",
            "INNER JOIN system sys ON s.system_id = sys.id ",
            "WHERE sys.name = #{systemName} AND s.name = #{stationName}"
    })
    @Results(id = "stationResultMap", value = {
            @Result(property = "id", column = "id", javaType = UUID.class),
            @Result(property = "marketId", column = "market_id", javaType = Long.class),
            @Result(property = "name", column = "name", javaType = String.class),
            @Result(property = "type", column = "type", javaType = String.class),
            @Result(property = "distanceFromStar", column = "distance_from_star", javaType = Double.class),
            @Result(property = "system", column = "system_id", javaType = MybatisSystemEntity.class,
                    one = @One(select = "io.edpn.backend.exploration.adapter.persistence.MybatisSystemRepository.findById")),
            @Result(property = "economy", column = "economy", javaType = String.class),
            @Result(property = "government", column = "government", javaType = String.class),
            @Result(property = "odyssey", column = "odyssey", javaType = Boolean.class),
            @Result(property = "updatedAt", column = "updated_at", javaType = LocalDateTime.class),
            @Result(property = "landingPads", column = "landing_pads", javaType = Map.class, typeHandler = StringIntegerMapTypeHandler.class),
            @Result(property = "economies", column = "economies", javaType = Map.class, typeHandler = StringDoubleMapTypeHandler.class),
            @Result(property = "services", column = "services", javaType = List.class, jdbcType = ARRAY, typeHandler = StringListToArrayTypeHandler.class),
    })
    Optional<MybatisStationEntity> findByIdentifier(@Param("systemName") String systemName, @Param("stationName") String stationName);
    
    @Select({
            "SELECT station.name FROM station " +
                    "INNER JOIN system ON station.system_id = system.id " +
                    "WHERE system.name = #{systemName} " +
                    "AND station.type != 'FleetCarrier'"
    })
    List<String> findStationNamesBySystemName(@Param("systemName") String systemName);
}
