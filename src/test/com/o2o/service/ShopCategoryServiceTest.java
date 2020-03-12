package com.o2o.service;

import com.o2o.entity.ShopCategory;
import com.o2o.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ShopCategoryServiceTest extends BaseTest {
    @Autowired
    private ShopCategoryService s;
    @Test
    public void func(){
        ShopCategory shopCategory = new ShopCategory();//带id=1的父类
        ShopCategory parent = new ShopCategory();//不带父类
        parent.setShopCategoryId(1L);
        shopCategory.setParent(parent);

        List<ShopCategory> shopCategoryList = s.getShopCategoryList(shopCategory);
        System.out.println("withparent"+"\t"+s.getShopCategoryList(shopCategory).size());
        System.out.println("without"+"\t"+s.getShopCategoryList(parent).size());
    }
}
