package com.o2o.web.front;

import com.o2o.entity.Shop;
import com.o2o.dto.ShopExecution;
import com.o2o.entity.Area;
import com.o2o.entity.ShopCategory;
import com.o2o.service.AreaService;
import com.o2o.service.ShopCategoryService;
import com.o2o.service.ShopService;
import com.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/front")
public class shopListController {

    @Autowired
    ShopService shopService;

    @Autowired
    ShopCategoryService shopCategoryService;

    @Autowired
    AreaService areaService;

    /**
     * 用途：首页 点击某个一级类别显示二级类别信息（或者全部显示）
     * 1、由前端提供一级类别 查找所有店铺类别信息
     * 2、查找区域信息
     * @param request
     * @return
     */
    @RequestMapping(value = "listshopspageinfo", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> listShopsPageinfo(HttpServletRequest request) {
        HashMap<String, Object> modelMap = new HashMap<>();
        long parentId = HttpServletRequestUtil.getLong(request, "parentId");

        //Step1、基于parentId查询二级类别
        // parentId==null，意味着查询所有
        List<ShopCategory> shopCategoryList = null;
        if(parentId>0){
            try{
                ShopCategory shopCategoryCondition = new ShopCategory();
                ShopCategory parent = new ShopCategory();
                parent.setShopCategoryId(parentId);
                shopCategoryCondition.setParent(parent);
                shopCategoryList = shopCategoryService.getShopCategoryList(shopCategoryCondition);
            }catch(Exception e){
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
        } else{//如果没提供parentId则查询所有一级类别下的二级类别
            try {
                shopCategoryList = shopCategoryService.getShopCategoryList(null);
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
        }
        //保存结果
        modelMap.put("shopCategoryList", shopCategoryList);

        //Step2、查询区域信息
        List<Area> areaList = null;
        try {
            areaList = areaService.getAreaList();
            modelMap.put("success", true);
            modelMap.put("areaList", areaList);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        }

        return modelMap;
    }

    /**
     * 一级类别 parentId
     * 二级类别 shopCategoryId
     * 区域名  areaId
     * 商店名 shopName
     * 基于以上条件的组合查询
     * @param request
     * @return
     */
    @RequestMapping(value = "listshops" ,method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> listShops(HttpServletRequest request){
        HashMap<String, Object> modelMap = new HashMap<>();
        // 获取页码
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        // 获取每页显示条数
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");

        if(pageIndex>-1 && pageSize>-1){
            //获取可能的查询条件
            //一级类别Id
            long parentId = HttpServletRequestUtil.getLong(request, "parentId");
            //二级类别Id
            long shopCategoryId = HttpServletRequestUtil.getLong(request, "shopCategoryId");
            //区域信息
            int areaId = HttpServletRequestUtil.getInt(request, "areaId");
            //shopName
            String shopName = HttpServletRequestUtil.getString(request, "shopName");
            if(parentId==0 && shopCategoryId==-1 && areaId==-1 && shopName==null){
                modelMap.put("success", false);
                modelMap.put("errMsg", "parameter is empty");
                return modelMap;
            }
            Shop shopCondition = compact(parentId, shopCategoryId, areaId, shopName);

            ShopExecution shopExecution = shopService.getShopList(shopCondition, pageIndex, pageSize);
            modelMap.put("success", true);
            modelMap.put("shopList", shopExecution.getShopList());
            modelMap.put("count", shopExecution.getCount());
        }else{
            modelMap.put("success", false);
            modelMap.put("errMsg", "pageSize or pageIndex is Empty");
        }
        return modelMap;
    }

    private Shop compact(long parentId,long shopCategoryId,int areaId,String shopName){
        Shop shop = new Shop();
        if(parentId!=-1) {
            ShopCategory parent = new ShopCategory();
            ShopCategory shopCategory = new ShopCategory();
            parent.setShopCategoryId(parentId);
            shopCategory.setParent(parent);
            shop.setShopCategory(shopCategory);
        }
        if(shopCategoryId!=-1){
            //如果直接给了二级类别Id那么把前面的一级类别覆盖掉就好
            ShopCategory shopCategory = new ShopCategory();
            shopCategory.setShopCategoryId(shopCategoryId);
            shop.setShopCategory(shopCategory);
        }
        if(areaId!=-1){
            Area area = new Area();
            area.setAreaId(areaId);
            shop.setArea(area);
        }
        if(shopName!=null){
            shop.setShopName(shopName);
        }
        return shop;
    }
}
