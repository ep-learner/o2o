package com.o2o.util;

public class PathUtil {
    private static String separator = System.getProperty("file.separator");
    /**
     * @return 项目图片的本地存储根路径
     */
    public static String getImgBasePath() {
        // 获取操作系统的信息
        String os = System.getProperty("os.name");
        String basePath = "";
        // 如果是window操作系统
        if (os.toLowerCase().startsWith("win")) {
            basePath = "D:/tempImg/image";
        } else {
            basePath = "/data/o2o/image/";
        }
        // 更换分隔符
        basePath = basePath.replace("/", separator);
        return basePath;
    }


    /**
     * @return 店铺文件的存放位置
     */
    public static String getShopImagePath(long shopId) {
        String imagePath = "/upload/item/shop" + shopId + "/";
        return imagePath.replace("/", separator);
    }

}
