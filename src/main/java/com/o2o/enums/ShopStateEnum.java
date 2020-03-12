package com.o2o.enums;

/**
 * 店铺状态的枚举类
 */
public enum ShopStateEnum {
    CHECK(0, "审核中"), PASS(1, "通过认证"), OFFLINE(-2001, "非法商铺"), EDIT_ERROR(-2002, "店铺操作失败"),
    NULL_SHOPID(-2003, "ShopId为空"), NULL_SHOP_INFO(-2004, "店铺信息为空");
    /**
     * ShopStateEnum.CHECK.getState()
     */
    private int state;
    private String stateInfo;
    ShopStateEnum(int state,String stateInfo){
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    // 根据index返回枚举对象
    public static ShopStateEnum stateOf(int index) {
        for (ShopStateEnum Enum : ShopStateEnum.values()) {
            if (Enum.getState() == index) {
                return Enum;
            }
        }
        return null;
    }

}
