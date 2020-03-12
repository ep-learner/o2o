package com.o2o.service.impl;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.o2o.cache.JedisUtil;
import com.o2o.dao.HeadLineDao;
import com.o2o.entity.HeadLine;
import com.o2o.service.HeadLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class HeadLineServiceImpl implements HeadLineService {

    @Autowired
    HeadLineDao headLineDao;

    @Autowired
    private JedisUtil.Keys jedisKeys;

    @Autowired
    private JedisUtil.Strings jedisStrings;


    /**
     * 如果redis数据库没有，则调用dao从mysql取出并添加进redis数据库
     * 如果redis数据库有，取出value，headLineList
     */
    @Override
    public List<HeadLine> getHeadLineList(HeadLine headLineCondition) throws IOException {
        //final String HLKEY = "headLineList";
        String key = "headLineList";
        List<HeadLine> headLineList = null;
        ObjectMapper mapper = new ObjectMapper();
        if(headLineCondition!=null && headLineCondition.getEnableStatus()!=null){
            key = key + "_" + headLineCondition.getEnableStatus();
        }
        if(!jedisKeys.exists(key)){
            headLineList = headLineDao.selectHeadLineList(headLineCondition);
            String jedisString = mapper.writeValueAsString(headLineList);
            jedisStrings.set(key, jedisString);
        }else {
            String jedisString = jedisStrings.get(key);
            JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, HeadLine.class);
            headLineList = mapper.readValue(jedisString , javaType);
        }
        return headLineList;
    }


}
