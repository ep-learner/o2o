package com.o2o.service;

import com.o2o.entity.ProductCategory;
import com.o2o.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ProductCategoryTest extends BaseTest {
    @Autowired
    ProductCategoryService pcs;

    @Test
    public void testProductCategoryService(){
        List<ProductCategory> productCategoryList = pcs.getProductCategoryList(1L);
        System.out.println(productCategoryList.size());

    }
}
