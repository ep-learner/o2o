package com.o2o.service;

import com.o2o.entity.ShopCategory;

import java.util.List;

public interface ShopCategoryService {
    /**
     * 如果是二级目录，属于同一大类的所有类别
     * 如果是一级目录或者为空，则显示所有
     * 需求：1、首页展示一级目录（即parent_id 为 null的店铺类别）
     *    2、点进去某个一级目录加载对应目录下的子目录
     *    3、店铺只能挂在二级类别下
     *    4、在首页点击某个一级店铺目录 进入店铺展示页面的时候 需要加载对应目录下的子目录
     *
     * @param shopCategoryCondition 查询条件
     * @return
     */
    List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition);

}
