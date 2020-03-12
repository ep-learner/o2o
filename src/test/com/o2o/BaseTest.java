package com.o2o;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 整合spring和junit的配置，其他测试类的基础
 */
@RunWith(SpringJUnit4ClassRunner.class)
//加載配置文件
@ContextConfiguration({ "classpath:spring/spring-dao.xml", "classpath:spring/spring-service.xml","classpath:spring/spring-redis.xml"
         })
public class BaseTest {

}
