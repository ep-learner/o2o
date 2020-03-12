package com.o2o.dao;

import com.o2o.entity.ShopCategory;
import com.o2o.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ShopCategoryDaoTest extends BaseTest {
    @Autowired
    private ShopCategoryDao scd ;

    @Test
    public void func(){
        //测试 parent==null的情况
        ShopCategory shopCategory = new ShopCategory();//带id=1的父类
        ShopCategory parent = new ShopCategory();//不带父类
        parent.setShopCategoryId(1L);
        shopCategory.setParent(parent);
        List<ShopCategory> withOutParent = scd.selectShopCategory(parent);
        List<ShopCategory> withParent = scd.selectShopCategory(shopCategory);
        if(withOutParent!=null) System.out.println("withOutParent"+withOutParent.size());
        if(withParent!=null) System.out.println("withParent"+withParent.size());
    }
}
