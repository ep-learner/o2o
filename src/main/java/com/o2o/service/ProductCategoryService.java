package com.o2o.service;

import com.o2o.exceptions.ProductCategoryOperationException;
import com.o2o.dto.ProductCategoryExecution;
import com.o2o.entity.ProductCategory;

import java.util.List;

public interface ProductCategoryService  {

    //基于shopId查找商铺的产品类别
    List<ProductCategory> getProductCategoryList(Long shopId);

    //基于shopId查找商铺的产品类别
    ProductCategoryExecution batchInsertProductCategory(List<ProductCategory> productCategoryList) throws ProductCategoryOperationException;

    ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId) throws ProductCategoryOperationException;
}
