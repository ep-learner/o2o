package com.o2o.service.impl;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.o2o.dao.AreaDao;
import com.o2o.cache.JedisUtil;
import com.o2o.entity.Area;
import com.o2o.exceptions.AreaOperationException;
import com.o2o.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AreaServiceImpl implements AreaService {

    @Autowired
    private AreaDao areaDao;

    @Autowired
    private JedisUtil.Keys jedisKeys;

    @Autowired
    private JedisUtil.Strings jedisStrings;

    private final String AREA_LIST_KEY = "arealist";

    /**
     * 如果redis数据库没有，则调用dao从mysql取出并添加进redis数据库
     * 如果redis数据库有，取出value，并转换为arealist
     * @return
     */
    @Override
    public List<Area> getAreaList() {
        String key = AREA_LIST_KEY;
        List<Area> arealist = null;
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = null;
        if(!jedisKeys.exists(key)){
            arealist = areaDao.selectArea();
            try{
                jsonString = mapper.writeValueAsString(arealist);
                jedisStrings.set(key, jsonString);
            }catch(Exception e){
                e.printStackTrace();
                throw new AreaOperationException(e.getMessage());
            }
        }else{
            jsonString = jedisStrings.get(key);
            JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, Area.class);
            try{
                arealist = mapper.readValue(jsonString, javaType);
            }catch(Exception e){
                e.printStackTrace();
                throw new AreaOperationException(e.getMessage());
            }
        }
        return arealist;
    }

}
