package com.o2o.dto;

public class Result<T> {

    private boolean success;//操作是否成功的标志

    private T data;//成功时返回的数据

    private String errorMsg;//错误信息

    private int errorCode;

    public Result() {}

    //成功时需要返回数据和操作成功的标识
    public Result(T data,boolean success){
        this.data=data;
        this.success=success;
    }

    //操作失败返回错误信息
    public Result(String errorMsg,boolean success,int errorCode){
        this.errorMsg=errorMsg;
        this.errorCode=errorCode;
        this.success=success;
    }

    //get和set方法
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
