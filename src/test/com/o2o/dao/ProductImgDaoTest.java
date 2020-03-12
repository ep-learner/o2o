package com.o2o.dao;

import com.o2o.BaseTest;
import com.o2o.entity.ProductImg;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;

public class ProductImgDaoTest extends BaseTest {

    @Autowired
    ProductImgDao pid;

    @Test
    public void AInsertTest(){

        ProductImg productImg1 = new ProductImg();
        productImg1.setCreateTime(new Date());
        productImg1.setImgAddr("addr1");
        productImg1.setImgDesc("desc1");
        productImg1.setPriority(1);
        productImg1.setProductId(8L);

        ProductImg productImg2 = new ProductImg();
        productImg2.setCreateTime(new Date());
        productImg2.setImgAddr("addr2");
        productImg2.setImgDesc("desc2");
        productImg2.setPriority(1);
        productImg2.setProductId(8L);
        ArrayList<ProductImg> productImgs = new ArrayList<>();
        productImgs.add(productImg1);
        productImgs.add(productImg2);
        int i = pid.batchInsertProductImg(productImgs);
        System.out.println(i);
    }

}
