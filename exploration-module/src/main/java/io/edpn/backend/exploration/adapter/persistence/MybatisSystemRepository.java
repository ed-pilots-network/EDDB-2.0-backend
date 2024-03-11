package io.edpn.backend.exploration.adapter.persistence;


import io.edpn.backend.exploration.adapter.persistence.entity.MybatisSystemEntity;
import io.edpn.backend.mybatisutil.UuidTypeHandler;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MybatisSystemRepository {

    @Select({"INSERT INTO system (id, name, star_class, elite_id, coordinates_geom)",
            "VALUES (#{id}, #{name}, #{primaryStarClass}, #{eliteId}, ST_MakePoint(#{xCoordinate}, #{yCoordinate}, #{zCoordinate}))",
            "ON CONFLICT (name)",
            "DO UPDATE SET",
            "star_class = COALESCE(system.star_class, EXCLUDED.star_class),",
            "elite_id = COALESCE(system.elite_id, EXCLUDED.elite_id),",
            "coordinates_geom = COALESCE(system.coordinates_geom, EXCLUDED.coordinates_geom)",
            "RETURNING id, name, star_class, elite_id, ST_X(coordinates_geom) as x_coordinate, ST_Y(coordinates_geom) as y_coordinate, ST_Z(coordinates_geom) as z_coordinate"
    })
    @ResultMap("systemResultMap")
    MybatisSystemEntity insertOrUpdateOnConflict(MybatisSystemEntity system);


    @Select({"SELECT id, name, star_class, elite_id, ST_X(coordinates_geom) as x_coordinate, ST_Y(coordinates_geom) as y_coordinate, ST_Z(coordinates_geom) as z_coordinate",
            "FROM system",
            "WHERE name = #{name}"}
    )
    @Results(id = "systemResultMap", value = {
            @Result(property = "id", column = "id", javaType = UUID.class, typeHandler = UuidTypeHandler.class),
            @Result(property = "name", column = "name", javaType = String.class),
            @Result(property = "primaryStarClass", column = "star_class", javaType = String.class),
            @Result(property = "eliteId", column = "elite_id", javaType = Long.class),
            @Result(property = "xCoordinate", column = "x_coordinate", javaType = Double.class),
            @Result(property = "yCoordinate", column = "y_coordinate", javaType = Double.class),
            @Result(property = "zCoordinate", column = "z_coordinate", javaType = Double.class)
    })
    Optional<MybatisSystemEntity> findByName(@Param("name") String name);

    @Select({"SELECT id, name, star_class, elite_id, ST_X(coordinates_geom) as x_coordinate, ST_Y(coordinates_geom) as y_coordinate, ST_Z(coordinates_geom) as z_coordinate",
            "FROM system",
            "WHERE id = #{id}"}
    )
    @ResultMap("systemResultMap")
    Optional<MybatisSystemEntity> findById(@Param("id") UUID id);

    @Select({"SELECT id, name, star_class, elite_id, ST_X(coordinates_geom) as x_coordinate, ST_Y(coordinates_geom) as y_coordinate, ST_Z(coordinates_geom) as z_coordinate",
            "FROM system",
            "WHERE name ILIKE CONCAT('%', #{name}, '%')",
            "ORDER BY CASE WHEN name ILIKE CONCAT(#{name}, '%') THEN 0 ELSE 1 END, name",
            "LIMIT #{amount}"})
    @ResultMap("systemResultMap")
    List<MybatisSystemEntity> findFromSearchbar(@Param("name") String name, @Param("amount") int amount);
}
