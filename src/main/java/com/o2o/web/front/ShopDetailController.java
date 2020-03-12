package com.o2o.web.front;

import com.o2o.dto.ProductExecution;
import com.o2o.entity.Product;
import com.o2o.entity.ProductCategory;
import com.o2o.entity.Shop;
import com.o2o.enums.ShopStateEnum;
import com.o2o.service.ProductCategoryService;
import com.o2o.service.ProductService;
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
public class ShopDetailController {

    @Autowired
    private ShopService shopService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductCategoryService productCategoryService;

    /**
     * 根据店铺Id显示所有的商品种类
     * 根据店铺Id显示店铺的基本信息
     */
    @RequestMapping(value = "/listshopdetailpageinfo", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> listShopDetailPageInfo(HttpServletRequest request) {

        HashMap<String, Object> modelMap = new HashMap<>();
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        if(shopId!=-1){
            Shop shop = shopService.getByShopId(shopId);
            List<ProductCategory> productCategoryList = productCategoryService.getProductCategoryList(shopId);
            modelMap.put("shop",shop);
            modelMap.put("productCategoryList",productCategoryList);
            modelMap.put("success", true);
        }else{
            modelMap.put("success", false);
            modelMap.put("errMsg", ShopStateEnum.NULL_SHOP_INFO.getStateInfo());
        }
        return modelMap;
    }


    /**
     * 组合查询商品显示商品
     * 查询条件：shopId，类别Id，产品名
     */
    @RequestMapping(value = "listproductsbyshop", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> listProductsByShop(HttpServletRequest request) {
        HashMap<String, Object> modelMap = new HashMap<>();
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        Product productCondition = null;
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        long shopCategoryId = HttpServletRequestUtil.getLong(request, "shopCategoryId");
        String productName = HttpServletRequestUtil.getString(request, "productName");
        if(shopId==-1 && shopCategoryId==-1 && productName==null){//条件均为空的时候 查询失败
            modelMap.put("success", false);
            modelMap.put("errMsg", "condition is null");
            return modelMap;
        }
        productCondition = compact(shopId, shopCategoryId, productName);
        ProductExecution productExecution = productService.getProductList(productCondition, pageIndex, pageSize);
        modelMap.put("productList",productExecution.getProductList());
        modelMap.put("count",productExecution.getCount());
        modelMap.put("success", true);
        return modelMap;
    }

    private Product compact(long shopId,long shopCategoryId,String productName){
        Product product = new Product();
        if(shopId!=-1){
            Shop shop = new Shop();
            shop.setShopId(shopId);
            product.setShop(shop);
        }
        if(shopCategoryId!=-1){
            ProductCategory productCategory = new ProductCategory();
            productCategory.setProductCategoryId(shopCategoryId);
            product.setProductCategory(productCategory);
        }
        if(productName!=null){
            product.setProductName(productName);
        }
        return product;
    }


}
