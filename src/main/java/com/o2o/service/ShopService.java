package com.o2o.service;

import com.o2o.entity.Shop;
import com.o2o.exceptions.ShopOperationException;
import com.o2o.dto.ShopExecution;
import org.springframework.web.multipart.MultipartFile;

public interface ShopService {

    /**
     *基于condition显示查询结果(index~size)
     * @param shopCondition 筛选条件
     * @param pageIndex 前端展示的页数
     * @param pageSize  前端展示的页面大小
     * @return
     */
    ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize);
    /**
     *插入店铺信息
     * @param shop
     * @param shopImg ：源图片
     * @return
     * @throws ShopOperationException
     */
    ShopExecution addShop(Shop shop, MultipartFile shopImg) throws ShopOperationException;

    /**
     * 基于Id查找店铺
     * @param shopId
     * @return
     */
    Shop getByShopId(long shopId);

    /**
     *更新店铺信息
     * @param shop
     * @param shopImg 源图片
     * @return
     * @throws ShopOperationException
     */
    ShopExecution modifyShop(Shop shop, MultipartFile shopImg) throws ShopOperationException;
}
