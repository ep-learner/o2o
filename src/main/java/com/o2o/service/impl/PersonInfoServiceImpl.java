package com.o2o.service.impl;

import com.o2o.dao.PersonInfoDao;
import com.o2o.dto.PersonInfoExecution;
import com.o2o.entity.PersonInfo;
import com.o2o.enums.OperationStatusEnum;
import com.o2o.enums.PersonInfoStateEnum;
import com.o2o.exceptions.PersonInfoOperationException;
import com.o2o.service.PersonInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Date;

@Controller
public class PersonInfoServiceImpl implements PersonInfoService {

    @Autowired
    private PersonInfoDao personInfoDao;

    @Override
    public PersonInfo queryInfoByUserId(long userId) {
        return personInfoDao.queryInfoByUserId(userId);
    }

    @Override
    public PersonInfoExecution insertPersonInfo(PersonInfo user) {
        if (user == null ||  user.getUserName() == null) {
            return new PersonInfoExecution(PersonInfoStateEnum.NULL_PERSON_INFO);
        }
        user.setCreateTime(new Date());
        try {
            int effectedNum = personInfoDao.insertPersonInfo(user);
            if (effectedNum <= 0) {
                throw new PersonInfoOperationException("用户信息新增失败");
            } else {
                return new PersonInfoExecution(OperationStatusEnum.SUCCESS, user);
            }
        } catch (Exception e) {
            throw new PersonInfoOperationException("insertPersonInfo error:" + e.getMessage());
        }

    }

    @Override
    public PersonInfoExecution updatePersonInfo(PersonInfo user) {
        // 空值判断
        if (user == null || user.getUserId() == null || user.getUserName() == null) {
            return new PersonInfoExecution(PersonInfoStateEnum.NULL_PERSON_INFO);
        }
        // 设置默认信息
        user.setLastEditTime(new Date());
        try {
            int effectedNum = personInfoDao.updatePersonInfo(user);
            if (effectedNum <= 0) {
                throw new PersonInfoOperationException("用户信息修改失败");
            } else {
                return new PersonInfoExecution(OperationStatusEnum.SUCCESS, user);
            }
        } catch (Exception e) {
            throw new PersonInfoOperationException("insertPersonInfo error:" + e.getMessage());
        }
    }
}
