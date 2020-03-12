package com.o2o.interceptor;

import com.o2o.entity.Shop;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ShopPermissionInterceptor extends HandlerInterceptorAdapter {

    //拦截shopadmin/**
    // 保证用户操作的店铺和其拥有的店铺匹配
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        // 从session中获取当前选择的店铺
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        // 获取session中当前用户可操作的店铺列表
        List<Shop> shopList = (List<Shop>) request.getSession().getAttribute("shopList");
        // 非空判断
        if (currentShop != null && shopList != null) {
            // 遍历
            for (Shop shop : shopList) {
                // 如果当前店铺在可操作的列表则返回true，进行后续操作
                if (shop.getShopId() == currentShop.getShopId()) {
                    return true;
                }
            }
        }
        String loginUrl = request.getContextPath() + "/local/login?userType=back";
        response.sendRedirect(loginUrl);
        return false;
    }
}
