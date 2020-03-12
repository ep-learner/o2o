package com.o2o.service.impl;

import com.o2o.dao.ProductCategoryDao;
import com.o2o.dto.ProductCategoryExecution;
import com.o2o.entity.ProductCategory;
import com.o2o.enums.OperationStatusEnum;
import com.o2o.enums.ProductCategoryStateEnum;
import com.o2o.exceptions.ProductCategoryOperationException;
import com.o2o.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
    @Autowired
    ProductCategoryDao pcd;

    @Override
    public List<ProductCategory> getProductCategoryList(Long shopId) {
        return pcd.queryProductCategoryList(shopId);
    }

    @Override
    public ProductCategoryExecution batchInsertProductCategory(List<ProductCategory> productCategoryList)
            throws ProductCategoryOperationException{
        if(productCategoryList!=null && !productCategoryList.isEmpty()){
            try{
                int num = pcd.batchInsertProductCategory(productCategoryList);
                if(num<=0){
                    throw new ProductCategoryOperationException("");
                }else {
                    return new ProductCategoryExecution(OperationStatusEnum.SUCCESS,productCategoryList,num);
                }
            }catch(Exception e){
                throw new ProductCategoryOperationException(e.getMessage());
            }
        }else {
            return new ProductCategoryExecution(ProductCategoryStateEnum.EMPETY_LIST);
        }

    }

    @Override
    public ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId) throws ProductCategoryOperationException {
        try{
            int effectedNum = pcd.deleteProductCategory(productCategoryId, shopId);
            if(effectedNum>0){
                return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
            }else{
                throw new ProductCategoryOperationException("商品类别删除失败");
            }
        }catch(Exception e){
            throw new ProductCategoryOperationException(e.getMessage());
        }
    }
}
