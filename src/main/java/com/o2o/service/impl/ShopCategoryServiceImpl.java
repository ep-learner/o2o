package com.o2o.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.o2o.cache.JedisUtil;
import com.o2o.dao.ShopCategoryDao;
import com.o2o.entity.ShopCategory;
import com.o2o.service.ShopCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShopCategoryServiceImpl implements ShopCategoryService {
    @Autowired
    private ShopCategoryDao scd;

    @Autowired
    private JedisUtil.Keys jedisKeys;

    @Autowired
    private JedisUtil.Strings jedisStrings;

    @Override
    public List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition) {
        String key = "shopCategoryList";
        if (shopCategoryCondition == null) {
            // 若查询条件为空，则列出所有首页大类，即parentId为空的店铺类型
            key = key + "_allfirstlevel";
        } else if (shopCategoryCondition.getParent() != null && shopCategoryCondition.getParent().getShopCategoryId() != null) {
            // 若parentId不为空，则列出该parentId下的所有子类别
            key = key + "_parent" + shopCategoryCondition.getParent().getShopCategoryId();
        } else  {
            // 列出所有子类别，不管其属于哪个类都列出
            key = key + "_allsecondlevel";
        }

        ObjectMapper mapper = new ObjectMapper();
        List<ShopCategory> shopCategoryList = null;
        if(!jedisKeys.exists(key)){
            shopCategoryList = scd.selectShopCategory(shopCategoryCondition);
            try {
                String jedisString = mapper.writeValueAsString(shopCategoryList);
                jedisStrings.set(key,jedisString);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }else {
            String jedisString = jedisStrings.get(key);
            JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, ShopCategory.class);
            try {
                shopCategoryList = mapper.readValue(jedisString,javaType);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return shopCategoryList;
    }
}
