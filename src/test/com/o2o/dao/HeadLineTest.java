package com.o2o.dao;

import com.o2o.BaseTest;
import com.o2o.entity.HeadLine;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HeadLineTest extends BaseTest {

    @Autowired
    HeadLineDao headLineDao;
    @Test
    public void ATestInsert(){
        HeadLine headLine1 = new HeadLine();

        headLine1.setLineName("头条1");
        headLine1.setLineLink("link1");
        headLine1.setLineImg("img1");
        headLine1.setPriority(1);
        headLine1.setEnableStatus(1);
        headLine1.setCreateTime(new Date());
        headLine1.setLastEditTime(new Date());
        headLineDao.insertHeadLine(headLine1);

        HeadLine headLine2 = new HeadLine();
        headLine2.setLineName("头条2");
        headLine2.setLineLink("link2");
        headLine2.setLineImg("img2");
        headLine2.setPriority(1);
        headLine2.setEnableStatus(1);
        headLine2.setCreateTime(new Date());
        headLine2.setLastEditTime(new Date());
        headLineDao.insertHeadLine(headLine2);
    }

    @Test
    public void BTestSelect(){
        String s ;
        HeadLine headLine = new HeadLine();
        headLine.setLineName("头条1");
        List<HeadLine> headLines = headLineDao.selectHeadLineList(headLine);
        System.out.println(headLines.size());

        HeadLine headLine1 = headLineDao.selectHeadLineById(1L);
        ArrayList<Long> longs = new ArrayList<>();
        longs.add(1L);
        longs.add(2L);
        //根据List<ID>查找HeadLine
        List<HeadLine> headLines1 = headLineDao.selectHeadLineByIds(longs);
        System.out.println(headLines.size());
    }
}
