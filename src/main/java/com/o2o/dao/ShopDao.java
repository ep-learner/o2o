package com.o2o.dao;

import com.o2o.entity.Shop;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopDao {
    int insertShop(Shop shop);

    int updateShop(Shop shop);

    Shop selectByShopId(long iii);

    /**
     * 分页查询
     * 查询条件可以是店铺名 店铺状态 店铺类别 区域 owner的排列组合
     * selse * from tb Limits (rowIndex,pageSize)
     * @param shop
     * @param rowIndex 从第几行开始显示
     * @param pageSize 返回的数据行数
     * @return
     */
    List<Shop> selectShopList(@Param("shopCondition") Shop shop, @Param("rowIndex")int rowIndex, @Param("pageSize")int pageSize);

    /**
     * 查询总数
     */
    int selectShopCount(@Param("shopCondition") Shop shop);

}
