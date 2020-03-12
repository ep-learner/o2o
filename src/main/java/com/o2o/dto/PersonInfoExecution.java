package com.o2o.dto;

import com.o2o.entity.PersonInfo;
import com.o2o.enums.OperationStatusEnum;
import com.o2o.enums.PersonInfoStateEnum;

import java.util.List;

public class PersonInfoExecution {
    // 结果状态
    private int state;

    // 状态标识
    private String stateInfo;

    private int count;

    private PersonInfo localAuth;

    private List<PersonInfo> localAuthList;

    PersonInfoExecution(){}

    //操作失败时的构造函数
    public PersonInfoExecution(PersonInfoStateEnum stateEnum) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    // 成功的构造器
    public PersonInfoExecution(OperationStatusEnum stateEnum) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    // 成功的构造器
    public PersonInfoExecution(OperationStatusEnum stateEnum, PersonInfo localAuth) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.localAuth = localAuth;
    }

    // 成功的构造器
    public PersonInfoExecution(OperationStatusEnum stateEnum, List<PersonInfo> localAuthList) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.localAuthList = localAuthList;
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

    public PersonInfo getLocalAuth() {
        return localAuth;
    }

    public void setLocalAuth(PersonInfo localAuth) {
        this.localAuth = localAuth;
    }

    public List<PersonInfo> getLocalAuthList() {
        return localAuthList;
    }

    public void setLocalAuthList(List<PersonInfo> localAuthList) {
        this.localAuthList = localAuthList;
    }
}
