package io.edpn.backend.trade.adapter.persistence.repository;

import io.edpn.backend.trade.adapter.persistence.entity.MybatisCommodityEntity;
import org.apache.ibatis.annotations.Select;

public interface MybatisCommodityRepository {

    @Select("""
            WITH enriched_data AS (
                SELECT v.id AS id, v.name AS technical_name, cw.display_name, cw.type, cw.is_rare
                FROM (VALUES (#{id}, #{name})) AS v(id, name)
                    LEFT JOIN commodity_whitelist cw
                        ON v.name = cw.technical_name)
            INSERT
            INTO commodity (id, technical_name, display_name, type, is_rare)
            SELECT id,
                   technical_name,
                   display_name,
                   type,
                   is_rare
            FROM enriched_data
            ON CONFLICT (technical_name)
                DO UPDATE SET technical_name = COALESCE(commodity.technical_name, EXCLUDED.technical_name),
                              display_name   = EXCLUDED.display_name,
                              type           = EXCLUDED.type,
                              is_rare        = EXCLUDED.is_rare
            RETURNING *;
            """)
    MybatisCommodityEntity createOrUpdateOnConflict(MybatisCommodityEntity commodity);
}
