package com.o2o.web.front;

import com.o2o.entity.Product;
import com.o2o.enums.ProductStateEnum;
import com.o2o.service.ProductService;
import com.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/front")
public class ProductDetailController {

    @Autowired
    private ProductService productService;

    /**
     * 获取商品详情
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/listproductdetailpageinfo", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listProductDetailPageInfo(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        long productId = HttpServletRequestUtil.getLong(request, "productId");
        Product product = null;
        if (productId != -1) {
            // 获取商品详情
            product = productService.getProductById(productId);
            modelMap.put("product", product);
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", ProductStateEnum.PRODUCT_ID_EMPTY.getStateInfo());
        }
        return modelMap;
    }
}
