package com.o2o.enums;


/**
 * 账号操作状态
 */
public enum PersonInfoStateEnum {
    NULL_PERSON_INFO(-1001, "注册信息为空");

    private int state;

    private String stateInfo;

    PersonInfoStateEnum(int state, String stateInfo) {
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
