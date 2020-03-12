package com.o2o.service;

import java.util.List;

import com.o2o.entity.Area;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.o2o.BaseTest;

public class AreaServiceTest extends BaseTest {

    @Autowired
    private AreaService areaService;

    @Test
    public void testQueryArea() {
//        //连接测试
//        Jedis jedis = new Jedis("116.62.102.69",6379);
//        System.out.println(jedis.ping());
//        jedis.close();
        List<Area> areaList = areaService.getAreaList();
        System.out.println("service测试：" + areaList.toString());
    }

}
