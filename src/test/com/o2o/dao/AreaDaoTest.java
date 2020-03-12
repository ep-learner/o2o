package com.o2o.dao;

import com.o2o.entity.Area;
import com.o2o.BaseTest;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * Dao的测试即是能成功访问数据库
 */

public class AreaDaoTest extends BaseTest {
    @Autowired
    private AreaDao ad;
    @Test
    public void ATestSelect() {
        /**
         * 测试select方法
         */
        List<Area> areaList = ad.selectArea();
        System.out.println(areaList);

    }

    @Test
    public void BTestInsert() {
        /**
         * 测试insert方法
         */
        Area area = new Area();
        area.setAreaName("hahaha");
        Date date = new Date();
        System.out.println(date);
        area.setCreateTime(date);
        area.setPriority(1);
        ad.insertArea(area);
    }
}
