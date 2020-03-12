package com.o2o.enums;

public enum ProductCategoryStateEnum {
    SUCCESS(1, "操作成功"),NULL_SHOP(-2001, "Shop信息为空"), EMPETY_LIST(-2002, "请输入商品目录信息"),
    DELETE_ERROR(-2003, "商品类别删除失败"), EDIT_ERROR(-2004, "商品类别编辑失败");
    private int state;
    private String stateInfo;

    ProductCategoryStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
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
}
