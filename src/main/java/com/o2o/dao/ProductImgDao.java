package com.o2o.dao;

import com.o2o.entity.ProductImg;

import java.util.List;

public interface ProductImgDao {
    /**
     * 批量插入图片
     * @param productImgList
     * @return
     */
    int batchInsertProductImg(List<ProductImg> productImgList);

    /**
     * 根据商品Id删除商品详情图
     *
     * @param productId
     * @return
     */
    int deleteProductImgByProductId(Long productId);

    /**
     * 根据商品Id获取商品详情图列表
     *
     * @param productId
     * @return
     */
    List<ProductImg> selectProductImgListByProductId(long productId);

}
