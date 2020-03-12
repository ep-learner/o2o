package com.o2o.dao;

import com.o2o.entity.LocalAuth;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface LocalAuthDao {

    //1、通过帐号和密码查询对应信息，登录用
    LocalAuth queryLocalByUserNameAndPwd(@Param("username") String username, @Param("password") String password);

    //2、通过用户Id查询对应localauth
    LocalAuth queryLocalByUserId(@Param("userId") long userId);

    //3、添加平台帐号
    int insertLocalAuth(LocalAuth localAuth);

    //4、通过userId,username,password更改密码并更新操作时间
    int updateLocalAuth(@Param("userId") long userId, @Param("username") String username,
                        @Param("password") String password, @Param("newPassword") String newPassword,
                        @Param("lastEditTime") Date lastEditTime);

    //5、通过用户名查找用户，避免注册的用户名重复
    LocalAuth queryLocalByUsername(@Param("username") String username);
}
