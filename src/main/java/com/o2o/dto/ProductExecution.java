package com.o2o.dto;

import com.o2o.entity.Product;
import com.o2o.enums.OperationStatusEnum;
import com.o2o.enums.ProductStateEnum;

import java.util.List;

public class ProductExecution {
    //结果状态
    private int state;

    //状态标识
    private String stateInfo;

    //商品数量
    private int count;

    //商品
    private Product product;

    //商品列表
    private List<Product> productList;

    public ProductExecution(){}

    //操作失败时返回错误信息
    public ProductExecution(ProductStateEnum stateEnum){
        this.state=stateEnum.getState();
        this.stateInfo=stateEnum.getStateInfo();
    }

    //操作成功时返回product信息
    public ProductExecution(OperationStatusEnum stateEnum, Product product){
        this.state=stateEnum.getState();
        this.stateInfo=stateEnum.getStateInfo();
        this.product=product;
    }

    public ProductExecution(OperationStatusEnum stateEnum,List<Product> productList){
        this.state=stateEnum.getState();
        this.stateInfo=stateEnum.getStateInfo();
        this.productList=productList;
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }
}
