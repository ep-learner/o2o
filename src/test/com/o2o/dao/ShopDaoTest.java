package com.o2o.dao;

import com.o2o.entity.Shop;
import com.o2o.BaseTest;
import com.o2o.entity.Area;
import com.o2o.entity.PersonInfo;
import com.o2o.entity.ShopCategory;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;


public class ShopDaoTest extends BaseTest {
    @Autowired
    ShopDao sd;

    /**
     * 测试SelectByShopId
     */
    @Test
    public void testSelectByShopId(){
        Shop shop = sd.selectByShopId(5);
        System.out.println(shop.getArea().getAreaId());
        System.out.println(shop.getArea().getAreaName());
    }


    /**
     * 测试SelectByShopId
     */
    @Test
    public void testSelectShopList(){
        Area area = new Area();
        area.setAreaId(1);

        // 一级类别 饮品类 Id=1 parentId=null
        ShopCategory parent = new ShopCategory();
        parent.setShopCategoryId(1L);

        //二级类别 奶茶 Id=3  parentId=1
        ShopCategory shopCategory = new ShopCategory();
        shopCategory.setShopCategoryId(3L);
        shopCategory.setParent(parent);

        Shop shopCondition = new Shop();
        PersonInfo personInfo = new PersonInfo();
        personInfo.setUserId(6L);
        shopCondition.setOwner(personInfo);


        List<Shop> shops = sd.selectShopList(shopCondition, 0, 2);
        System.out.println("计算查询结果数量"+sd.selectShopCount(shopCondition));
        System.out.println("size"+shops.size());
        System.out.println(shops);
    }

    @Test
    @Ignore
    /**
     * 测试update方法
     */
    public void testUpdateShop(){
        Shop shop = new Shop();
        shop.setAdvice("只改这个属性");
        shop.setShopId(1L);
        sd.updateShop(shop);
    }

    @Test
    @Ignore
    /**
     * 测试insert方法
     */
    public void testInsertShop(){
        Shop shop = new Shop();

        Area area = new Area();
        area.setAreaId(1);
        shop.setArea(area);

        ShopCategory shopCategory = new ShopCategory();
        shopCategory.setShopCategoryId(1L);
        shop.setShopCategory(shopCategory);

        PersonInfo owner = new PersonInfo();
        owner.setUserId(1L);
        shop.setOwner(owner);

        shop.setShopName("test店铺");
        shop.setShopDesc("my_description");
        shop.setShopAddr("test");
        shop.setPhone("test");
        shop.setShopImg("test");
        shop.setPriority(1);
        shop.setCreateTime(new Date());
        shop.setEnableStatus(0);
        //shop.setLastEditTime(new Date());
        shop.setAdvice("审核中");
        sd.insertShop(shop);
    }
}
