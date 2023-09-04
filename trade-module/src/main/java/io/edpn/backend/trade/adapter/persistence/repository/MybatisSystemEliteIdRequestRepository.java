package io.edpn.backend.trade.adapter.persistence.repository;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface MybatisSystemEliteIdRequestRepository {

    @Insert("INSERT INTO system_elite_id_data_request (system_name) VALUES (#{systemName})")
    void insert(@Param("systemName") String systemName);

    @Delete("DELETE FROM system_elite_id_data_request WHERE system_name = #{systemName}")
    void delete(@Param("systemName") String systemName);

    @Select("SELECT EXISTS(SELECT 1 FROM system_elite_id_data_request WHERE system_name = #{systemName})")
    boolean exists(@Param("systemName") String systemName);
}
