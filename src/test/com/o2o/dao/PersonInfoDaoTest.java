package com.o2o.dao;

import com.o2o.BaseTest;
import com.o2o.entity.PersonInfo;
import com.o2o.enums.PersonInfoStatusEnum;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class PersonInfoDaoTest extends BaseTest {

    @Autowired
    PersonInfoDao personInfoDao;

    @Test
    public void ATest(){
        PersonInfo personInfo = new PersonInfo();
        personInfo.setLocalAuthId(1L);
        personInfo.setEnableStatus(PersonInfoStatusEnum.ALLOW.getState());
        personInfo.setUserType(PersonInfoStatusEnum.CUSTOMER.getState());
        personInfo.setCreateTime(new Date());
        personInfo.setGender("男");
        personInfo.setEmail("123@qq.com");
        personInfo.setUserName("王五");
        int effectNum = personInfoDao.insertPersonInfo(personInfo);

        personInfoDao.insertPersonInfo(personInfo);
    }

    /**
     * 根据用户Id查询本地账号信息
     */
    @Test
    public void testQueryInfoByUserId() {
        PersonInfo personInfo = personInfoDao.queryInfoByUserId(2L);
        System.out.println(personInfo.toString());
        //assertEquals("顾客a", personInfo.getUserName());
    }

    /**
     * 更新账号信息
     */
    @Test
    public void testUpdatePersonInfo() {
        PersonInfo personInfo = new PersonInfo();
        personInfo.setLastEditTime(new Date());
        personInfo.setUserId(2L);
        personInfo.setUserName("顾客aa");
        int effectNum = personInfoDao.updatePersonInfo(personInfo);
        assertEquals(1, effectNum);
    }

}
