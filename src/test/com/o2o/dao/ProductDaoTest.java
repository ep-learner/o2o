package com.o2o.dao;

import com.o2o.BaseTest;
import com.o2o.entity.Product;
import com.o2o.entity.ProductCategory;
import com.o2o.entity.Shop;
import com.o2o.enums.EnableStatusEnum;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

public class ProductDaoTest extends BaseTest {

    @Autowired
    private ProductDao productDao;

    @Test
    public void testInsertProduct() {
        Shop shop = new Shop();
        shop.setShopId(2L);
        ProductCategory productCategory = new ProductCategory();
        productCategory.setProductCategoryId(1L);
        Product product = new Product();
        product.setCreateTime(new Date());
        product.setEnableStatus(EnableStatusEnum.AVAILABLE.getState());
        product.setProductName("康师傅红茶");
        product.setProductDesc("康师傅红茶");
        product.setImgAddr("222");
        product.setNormalPrice("3");
        product.setPromotionPrice("2.5");
        product.setPriority(2);
        product.setProductCategory(productCategory);
        product.setShop(shop);
        int effectNum = productDao.insertProduct(product);
        System.out.println("影响行数"+effectNum);
        //System.out.println(product.getProductId());
    }
    @Test
    public void testASelect() {
        /**
         *  Product selectProductByProductId(long productId);
         */
        Product product = productDao.selectProductByProductId(1L);
        System.out.println(product.toString());


    }

    @Test
    public void testBSelect() {
        Product productCondition = new Product();
        Shop shop = new Shop();
        shop.setShopId(5L);
        productCondition.setShop(shop);

        List<Product> products = productDao.selectProductList(productCondition, 0, 10);
        int count = productDao.selectProductCount(productCondition);
        System.out.println(products.size());
    }

    @Test
    public void testUpdateProduct() throws Exception {
        Product product = new Product();
        product.setProductId(1L);
        Shop shop = new Shop();
        shop.setShopId(5L);
        product.setShop(shop);
        product.setProductName("统一红茶");
        int effectNum = productDao.updateProduct(product);
        System.out.println("effectNum:" + effectNum);
    }

    @Test
    public void testUpdateProductCategoryToNull() throws Exception {
        int effectNum = productDao.updateProductCategoryToNull(5L);
        System.out.println("effectNum:" + effectNum);
    }
}
