package com.o2o.dto;

import com.o2o.entity.Shop;
import com.o2o.enums.OperationStatusEnum;
import com.o2o.enums.ShopStateEnum;

import java.util.List;

/**
 * Shop 的基础上补充操作状态的描述信息
 */
public class ShopExecution {
    //结果状态
    private int state;
    //状态补充
    private String stateInfo;
    //店铺数量
    private int count;
    //操作的店铺
    private Shop shop;
    //店铺List，查询时使用
    private List<Shop> shopList;

    public ShopExecution(){
        //默认构造函数
    }

    //店铺操作失败时的构造器 返回操作失败的类型信息
    public ShopExecution(ShopStateEnum stateEnum){
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    //店铺操作成功时的构造器
    public ShopExecution(ShopStateEnum stateEnum,Shop shop){
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.shop = shop;
    }

    public ShopExecution(OperationStatusEnum statusEnum, Shop shop) {
        this.state = statusEnum.getState();
        this.stateInfo = statusEnum.getStateInfo();
        this.shop = shop;
    }

    public ShopExecution(ShopStateEnum stateEnum,List<Shop> shopList){
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.shopList = shopList;
    }

    public ShopExecution(OperationStatusEnum stateEnum, List<Shop> shopList) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.shopList = shopList;

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

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public List<Shop> getShopList() {
        return shopList;
    }

    public void setShopList(List<Shop> shopList) {
        this.shopList = shopList;
    }

    @Override
    public String toString() {
        return "ShopExecution{" +
                "state=" + state +
                ", stateInfo='" + stateInfo + '\'' +
                ", count=" + count +
                ", shop=" + shop +
                ", shopList=" + shopList +
                '}';
    }
}
