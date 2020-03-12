package com.o2o.service.impl;

import com.o2o.cache.JedisUtil;
import com.o2o.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CacheServiceImpl implements CacheService {
    @Autowired
    JedisUtil.Keys jediskeys;
    @Override
    public void removeFromCache(String keyPrefix) {
        Set<String> keys = jediskeys.keys(keyPrefix + "*");
        for(String str :keys){
            jediskeys.del(str);
        }
    }
}
