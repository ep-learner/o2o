package com.o2o.web.front;

import com.o2o.entity.HeadLine;
import com.o2o.entity.ShopCategory;
import com.o2o.enums.EnableStatusEnum;
import com.o2o.service.HeadLineService;
import com.o2o.service.ShopCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/front")
public class MainPageController {

    @Autowired
    HeadLineService headLineService;

    @Autowired
    ShopCategoryService shopCategoryService;

    /**
     * 获取一级店铺类别 key = shopCategoryList
     * 获取头条信息 key = headLineList
     */
    @RequestMapping(value = "/listmainpageinfo",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> listMainPageInfo(){
        HashMap<String, Object> modelMap = new HashMap<>();
        //获取一级店铺类别
        List<ShopCategory> shopCategoryList;
        try{
            shopCategoryList = shopCategoryService.getShopCategoryList(null);
            modelMap.put("shopCategoryList",shopCategoryList);
        }catch(Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
            return modelMap;
        }

        //获取头条信息
        HeadLine headLineCondition = new HeadLine();
        headLineCondition.setEnableStatus(EnableStatusEnum.AVAILABLE.getState());
        List<HeadLine> headLineList ;
        try {
            headLineList = headLineService.getHeadLineList(headLineCondition);
            modelMap.put("headLineList", headLineList);
        } catch (IOException e) {
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
            return modelMap;
        }
        modelMap.put("success", true);
        return modelMap;
    }
}
