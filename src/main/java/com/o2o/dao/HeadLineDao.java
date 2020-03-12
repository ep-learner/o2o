package com.o2o.dao;

import com.o2o.entity.HeadLine;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface HeadLineDao {
    /**
     * 添加首页头条
     * @param headLine
     * @return
     */
    int insertHeadLine(HeadLine headLine);

    /**
     * 按照headline 的enablestatus 筛选 只显示可用的headline
     * @return
     */
    List<HeadLine> selectHeadLineList(@Param("headLineCondition") HeadLine headLineCondition);

    /**
     * 基于List<Id>查询一组头条
     */
    List<HeadLine> selectHeadLineByIds(List<Long> lineIdList);

    /**
     * 基于Id查询头条
     */
    HeadLine selectHeadLineById(Long Id);
}
