package com.o2o.service.impl;

import com.o2o.dao.LocalAuthDao;
import com.o2o.dto.LocalAuthExecution;
import com.o2o.entity.LocalAuth;
import com.o2o.enums.LocalAuthStateEnum;
import com.o2o.exceptions.LocalAuthOperationException;
import com.o2o.service.LocalAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class LocalAuthServiceImpl implements LocalAuthService {

    @Autowired
    LocalAuthDao localAuthDao;

    @Override
    public LocalAuth getLocalAuthByUsername(String username) {
        return localAuthDao.queryLocalByUsername(username);
    }
    @Override
    public LocalAuth getLocalAuthByUsernameAndPwd(String userName, String password) {
//        return localAuthDao.queryLocalByUserNameAndPwd(userName, MD5.getMd5(password));
        return localAuthDao.queryLocalByUserNameAndPwd(userName, password);
    }

    @Override
    public LocalAuth getLocalAuthByUserId(long userId) {
        return localAuthDao.queryLocalByUserId(userId);
    }

    @Override
    public LocalAuthExecution bindLocalAuth(LocalAuth localAuth) throws LocalAuthOperationException {
        // 空值判断，传入的localAuth 信息是否为空
        if(localAuth==null || localAuth.getUsername()==null || localAuth.getPassword()==null){
            return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
        }

        // 如果之前没有绑定过平台帐号，则创建一个平台帐号与该用户绑定
        try{
            localAuth.setCreateTime(new Date());
            localAuth.setLastEditTime(new Date());
            localAuth.setPassword(localAuth.getPassword());
            localAuth.setUsername(localAuth.getUsername());
            // 判断创建是否成功
            int effetcedNum = localAuthDao.insertLocalAuth(localAuth);
            if(effetcedNum<=0){
                throw new LocalAuthOperationException("账号新增失败");
            }else {
                return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS, localAuth);
            }
        }catch(Exception e){
            throw new LocalAuthOperationException("insert error: " + e.getMessage());
        }

    }

    @Override
    public LocalAuthExecution modifyLocalAuth(Long userId, String username, String password, String newPassword) throws LocalAuthOperationException {
        //参数校验
        if(userId==null || username==null || password==null || newPassword==null ||password.equals(newPassword)){
            return new LocalAuthExecution(LocalAuthStateEnum.LOGINFAIL);
        }else{
            try{
                int effectedNum = localAuthDao.updateLocalAuth(userId, username, password, newPassword, new Date());
                if(effectedNum<=0){
                    throw new LocalAuthOperationException("更新密码失败");
                }
                else return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS);
            }catch(Exception e){
                throw new LocalAuthOperationException("modify error"+e.getMessage());
            }
        }
    }
}
