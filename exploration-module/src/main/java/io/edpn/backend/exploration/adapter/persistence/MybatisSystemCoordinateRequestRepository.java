package io.edpn.backend.exploration.adapter.persistence;

import io.edpn.backend.exploration.adapter.persistence.entity.MybatisSystemCoordinateRequestEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MybatisSystemCoordinateRequestRepository {

    @Insert("INSERT INTO system_coordinate_data_request (requesting_module, system_name) VALUES (#{requestingModule}, #{systemName})")
    void insert(MybatisSystemCoordinateRequestEntity requestDataMessageEntity);

    @Delete("DELETE FROM system_coordinate_data_request WHERE requesting_module = #{requestingModule} AND system_name = #{systemName}")
    void delete(MybatisSystemCoordinateRequestEntity requestDataMessageEntity);

    @Select("SELECT * FROM system_coordinate_data_request")
    List<MybatisSystemCoordinateRequestEntity> findAll();

    @Select("SELECT * FROM system_coordinate_data_request WHERE system_name = #{systemName}")
    @Results(id = "commodityMarketInfoResultMap", value = {
            @Result(property = "systemName", column = "system_name"),
            @Result(property = "requestingModule", column = "requesting_module")
    })
    List<MybatisSystemCoordinateRequestEntity> findBySystemName(String requestDataMessageEntity);
}
