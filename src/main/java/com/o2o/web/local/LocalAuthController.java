package com.o2o.web.local;

import com.o2o.dto.LocalAuthExecution;
import com.o2o.dto.PersonInfoExecution;
import com.o2o.entity.LocalAuth;
import com.o2o.entity.PersonInfo;
import com.o2o.enums.LocalAuthStateEnum;
import com.o2o.enums.OperationStatusEnum;
import com.o2o.enums.PersonInfoStatusEnum;
import com.o2o.service.LocalAuthService;
import com.o2o.service.PersonInfoService;
import com.o2o.util.CodeUtil;
import com.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/local")
public class LocalAuthController {

    @Autowired
    private LocalAuthService localAuthService;

    @Autowired
    private PersonInfoService personInfoService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    //账号注册
    private Map<String, Object> register(HttpServletRequest request) {
        HashMap<String, Object> modelMap = new HashMap<>();
        // 验证码校验
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "输入了错误的验证码");
            return modelMap;
        }
        //从请求中获取账号密码和用户信息
        String userName = HttpServletRequestUtil.getString(request, "userName");
        String password = HttpServletRequestUtil.getString(request, "password");

        //添加用户，再创建本地账号
        if(userName != null && password != null ){
            PersonInfo personInfo = new PersonInfo();
            personInfo.setUserName(userName);
            personInfo.setUserType(PersonInfoStatusEnum.CUSTOMER.getState());
            personInfo.setEnableStatus(PersonInfoStatusEnum.ALLOW.getState());
            personInfo.setCreateTime(new Date());
            PersonInfoExecution personInfoExecution = personInfoService.insertPersonInfo(personInfo);
            if(personInfoExecution.getState()== OperationStatusEnum.SUCCESS.getState()){
                LocalAuth localAuth = new LocalAuth();
                localAuth.setUsername(userName);
                localAuth.setPassword(password);
                localAuth.setPersonInfo(personInfo);
                LocalAuthExecution localAuthExecution = localAuthService.bindLocalAuth(localAuth);
                modelMap.put("success", true);
            }
            else{
                modelMap.put("success", false);
                modelMap.put("erMsg", personInfoExecution.getStateInfo());
            }
        }else{
            modelMap.put("success", false);
            modelMap.put("erMsg", "用户名/密码不能为空");
        }
        return modelMap;
    }

    /**
     * 检查注册的用户名是否已注册
     */
    @RequestMapping(value = "/checkusername")
    @ResponseBody
    private Map<String, Object> checkUsername(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        String userName = HttpServletRequestUtil.getString(request, "userName");
        if (userName!=null) {
            LocalAuth localAuth = localAuthService.getLocalAuthByUsername(userName);
            // 当前用户名的用户已存在
            if (localAuth != null) {
                modelMap.put("success", false);
                modelMap.put("errMsg", "用户名已存在，请重新输入！");
            } else {
                modelMap.put("success", true);
            }
        }else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "用户名不能为空！");
        }
        return modelMap;
    }


    @RequestMapping(value = "/changelocalpwd", method = RequestMethod.POST)
    @ResponseBody
    /**
     * 修改密码
     */
    private Map<String, Object> changeLocalPwd(HttpServletRequest request) {
        HashMap<String, Object> modelMap = new HashMap<>();

        // 验证码校验
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "输入了错误的验证码");
            return modelMap;
        }
        String userName = HttpServletRequestUtil.getString(request, "userName");
        String password = HttpServletRequestUtil.getString(request, "password");
        String newPassword = HttpServletRequestUtil.getString(request, "newPassword");
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        if(userName!=null && password!=null && newPassword!=null && !password.equals(newPassword) && user!=null && user.getUserId()!=null){
            try{
                // 检查原账号和当前登录账号是否一致
                System.out.println(user.getUserId());
                LocalAuth localAuth = localAuthService.getLocalAuthByUserId(user.getUserId());
                if (localAuth == null || !localAuth.getUsername().equals(userName)) {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", "输入的帐号非本次登录的帐号");
                    return modelMap;
                }

                LocalAuthExecution localAuthExecution = localAuthService.modifyLocalAuth(user.getUserId(), userName, password, newPassword);
                if(localAuthExecution.getState()==LocalAuthStateEnum.SUCCESS.getState()){
                    modelMap.put("success", true);
                }else{
                    modelMap.put("success", false);
                    modelMap.put("errMsg", localAuthExecution.getStateInfo());
                }
            }catch(Exception e){
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
        }else{
            modelMap.put("success", false);
            modelMap.put("errMsg", LocalAuthStateEnum.NULL_AUTH_INFO.getStateInfo());
        }
        return modelMap;
    }

    @RequestMapping(value = "/logincheck", method = RequestMethod.POST)
    @ResponseBody
    //使用账号密码登录
    private Map<String, Object> logincheck(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        //验证码
        boolean needVerify = HttpServletRequestUtil.getBoolean(request, "needVerify");
        if (needVerify && !CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "输入了错误的验证码");
            return modelMap;
        }
        // 获取输入的帐号和密码
        String userName = HttpServletRequestUtil.getString(request, "userName");
        String password = HttpServletRequestUtil.getString(request, "password");

        if (userName != null && password != null) {
            LocalAuth localAuth = localAuthService.getLocalAuthByUsernameAndPwd(userName, password);
            if (localAuth != null) {
                modelMap.put("success", true);
                request.getSession().setAttribute("user", localAuth.getPersonInfo());
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", "用户名/密码错误");
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", LocalAuthStateEnum.NULL_AUTH_INFO.getStateInfo());
        }
        return modelMap;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    //当用户点击登出按钮的时候注销session
    private Map<String, Object> logout(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        // 将session中的用户对象置为空
        request.getSession().setAttribute("user", null);
        modelMap.put("success", true);
        return modelMap;
    }

}
