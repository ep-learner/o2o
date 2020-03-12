package com.o2o.dao.split;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 使用ThreadLocal存储链接类型的字符串常量
 */
public class DynamicDataSourceHolder {
    private static Logger logger = LoggerFactory.getLogger(DynamicDataSourceHolder.class);
    private static ThreadLocal<String> contextHolder = new ThreadLocal<>();
    static final String DB_MASTER = "master";
    public static final String DB_SLAVE = "slave";

    public static String getDbType(){
        String db = contextHolder.get();
        if(db==null){ db = DB_MASTER; }
        return db;
    }

    public static void setDbType(String str){
        logger.debug("所使用的数据源为" + str);
        contextHolder.set(str);
    }

    public static void clearDBType(){
        contextHolder.remove();
    }
}
