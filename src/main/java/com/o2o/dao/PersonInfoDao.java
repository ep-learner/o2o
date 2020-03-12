package com.o2o.dao;

import com.o2o.entity.PersonInfo;

public interface PersonInfoDao {

    //使用userId查询用户
    PersonInfo queryInfoByUserId(long userId);

    //添加账号
    int insertPersonInfo(PersonInfo user);

    //修改用户信息
    int updatePersonInfo(PersonInfo user);
}
