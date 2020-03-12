package com.o2o.interceptor;

import com.o2o.entity.PersonInfo;
import com.o2o.enums.EnableStatusEnum;
import com.o2o.enums.PersonInfoStatusEnum;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class ShopLoginInterceptor extends HandlerInterceptorAdapter {

    /**
     * 拦截所有shopadmin/**（保证用户是店家）
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 从session中获取用户信息
        Object userObj = request.getSession().getAttribute("user");
        // 如果用户信息存在
        if (userObj != null) {
            // 将session中的用户信息转换为PersonInfo实体类对象
            PersonInfo user = (PersonInfo) userObj;
            // 用户存在且可用，并且用户类型为店家或者管理员
            if (user != null && user.getUserId() != null && user.getUserId() > 0
                    && user.getEnableStatus().equals(EnableStatusEnum.AVAILABLE.getState())) {
                // 如果通过验证，则返回true，用户正常执行后续操作
                return true;
            }
        }
        // 不满足登录条件，则直接跳转后台用户登录页面
        String loginUrl = request.getContextPath() + "/local/login?userType=back";
        response.sendRedirect(loginUrl);
        return false;
    }

}
