package com.o2o.service;

import com.o2o.dto.LocalAuthExecution;
import com.o2o.entity.LocalAuth;
import com.o2o.exceptions.LocalAuthOperationException;

public interface LocalAuthService {
    /**
     * 通过帐号和密码获取平台帐号信息
     *
     */
    LocalAuth getLocalAuthByUsernameAndPwd(String userName, String password);

    /**
     * 通过userId获取平台帐号信息
     */
    LocalAuth getLocalAuthByUserId(long userId);

    /**
     * 账号绑定
     */
    LocalAuthExecution bindLocalAuth(LocalAuth localAuth) throws LocalAuthOperationException;

    /**
     * 修改平台帐号的登录密码
     */
    LocalAuthExecution modifyLocalAuth(Long userId, String username, String password, String newPassword)
            throws LocalAuthOperationException;

    //使用用户名查找
    public LocalAuth getLocalAuthByUsername(String username);
}
