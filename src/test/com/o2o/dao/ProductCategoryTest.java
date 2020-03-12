package com.o2o.dao;

import com.o2o.entity.ProductCategory;
import com.o2o.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductCategoryTest extends BaseTest {

    @Autowired
    ProductCategoryDao pcd;

    @Test
    public void testProductCategory(){
        List<ProductCategory> productCategories = pcd.queryProductCategoryList(1L);
        System.out.println(productCategories.size());
    }

    @Test
    public void testbatchInsertProductCategory(){
        ProductCategory p1 = new ProductCategory();
        p1.setShopId(1L);
        p1.setProductCategoryName("奶茶");
        p1.setCreateTime(new Date());
        p1.setPriority(1);
        p1.setProductCategoryDesc("哈哈哈哈");
        ProductCategory p2 = new ProductCategory();
        p2.setShopId(2L);
        p2.setProductCategoryName("咖啡");
        p2.setCreateTime(new Date());
        p2.setPriority(1);
        p1.setProductCategoryDesc("哈哈哈哈");
        List<ProductCategory> lists = new ArrayList<>();
        lists.add(p1);
        lists.add(p2);
        pcd.batchInsertProductCategory(lists);
    }
}
