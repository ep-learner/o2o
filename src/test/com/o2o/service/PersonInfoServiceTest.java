package com.o2o.service;

import com.o2o.BaseTest;
import com.o2o.dao.PersonInfoDao;
import com.o2o.dto.PersonInfoExecution;
import com.o2o.entity.PersonInfo;
import com.o2o.enums.PersonInfoStatusEnum;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;


public class PersonInfoServiceTest extends BaseTest {

    @Autowired
    PersonInfoService personInfoService;

    @Test
    public void AInsertTest(){
        PersonInfo personInfo = new PersonInfo();
        personInfo.setLocalAuthId(1L);
        personInfo.setEnableStatus(PersonInfoStatusEnum.ALLOW.getState());
        personInfo.setUserType(PersonInfoStatusEnum.CUSTOMER.getState());
        personInfo.setCreateTime(new Date());
        personInfo.setGender("男");
        personInfo.setEmail("123@qq.com");
        personInfo.setUserName("王五");
        PersonInfoExecution personInfoExecution = personInfoService.insertPersonInfo(personInfo);
        System.out.println(personInfoExecution.getState());
    }

    @Test
    public void BselectTest(){
        PersonInfo personInfo = personInfoService.queryInfoByUserId(1L);
        System.out.println(personInfo);
    }

    @Test
    public void CUpdateTest(){
        PersonInfo personInfo = new PersonInfo();
        personInfo.setLastEditTime(new Date());
        personInfo.setUserId(4L);
        personInfo.setUserName("333");
        PersonInfoExecution personInfoExecution = personInfoService.updatePersonInfo(personInfo);

    }

}
