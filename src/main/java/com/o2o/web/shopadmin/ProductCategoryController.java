package com.o2o.web.shopadmin;

import com.o2o.entity.ProductCategory;
import com.o2o.entity.Shop;
import com.o2o.enums.OperationStatusEnum;
import com.o2o.enums.ProductCategoryStateEnum;
import com.o2o.exceptions.ProductCategoryOperationException;
import com.o2o.service.ProductCategoryService;
import com.o2o.dto.ProductCategoryExecution;
import com.o2o.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/shopadmin")
@Controller
/**
 * 操作商品类别：查询，增添和删除
 * getProductCategoryList(request)	从请求中解析出shopId，查找店铺经营的商品类别
 * addProductCategorys(productCategoryList,request)	为店铺添加商品列表
 * removeProductCategory(CategoryId,request)	删除商品目录
 */
public class ProductCategoryController {

    @Autowired
    ProductCategoryService pcs;

    /**
     * 从请求中解析出shopId，查找店铺所拥有的商品类别
     */
    @RequestMapping(value = "/getproductcategorylist", method = RequestMethod.GET)
    @ResponseBody
    public Result<List<ProductCategory>> getProductCategoryList(HttpServletRequest request){

        Shop currentShop;
        currentShop = (Shop)request.getSession().getAttribute("currentShop");

        List<ProductCategory> productCategoryList;
        ProductCategoryStateEnum ps;
        if(currentShop!=null && currentShop.getShopId()>0){
            try{
                productCategoryList = pcs.getProductCategoryList(currentShop.getShopId());
                return new Result<>(productCategoryList,true);
            }catch(Exception e){
                ps = ProductCategoryStateEnum.EDIT_ERROR;
                return new Result<>(ps.getStateInfo()+e.getMessage(), false, ps.getState());
            }
        }else {
            ps = ProductCategoryStateEnum.NULL_SHOP;
            return new Result<>(ps.getStateInfo(), false, ps.getState());
        }
    }


    /**
     * 为店铺添加商品列表
     * @param productCategoryList
     * @param request
     * @return
     */

    @RequestMapping(value = "/addproductcategorys", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> addProductCategorys(@RequestBody List<ProductCategory> productCategoryList,
                                                   HttpServletRequest request) {
        HashMap<String, Object> modelMap = new HashMap<>();
        if(productCategoryList!=null && productCategoryList.size()!=0){

            //为商铺产品列表的Id赋值
            Shop currentShop = (Shop)request.getSession().getAttribute("currentShop");
            if(currentShop!=null && currentShop.getShopId()>0){
                for (ProductCategory p:productCategoryList) {
                    p.setShopId(currentShop.getShopId());
                    p.setCreateTime(new Date());
                }
            }

            //执行插入产品列表方法 记录
            try{
                ProductCategoryExecution productCategoryExecution = pcs.batchInsertProductCategory(productCategoryList);
                if(productCategoryExecution.getState()== OperationStatusEnum.SUCCESS.getState()){
                    modelMap.put("success",true);
                    modelMap.put("effectNum",productCategoryExecution.getCount());
                }else {
                    modelMap.put("success",false);
                    modelMap.put("errMsg",productCategoryExecution.getStateInfo());
                }
            }catch(Exception e){
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }

        }else {//产品列表为空
            modelMap.put("success", false);
            modelMap.put("errMsg", ProductCategoryStateEnum.EMPETY_LIST.getStateInfo());
        }
        return modelMap;
    }


    //删除商品目录
    @RequestMapping(value = "/removeproductcategory", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> removeProductCategory(Long productCategoryId, HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if (productCategoryId != null && productCategoryId > 0) {
            // 从session中获取shop的信息
            Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
            if (currentShop != null && currentShop.getShopId() != null) {
                try {
                    // 删除
                    Long shopId = currentShop.getShopId();
                    ProductCategoryExecution pce = pcs.deleteProductCategory(productCategoryId, shopId);
                    if (pce.getState() == OperationStatusEnum.SUCCESS.getState()) {
                        modelMap.put("success", true);
                    } else {
                        modelMap.put("success", false);
                        modelMap.put("errMsg", pce.getStateInfo());
                    }
                } catch (ProductCategoryOperationException e) {
                    e.printStackTrace();
                    modelMap.put("success", false);
                    modelMap.put("errMsg", e.getMessage());
                    return modelMap;
                }
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", ProductCategoryStateEnum.NULL_SHOP.getStateInfo());
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", ProductCategoryStateEnum.EMPETY_LIST.getStateInfo());
        }
        return modelMap;
    }

}
