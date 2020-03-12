package com.o2o.service;

import com.o2o.dto.ShopExecution;
import com.o2o.entity.Area;
import com.o2o.entity.PersonInfo;
import com.o2o.entity.Shop;
import com.o2o.entity.ShopCategory;
import com.o2o.BaseTest;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


public class ShopServiceTest extends BaseTest {
    @Autowired
    private ShopService shopService;

    /**
     * 测试getShopList方法
     */
    @Test
    public void testGetShopList(){
        Shop shopCondition = new Shop();
//        shopCondition.setShopId(1L);
//        ShopCategory shopCategory = new ShopCategory();
//        shopCategory.setShopCategoryId(1L);
//        shopCondition.setShopCategory(shopCategory);

        ShopExecution shopList = shopService.getShopList(shopCondition, 0, 5);
        System.out.println(shopList.getShopList().size());
        System.out.println(shopList.getCount());
    }
    /**
     * 测试modifyShop方法
     * 通过id查找shop所以id不为空
     *
     * @throws IOException
     */
    @Test
    @Ignore
    public void testModifyShop() throws IOException {
        Shop shop = new Shop();

        /**
         * 提供源图片的路径
         */
        shop.setShopId(1L);
        shop.setShopName("new name hahahahaha");
        String filePath = "D:\\tempImg\\image\\haha.png";
        ShopExecution shopExecution = shopService.modifyShop(shop, path2MultipartFile(filePath));
        System.out.println(shopExecution);
    }


    @Test
    @Ignore
    public void testAddShop() throws IOException {
        Shop shop = new Shop();

        Area area = new Area();
        area.setAreaId(1);
        shop.setArea(area);

        ShopCategory shopCategory = new ShopCategory();
        shopCategory.setShopCategoryId(1L);
        shop.setShopCategory(shopCategory);

        PersonInfo owner = new PersonInfo();
        owner.setUserId(1L);
        shop.setOwner(owner);

        shop.setShopName("店铺222");
        shop.setShopDesc("my_description");
        shop.setShopAddr("test");
        shop.setPhone("test");
        shop.setPriority(1);
        shop.setAdvice("审核中");
        /**
         * 提供源图片的路径
         */
        String filePath = "D:\\tempImg\\image\\mainPic.png";
        ShopExecution shopExecution = shopService.addShop(shop, path2MultipartFile(filePath));
        System.out.println("state"+shopExecution.getState());
        System.out.println("stateInfo"+shopExecution.getStateInfo());
    }

    /**
     * 文件路径到multipartFile的转换
     * @param filePath
     * @return
     * @throws IOException
     */
    private MultipartFile path2MultipartFile(String filePath) throws IOException {
        File file = new File(filePath);
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "text/plain",input);
        return multipartFile;
    }

}
