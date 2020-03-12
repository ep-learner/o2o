package com.o2o.dto;

import com.o2o.entity.ProductCategory;
import com.o2o.enums.OperationStatusEnum;
import com.o2o.enums.ProductCategoryStateEnum;

import java.util.List;

public class ProductCategoryExecution {
    private int state;
    private String stateInfo;
    private List<ProductCategory> productCategoryList;
    private int count;

    public ProductCategoryExecution() { }

    //操作成功 返回查询结果
    public ProductCategoryExecution(OperationStatusEnum stateEnum, List<ProductCategory> productCategoryList, int count) {
        this.state=stateEnum.getState();
        this.stateInfo=stateEnum.getStateInfo();
        this.productCategoryList=productCategoryList;
        this.count=count;
    }

    //操作失败
    public ProductCategoryExecution(ProductCategoryStateEnum stateEnum) {
        this.state=stateEnum.getState();
        this.stateInfo=stateEnum.getStateInfo();
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public List<ProductCategory> getProductCategoryList() {
        return productCategoryList;
    }

    public void setProductCategoryList(List<ProductCategory> productCategoryList) {
        this.productCategoryList = productCategoryList;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
