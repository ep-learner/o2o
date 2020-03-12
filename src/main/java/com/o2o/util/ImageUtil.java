package com.o2o.util;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class ImageUtil {

    //资源路径 resource目录下的文件
    private static String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    //日期格式
    private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    // 随机数
    private static final Random r = new Random();

    /**
     * 生成商铺缩略图
     * @param thumbnail 源图片
     * @param targetAddr 合成后图片的目标路径
     * @return 返回合成后的图片的相对路径
     * 决定路径 = PathUtil.getImgBasePath() + 相对路径
     * PathUtil.getImgBasePath() = "D:/tempImg/image"
     */
    public static String generateThumbnail(MultipartFile thumbnail, String targetAddr) {

        // 获取随机文件名，防止文件重名
        String realFileName = getRandomFileName();

        // 获取文件扩展名
        String extension = getFileExtension(thumbnail);

        // 在文件夹不存在时创建
        makeDirPath(targetAddr);
        String relativeAddr = targetAddr + realFileName + extension;
        File dest = new File( PathUtil.getImgBasePath() + relativeAddr);
        try {
            Thumbnails.of(thumbnail.getInputStream()).size(1200,600)
                    .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath+ "/watermark.png")), 0.8f)
                    .outputQuality(0.8f).toFile(dest);
        } catch (IOException e) {
            throw new RuntimeException("创建缩略图失败：" + e.getMessage());
        }
        return relativeAddr;
    }

    /**
     * 获取文件扩展名
     * @param thumbnail
     * @return
     */
    static String getFileExtension(MultipartFile thumbnail){
        String originalFilename = thumbnail.getOriginalFilename();//获取文件的完全路径名
        return originalFilename.substring(originalFilename.lastIndexOf("."));//获取.及其后的字符串
    }

    /**
     * 创建目标路径上的目录
     * @param targetAddr
     */
    private static void makeDirPath(String targetAddr) {
        String realFileParentPath = PathUtil.getImgBasePath() + targetAddr;
        File dirPath = new File(realFileParentPath);
        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }
    }

    /**
     * 随机生成一个文件名
     * RandomFileName=Date+random
     * @return
     */
    static String getRandomFileName(){
        //生成随机五位数
        int rannum = (int)(r.nextDouble()*(90000)) + 10000;
        //当前时间
        String nowTime = sDateFormat.format(new Date());
        return nowTime+rannum;
    }

    /**
     * 根据传入的字符串删除整条路径上的文件
     * @param path
     */
    public static void deleteFileOrPath(String path){
        File file = new File(PathUtil.getImgBasePath() + path);
        if(file.exists()){
            if(file.isDirectory()){
                File[] files = file.listFiles();
                for (int i=0;i<files.length;i++){
                    files[i].delete();
                }
            }
            else file.delete();
        }
    }

    public static void main(String[] args) throws IOException {
        Thumbnails.of(new File("D:\\tempImg\\image\\mainPic.png")).size(1200,600)
                .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(
                        new File(basePath+"/watermark.png")),0.9f)
                .outputQuality(0.8).toFile(new File("D:\\tempImg\\image\\result.png"));
    }

    public static MultipartFile path2MultipartFile(String filePath) throws IOException {
        File file = new File(filePath);
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "text/plain",input);
        return multipartFile;
    }


}
