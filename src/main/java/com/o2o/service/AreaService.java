package com.o2o.service;

import java.util.List;

import com.o2o.entity.Area;

/**
 * @Description: 区域业务接口
 */

public interface AreaService {
    /**
     * 获取区域列表,将区域信息放入缓存中
     */
    List<Area> getAreaList();

}