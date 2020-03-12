package com.o2o.dao;

import com.o2o.entity.Area;

import java.util.List;

/**
 * 列出区域列表
 */

public interface AreaDao {
    List<Area> selectArea();
    void insertArea(Area area);
}
