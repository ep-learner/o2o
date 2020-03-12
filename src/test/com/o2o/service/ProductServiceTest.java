package com.o2o.service;

import com.o2o.dto.ProductExecution;
import com.o2o.entity.Product;
import com.o2o.entity.ProductCategory;
import com.o2o.entity.Shop;
import com.o2o.enums.EnableStatusEnum;
import com.o2o.util.ImageUtil;
import com.o2o.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductServiceTest extends BaseTest {

    @Autowired
    ProductService ps;

    @Test
    public void ATestAddProduct() throws IOException {

        ProductCategory productCategory = new ProductCategory();
        productCategory.setProductCategoryId(1L);
        Shop shop = new Shop();
        shop.setShopId(1L);

        Product product = new Product();
        product.setShop(shop);
        product.setProductCategory(productCategory);
        product.setProductName("测试商品1");
        product.setProductDesc("测试商品1描述");
        product.setPriority(11);
        product.setEnableStatus(EnableStatusEnum.AVAILABLE.getState());
        product.setLastEditTime(new Date());
        product.setCreateTime(new Date());

        //缩略图
        String filePath0 = "D:\\tempImg\\image\\1.jpg";

        //详情图
        List<MultipartFile> productImgList = new ArrayList<>();
        String filePath1 = "D:\\tempImg\\image\\2.jpg";
        MultipartFile productImg1 = ImageUtil.path2MultipartFile(filePath1);
        String filePath2 = "D:\\tempImg\\image\\3.jpg";
        MultipartFile productImg2 = ImageUtil.path2MultipartFile(filePath2);
        productImgList.add(productImg1);
        productImgList.add(productImg2);

        ProductExecution se = ps.addProduct(product, ImageUtil.path2MultipartFile(filePath0), productImgList);
        System.out.println("ProductExecution.state" + se.getState());
        System.out.println("ProductExecution.stateInfo" + se.getStateInfo());
    }


    @Test
    public void testModifyShop() throws IOException {
        Product product = new Product();
        Shop shop = new Shop();
        shop.setShopId(2L);
        product.setShop(shop);
        ProductCategory productCategory = new ProductCategory();
        productCategory.setProductCategoryId(2L);
        product.setProductId(3L);
        product.setProductCategory(productCategory);
        product.setProductName("虹彩");
        product.setProductDesc("虹彩");
        product.setPriority(22);
        product.setEnableStatus(EnableStatusEnum.AVAILABLE.getState());
        product.setLastEditTime(new Date());
        product.setCreateTime(new Date());
        String filePath0 = "D:\\tempImg\\image\\3.jpg";
        List<MultipartFile> productImgList = new ArrayList<>();
        String filePath1 = "D:\\tempImg\\image\\3.jpg";
        MultipartFile productImg1 = ImageUtil.path2MultipartFile(filePath1);
        productImgList.add(productImg1);
        String filePath2 = "D:\\tempImg\\image\\3.jpg";
        MultipartFile productImg2 = ImageUtil.path2MultipartFile(filePath2);
        productImgList.add(productImg2);
        ProductExecution se = ps.modifyProduct(product, ImageUtil.path2MultipartFile(filePath0), productImgList);
        System.out.println("ProductExecution.state" + se.getState());
        System.out.println("ProductExecution.stateInfo" + se.getStateInfo());
    }


}
