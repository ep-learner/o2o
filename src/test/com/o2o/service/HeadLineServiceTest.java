package com.o2o.service;

import com.o2o.BaseTest;
import com.o2o.entity.HeadLine;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class HeadLineServiceTest extends BaseTest {
    @Autowired
    HeadLineService hls;

    @Test
    public void ATestSelect(){
        HeadLine headLine = new HeadLine();
        headLine.setEnableStatus(1);
        try{
            List<HeadLine> headLineList = hls.getHeadLineList(headLine);
            System.out.println(headLineList.toString());
        }catch(Exception e){
            e.printStackTrace();
        }

    }

}
