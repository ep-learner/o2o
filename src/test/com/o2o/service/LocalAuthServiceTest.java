package com.o2o.service;

import com.o2o.BaseTest;
import com.o2o.dto.LocalAuthExecution;
import com.o2o.entity.LocalAuth;
import com.o2o.entity.PersonInfo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class LocalAuthServiceTest extends BaseTest {
    @Autowired
    private LocalAuthService localAuthService;
    @Test
    public void ATestBind(){
        LocalAuth localAuth = new LocalAuth();
        PersonInfo personInfo = new PersonInfo();
        String username = "test1";
        String password = "test2";
        personInfo.setUserId(1L);
        localAuth.setPersonInfo(personInfo);
        localAuth.setPassword(password);
        localAuth.setUsername(username);
        //在localauth中插入一个用户信息
        localAuthService.bindLocalAuth(localAuth);

        //查找该用户信息
        LocalAuth temp = localAuthService.getLocalAuthByUserId(1L);
        System.out.println(temp);
    }

    @Test
    public void testBModifyLocalAuth() {
        // 设置帐号信息
        long userId = 1;
        String username = "test1";
        String password = "test2";
        String newPassword = "passwd";
        // 修改该帐号对应的密码
        LocalAuthExecution lae = localAuthService.modifyLocalAuth(userId, username, password, newPassword);

        // 通过帐号密码找到修改后的localAuth
        LocalAuth localAuth = localAuthService.getLocalAuthByUsernameAndPwd(username, newPassword);
        // 打印用户名字看看跟预期是否相符
        System.out.println(localAuth);
    }
    @Test
    public void a(){
        LocalAuth localAuth = localAuthService.getLocalAuthByUserId(5L);
    }

}
