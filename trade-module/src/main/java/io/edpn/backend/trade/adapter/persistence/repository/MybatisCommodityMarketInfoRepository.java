package io.edpn.backend.trade.adapter.persistence.repository;

import io.edpn.backend.trade.adapter.persistence.entity.MybatisCommodityMarketInfoEntity;
import io.edpn.backend.trade.adapter.persistence.entity.MybatisStationEntity;
import io.edpn.backend.trade.adapter.persistence.entity.MybatisValidatedCommodityEntity;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MybatisCommodityMarketInfoRepository {

    @Select({"""
            SELECT *
            FROM commodity_market_info_view
            WHERE commodity_id = #{commodityId}"""})
    @Results(id = "commodityMarketInfoResultMap", value = {
            @Result(property = "validatedCommodity", column = "commodity_id", javaType = MybatisValidatedCommodityEntity.class,
                    one = @One(select = "io.edpn.backend.trade.adapter.persistence.repository.MybatisValidatedCommodityRepository.findById")),
            @Result(column="max_buy_price", property="maxBuyPrice"),
            @Result(column="min_buy_price", property="minBuyPrice"),
            @Result(column="avg_buy_price", property="avgBuyPrice"),
            @Result(column="max_sell_price", property="maxSellPrice"),
            @Result(column="min_sell_price", property="minSellPrice"),
            @Result(column="avg_sell_price", property="avgSellPrice"),
            @Result(column="mean_price", property="meanPrice"),
            @Result(column="total_stock", property="totalStock"),
            @Result(column="total_demand", property="totalDemand"),
            @Result(column="total_stations", property="totalStations"),
            @Result(column="stations_with_buy_price", property="stationsWithBuyPrice"),
            @Result(column="stations_with_sell_price", property="stationsWithSellPrice"),
            @Result(column="stations_with_buy_price_lower_than_average", property="stationsWithBuyPriceLowerThanAverage"),
            @Result(column="stations_with_sell_price_higher_than_average", property="stationsWithSellPriceHigherThanAverage"),
            @Result(column="highest_selling_to_station", property="highestSellingToStation", javaType = MybatisStationEntity.class,
                    one = @One(select = "io.edpn.backend.trade.adapter.persistence.repository.MybatisStationRepository.findById")),
            @Result(column="lowest_buying_from_station", property="lowestBuyingFromStation", javaType = MybatisStationEntity.class,
                    one = @One(select = "io.edpn.backend.trade.adapter.persistence.repository.MybatisStationRepository.findById"))
    })
    Optional<MybatisCommodityMarketInfoEntity> findByCommodityId(@Param("commodityId") UUID commodityId);

    @Select({"SELECT * FROM commodity_market_info_view"})
    @ResultMap("commodityMarketInfoResultMap")
    List<MybatisCommodityMarketInfoEntity> findAll();

}
