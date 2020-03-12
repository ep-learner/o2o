package com.o2o.web.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.o2o.entity.Area;
import com.o2o.entity.PersonInfo;
import com.o2o.entity.Shop;
import com.o2o.entity.ShopCategory;
import com.o2o.enums.OperationStatusEnum;
import com.o2o.enums.ShopStateEnum;
import com.o2o.service.AreaService;
import com.o2o.service.ShopService;
import com.o2o.util.CodeUtil;
import com.o2o.util.HttpServletRequestUtil;
import com.o2o.dto.ShopExecution;
import com.o2o.service.ShopCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/shopadmin")

/**
 *    操作店铺
 *    getShopList(request)	根据用户信息返回用户所拥有的shopList
 *    getshopmanageInfo(request)	从请求中获取shopId,基于shopId查找shop
 *    registerShop(request)	从请求中获取shop对象，然后进行注册
 *    modifyShop(request)	从请求中获取shop对象，然后进行修改
 *    getShopInitInfo()	店铺初始信息。列出全部的店铺和区域
 *    getShopById(request)	从请求中获取shopId,基于shopId查找shop

 */
public class ShopManagementController {
    @Autowired
    private ShopService shopService;
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private AreaService areaService;

    // 根据用户信息返回用户所拥有的shopList
    @RequestMapping(value = "/getshoplist",method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getShopList(HttpServletRequest request){
        HashMap<String, Object> modelMap = new HashMap<>();
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        try{
            Shop shopCondition = new Shop();
            shopCondition.setOwner(user);
            ShopExecution se = shopService.getShopList(shopCondition, 1, 100);
            request.getSession().setAttribute("shopList", se.getShopList());
            modelMap.put("shopList",se.getShopList());
            modelMap.put("success",true);
            modelMap.put("user",user);
        }catch(Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
        }
        return modelMap;
    }


    @RequestMapping(value = "/getshopmanageInfo",method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getshopmanageInfo(HttpServletRequest request){
        HashMap<String, Object> modelMap = new HashMap<>();
        //如果能获取到shopId则认为访问者有权限操作Shop
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        if(shopId<=0){
            Object currentShopObj = request.getSession().getAttribute("currentShop");
            if(currentShopObj==null){
                modelMap.put("success",true);
                modelMap.put("url","/o2o/shopadmin/shoplist");
            }else{
                Shop currentShop = (Shop)currentShopObj;
                modelMap.put("redirect",false);
                modelMap.put("shopId",currentShop.getShopId());
            }
        }else{//从request中获取到shopId，则将信息保存到shop对象中，并将shop保存在session中
            Shop currentShop = new Shop();
            currentShop.setShopId(shopId);
            request.getSession().setAttribute("currentShop",currentShop);
            modelMap.put("redirect",false);
        }
        return modelMap;
    }


    @RequestMapping(value = "/registershop",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> registerShop(HttpServletRequest request){
        Map<String, Object> modelMap = new HashMap<>();
        if(!CodeUtil.checkVerifyCode(request)){
            modelMap.put("success",false);
            modelMap.put("errMsg","输入错误验证码");
            return modelMap;
        }
        //1、接收参数 将参数转换为shop的POJO
        String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
        ObjectMapper mapper = new ObjectMapper();
        Shop shop = null;
        try{
            shop = mapper.readValue(shopStr, Shop.class);
        }catch(Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
            return modelMap;
        }

        //2、获取图片文件流 用户上传的图片
        MultipartHttpServletRequest multipartRequest = null;
        MultipartFile shopImg = null;
        MultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        if (multipartResolver.isMultipart(request)) {
            multipartRequest = (MultipartHttpServletRequest) request;
            shopImg = multipartRequest.getFile("shopImg");
        }
        if (shopImg == null) {
            modelMap.put("success", false);
            modelMap.put("errMsg", OperationStatusEnum.PIC_EMPTY.getStateInfo());
            return modelMap;
        }

        // 3、注册店铺 有了shop和图片流可以调用Service的添加店铺方法
        if (shop != null) {
            PersonInfo owner = (PersonInfo) request.getSession().getAttribute("user");
            shop.setOwner(owner);
            //执行添加店铺的功能
            ShopExecution se = shopService.addShop(shop, shopImg);
            if (se.getState() == ShopStateEnum.CHECK.getState()) {
                modelMap.put("success", true);
                // 用户允许操作的列表
                List<Shop> shopList = (List<Shop>) request.getSession().getAttribute("shopList");
                if (shopList == null || shopList.isEmpty()) {
                    shopList = new ArrayList<>();
                }
                shopList.add(se.getShop());
                request.getSession().setAttribute("shopList", shopList);
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", se.getStateInfo());
            }
            return modelMap;
        } else {//shop为空的情况
            modelMap.put("success", false);
            modelMap.put("errMsg", ShopStateEnum.NULL_SHOP_INFO.getStateInfo());
            return modelMap;
        }

    }

    //修改店铺
    @RequestMapping(value = "/modifyshop",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> modifyShop(HttpServletRequest request){
        Map<String, Object> modelMap = new HashMap<>();
        if(!CodeUtil.checkVerifyCode(request)){
            modelMap.put("success",false);
            modelMap.put("errMsg","输入错误验证码");
            return modelMap;
        }

        //1、接收参数 将参数转换为shop的POJO
        String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
        ObjectMapper mapper = new ObjectMapper();
        Shop shop = null;
        try{
            shop = mapper.readValue(shopStr, Shop.class);
        }catch(Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
            return modelMap;
        }

        //2、修改店铺 获取图片文件流 用户上传的图片
        MultipartHttpServletRequest multipartRequest = null;
        MultipartFile shopImg = null;
        MultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        if (multipartResolver.isMultipart(request)) {
            multipartRequest = (MultipartHttpServletRequest) request;
            shopImg = multipartRequest.getFile("shopImg");
        }

        // 3、修改店铺
        if (shop != null && shop.getShopId()>0)  {
            PersonInfo owner = (PersonInfo) request.getSession().getAttribute("user");
            shop.setOwner(owner);
            ShopExecution se = shopService.modifyShop(shop, shopImg);
            if (se.getState() == OperationStatusEnum.SUCCESS.getState()) {
                modelMap.put("success", true);
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", se.getStateInfo());
            }
            return modelMap;
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", ShopStateEnum.NULL_SHOP_INFO.getStateInfo());
            return modelMap;
        }

    }

    //获取店铺的初始信息
    @RequestMapping(value = "/getshopinitinfo",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getShopInitInfo(){
        HashMap<String, Object> modelMap = new HashMap<>();
        List<ShopCategory> shopCategories ;
        List<Area> areas ;
        try{
            areas = areaService.getAreaList();
            shopCategories = shopCategoryService.getShopCategoryList(new ShopCategory());
            modelMap.put("success",true);
            modelMap.put("areaList",areas);
            modelMap.put("shopCategoryList",shopCategories);
        }catch(Exception e){
            modelMap.put("success",false);
            modelMap.put("msg",e.getMessage());
        }
        return modelMap;
    }


    @RequestMapping(value = "/getshopbyid", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getShopById(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        if (shopId > -1) {
            try {
                Shop shop = shopService.getByShopId(shopId);
                modelMap.put("shop", shop);
                List<Area> areaList = areaService.getAreaList();
                modelMap.put("areaList", areaList);
                modelMap.put("success", true);
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", ShopStateEnum.NULL_SHOPID.getStateInfo());
        }
        return modelMap;
    }

}
