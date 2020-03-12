package com.o2o.util;

public class PageCalculator {

    //前端页索引到SQL行索引的转换
    public static int calculateRowIndex(int pageIndex,int pageSize){
        return pageIndex>0?(pageIndex-1)*pageSize:0;
    }
}
