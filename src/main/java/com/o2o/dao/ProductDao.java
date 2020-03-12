package com.o2o.dao;

import com.o2o.entity.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductDao {
    /**
     * 查询商品列表并分页，输入条件：商品名（模糊），商品状态，店铺Id，商品类别
     *
     * @param productCondition 查询条件
     * @param rowIndex         行数
     * @param pageSize         每页数
     * @return
     */
    List<Product> selectProductList(@Param("productCondition") Product productCondition, @Param("rowIndex") int rowIndex,
                                    @Param("pageSize") int pageSize);

    /**
     * 根据条件查询商品总数
     *
     * @param productCondition 查询条件
     * @return
     */
    int selectProductCount(@Param("productCondition") Product productCondition);

    /**
     * 商品添加
     * @param product
     * @return
     */
    int insertProduct(Product product);

    /**
     * 根据商品Id查询商品详情
     *
     * @param productId
     * @return
     */
    Product selectProductByProductId(long productId);

    /**
     * 更新商品信息
     *
     * @param product
     * @return
     */
    int updateProduct(Product product);

    /**
     * 删除商品类别时将商品记录中的类别项置空
     *
     * @param productCategoryId
     * @return
     */
    int updateProductCategoryToNull(long productCategoryId);


}
