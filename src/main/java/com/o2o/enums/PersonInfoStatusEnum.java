package com.o2o.enums;

/**
 * 用户状态
 */
public enum PersonInfoStatusEnum {
    NULL_PERSON_INFO(-1001, "注册信息为空"),ALLOW(1, "允许"), NOT_ALLOW(0, "不允许"),
    CUSTOMER(1, "顾客"), OWNER(2, "店家"), ADMIN(3, "管理员");

    private int state;
    private String stateInfo;

    PersonInfoStatusEnum(int state, String stateInfo) {
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
