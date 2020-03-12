package com.o2o.service.impl;

import com.o2o.exceptions.ProductCategoryOperationException;
import com.o2o.exceptions.ProductOperationException;
import com.o2o.dao.ProductDao;
import com.o2o.dao.ProductImgDao;
import com.o2o.dto.ProductExecution;
import com.o2o.entity.Product;
import com.o2o.entity.ProductImg;
import com.o2o.enums.EnableStatusEnum;
import com.o2o.enums.OperationStatusEnum;
import com.o2o.enums.ProductStateEnum;
import com.o2o.service.ProductService;
import com.o2o.util.ImageUtil;
import com.o2o.util.PageCalculator;
import com.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductDao productDao;

    @Autowired
    private ProductImgDao productImgDao;

    /**
     * 综合productDao的添加商品和productImgDao的批处理图片方法，组合成Service层
     *
     * @param product        商品信息
     * @param productImg     商品缩略图
     * @param productImgList 商品图片列表
     * @return
     * @throws ProductOperationException
     */
    @Override
    public ProductExecution addProduct(Product product, MultipartFile productImg, List<MultipartFile> productImgList) throws ProductOperationException {
        if(product != null && product.getShop() != null && product.getShop().getShopId() != null){
            product.setCreateTime(new Date());
            product.setEnableStatus(EnableStatusEnum.AVAILABLE.getState());
            if(productImg!=null){
                addProductImg(product, productImg);
            }
            try{
                int effectNum = productDao.insertProduct(product);
                if(effectNum<=0){
                    throw  new ProductCategoryOperationException(ProductStateEnum.EDIT_ERROR.getStateInfo());
                }
            }catch(Exception e){
                throw new ProductOperationException(e.getMessage());
            }
            if(productImgList!=null){
                addProductImgList(productImgList,product);
            }
            return new ProductExecution(OperationStatusEnum.SUCCESS,product);
        }else {
             return new ProductExecution(ProductStateEnum.EMPTY);
        }
    }

    @Override
    public ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize) {
        int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
        List<Product> products = null;
        try{
            products = productDao.selectProductList(productCondition, rowIndex, pageSize);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        int count = productDao.selectProductCount(productCondition);
        ProductExecution productExecution = new ProductExecution();
        productExecution.setCount(count);
        productExecution.setProductList(products);
        return productExecution;
    }

    @Override
    public Product getProductById(long productId) {
        return productDao.selectProductByProductId(productId);
    }

    /**
     * 商品更新  若有图则先删除
     * @param product        商品信息
     * @param productImg     商品缩略图
     * @param productImgList 商品图片列表
     * @return
     * @throws ProductOperationException
     */
    @Override
    public ProductExecution modifyProduct(Product product, MultipartFile productImg, List<MultipartFile> productImgList) throws ProductOperationException {
        if(product!=null && product.getShop()!=null && product.getShop().getShopId()>0){
            product.setLastEditTime(new Date());
            if(productImg!=null){
                Product oriproduct = productDao.selectProductByProductId(product.getProductId());
                if(null!=oriproduct.getImgAddr()){
                    ImageUtil.deleteFileOrPath(oriproduct.getImgAddr());
                }
                addProductImg(product, productImg);
            }
            if(productImgList!=null &&productImgList.size()!=0){
                deleteProductImgList(product.getProductId());//删除旧图片
                addProductImgList(productImgList,product);//
            }
            try{
                int i = productDao.updateProduct(product);
                if(i<=0){
                    throw new ProductOperationException(ProductStateEnum.EDIT_ERROR.getStateInfo());
                }
                return new ProductExecution(OperationStatusEnum.SUCCESS,product);
            }catch(Exception e){
                throw new ProductOperationException(ProductStateEnum.EDIT_ERROR.getStateInfo()+e.getMessage());
            }
        }else{
            return new ProductExecution(ProductStateEnum.EMPTY);
        }

    }

    /**
     * 基于productId删除详情图
     * 首先删除文件 然后在数据库中删除对应的记录
     * @param productId
     */
    private void deleteProductImgList(long productId) {
        // 根据productId获取原有的图片
        List<ProductImg> productImgList = productImgDao.selectProductImgListByProductId(productId);
        if (productImgList != null && !productImgList.isEmpty()) {
            for (ProductImg productImg : productImgList) {
                // 删除保存的图片
                ImageUtil.deleteFileOrPath(productImg.getImgAddr());
            }
            // 删除数据库中图片
            productImgDao.deleteProductImgByProductId(productId);
        }
    }

    // 输入List<MultipartFile> 和product
    // 将MultipartFile转换为productImg 加入List<productImg> 并设置地址，shopId和时间
    // 批量插入
    private void addProductImgList(List<MultipartFile> productImgList, Product product)  {
        String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
        List<ProductImg> productImgs = new ArrayList<>();
        for(MultipartFile temp:productImgList){
            String addr = ImageUtil.generateThumbnail(temp, dest);
            ProductImg productImg = new ProductImg();
            productImg.setProductId(product.getProductId());
            productImg.setImgAddr(addr);
            product.setCreateTime(new Date());
            productImgs.add(productImg);
        }
        if(productImgs.size()>0){
            try{
                int i = productImgDao.batchInsertProductImg(productImgs);
                if(i<=0){
                    throw new ProductOperationException(OperationStatusEnum.PIC_EMPTY.getStateInfo());
                }
            }catch(Exception e){
                throw new ProductOperationException(OperationStatusEnum.PIC_UPLOAD_ERROR.getStateInfo() + e.toString());
            }
        }
    }

    //添加缩略图
    private void addProductImg(Product product, MultipartFile productImg){
        String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
        String s = ImageUtil.generateThumbnail(productImg, dest);
        product.setImgAddr(s);
    }



}
