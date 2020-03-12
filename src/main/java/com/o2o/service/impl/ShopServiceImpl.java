package com.o2o.service.impl;

import com.o2o.entity.Shop;
import com.o2o.dao.ShopDao;
import com.o2o.dto.ShopExecution;
import com.o2o.enums.EnableStatusEnum;
import com.o2o.enums.OperationStatusEnum;
import com.o2o.enums.ShopStateEnum;
import com.o2o.exceptions.ShopOperationException;
import com.o2o.service.ShopService;
import com.o2o.util.ImageUtil;
import com.o2o.util.PageCalculator;
import com.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopDao shopDao;

    /**
     *
     * @param shopCondition 筛选条件
     * @param pageIndex 前端展示的页数
     * @param pageSize  前端展示的页面大小
     * @return
     */
    @Override
    public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) {
        int rowIndex = PageCalculator.calculateRowIndex(pageIndex,pageSize);
        List<Shop> shopList = shopDao.selectShopList(shopCondition, rowIndex, pageSize);
        int count = shopDao.selectShopCount(shopCondition);
        ShopExecution se = new ShopExecution();
        if(shopList.size()!=0){
            se.setShopList(shopList);
            se.setCount(count);
        }else {
            se.setState(ShopStateEnum.OFFLINE.getState());
        }
        return se;
    }

    /**
     * 包装dao的insert方法 解析图片文件流，为shop_img赋值
     * @param shop
     * @param shopImg ：源图片
     * @return
     * @throws ShopOperationException
     */
    @Override
    public ShopExecution addShop(Shop shop, MultipartFile shopImg) throws ShopOperationException {
        if(shop==null){
            //店铺为空：对于shop的操作失败 对应枚举类的NULL_SHOP_INFO
            //调用店铺操作失败时ShopExecution的构造函数
            return new ShopExecution(ShopStateEnum.NULL_SHOP_INFO);
        }else {
            try{
                shop.setCreateTime(new Date());
                //int型标签对应某一状态String
                //枚举类可以清楚的表示这种映射关系，你不一定能记住0表示什么，但是CHECK你一定知道是哪种情况
                shop.setEnableStatus(EnableStatusEnum.CHECK.getState());
                int effectedNum = shopDao.insertShop(shop);//insert操作影响的行数
                if(effectedNum<=0){//插入操作失败
                    throw  new ShopOperationException(ShopStateEnum.EDIT_ERROR.getStateInfo());
                }else {
                    try{
                        if(shopImg==null){//上传图片为空的异常
                            throw new ShopOperationException(OperationStatusEnum.PIC_EMPTY.getStateInfo());
                        }else {
                            //设置shop的shopImge
                            // shopImge和文件流同名
                            addImage(shop,shopImg);
                            int i = shopDao.updateShop(shop);
                            if(i<=0){//更新操作失败
                                throw new ShopOperationException(ShopStateEnum.EDIT_ERROR.getStateInfo());
                            }
                        }
                    }catch(Exception e){
                        throw new ShopOperationException(ShopStateEnum.EDIT_ERROR.getStateInfo()+e.getMessage());
                    }
                }
            }catch(Exception e){
                throw new ShopOperationException(ShopStateEnum.EDIT_ERROR.getStateInfo()+e.getMessage());
            }
            return new ShopExecution(ShopStateEnum.CHECK,shop);
        }
    }

    /**
     * 根据店铺Id查找Shop
     * @param shopId
     * @return
     */
    @Override
    public Shop getByShopId(long shopId) {
        return shopDao.selectByShopId(shopId);
    }

    /**
     *修改店铺
     * @param shop
     * @param shopImg 源图片
     * @return
     * @throws ShopOperationException
     */
    @Override
    public ShopExecution modifyShop(Shop shop, MultipartFile shopImg) throws ShopOperationException {
        //店铺信息为空
        if(shop==null || shop.getShopId()==null){
            return new ShopExecution(ShopStateEnum.NULL_SHOP_INFO);
        }else{
            try{
                //图片非空 删除旧照片 添加新照片
                if(shopImg!=null){
                    Shop temp = shopDao.selectByShopId(shop.getShopId());
                    if(temp.getShopImg()!=null){
                        ImageUtil.deleteFileOrPath(temp.getShopImg());
                    }
                    shop.setEnableStatus(EnableStatusEnum.CHECK.getState());
                    System.out.println(shop+" "+shopImg.getOriginalFilename());
                    addImage(shop,shopImg);
                }
                //更新修改时间
                shop.setLastEditTime(new Date());
                int i = shopDao.updateShop(shop);
                if(i>0){
                    shop = shopDao.selectByShopId(shop.getShopId());
                    return new ShopExecution(OperationStatusEnum.SUCCESS,shop);//修改成功
                }
                else return new ShopExecution(ShopStateEnum.EDIT_ERROR);
            }catch(Exception e){

                throw new ShopOperationException(ShopStateEnum.EDIT_ERROR.getStateInfo()+e.getMessage());
            }
        }

    }

    /**
     * imageUtil的方法是找到图片加水印
     * 设置shop_img=图片相对路径
     * @param shop
     * @param shopImg：源图片
     */
    private void addImage(Shop shop, MultipartFile shopImg) {
        /**
         * 获取合成图片的路径
         * dest = "/upload/item/shop" + shopId + "/"
         */
        String dest = PathUtil.getShopImagePath(shop.getShopId());

        /**
         * imageUtil的方法是找到图片加水印之后返回图片的相对路径
         * @shopImg 原始图片
         * @dest 存放合成图片的路径  "/upload/item/shop" + shopId + "/"
         * @shopImgAddr 合成图片的相对路径 "/upload/item/shop" + shopId + "/"+图片名称+类型
         */
        String shopImgAddr = ImageUtil.generateThumbnail(shopImg, dest);

        shop.setShopImg(shopImgAddr);
    }
}
