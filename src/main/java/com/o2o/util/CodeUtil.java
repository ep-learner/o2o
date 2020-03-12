package com.o2o.util;

import com.google.code.kaptcha.Constants;

import javax.servlet.http.HttpServletRequest;

public class CodeUtil {
    public static boolean checkVerifyCode(HttpServletRequest request ){
        //输入的验证码
        String verifyCodeActual = HttpServletRequestUtil.getString(request, "verifyCodeActual");
        //图片中的验证码
        String verifyCodeExpexted = (String)request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
        //输入验证码非空且和真实验证码相等（无视大小写）则验证通过
        if(verifyCodeActual==null || !verifyCodeActual.equalsIgnoreCase(verifyCodeExpexted)) return false;
        else return true;
    }
}
