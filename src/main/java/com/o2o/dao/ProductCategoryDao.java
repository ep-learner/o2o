package com.o2o.dao;

import com.o2o.entity.ProductCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductCategoryDao {
    /**
     * 通过店铺Id查找产品类别列表
     * @param shopId
     * @return
     */
    List<ProductCategory> queryProductCategoryList(Long shopId);

    /**
     *批量插入productCategory
     * @param productCategoryList
     * @return
     */
    int batchInsertProductCategory(List<ProductCategory> productCategoryList);

    /**
     * 删除商品类别
     *
     * @param productCategoryId 商品类别Id
     * @param shopId            店铺Id,使删除操作更安全
     * @return
     */
    int deleteProductCategory(@Param("productCategoryId") long productCategoryId, @Param("shopId") long shopId);

}
