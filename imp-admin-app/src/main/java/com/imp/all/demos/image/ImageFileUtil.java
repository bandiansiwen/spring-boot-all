package com.imp.all.demos.image;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * @author Longlin
 * @date 2023/12/26 15:01
 * @description
 * 图片缩放裁剪，图片像素比例变更，批量转换图片像素比
 */
@Slf4j
public class ImageFileUtil {


    public static void main(String[] args) {
    	// 处理目录下的所有jpg格式文件，裁剪图片尺寸变为3840:2160
//        String sourceImgDir = "D:\\MyData\\lill169\\Documents\\WeChat Files\\wxid_4337743381512\\FileStorage\\File\\2023-12\\src\\";
//        String targetImgDir = "D:\\MyData\\lill169\\Documents\\WeChat Files\\wxid_4337743381512\\FileStorage\\File\\2023-12\\dist\\";
        String sourceImgDir = "E:\\image\\src\\";
        String targetImgDir = "E:\\image\\dist\\";
        Arrays.stream(Objects.requireNonNull(new File(sourceImgDir).listFiles())).filter(p -> p.getName().endsWith(".jpg"))
                .forEach(p -> {
                    try {
                        imgDpiModify(p, new File(StringUtils.join(targetImgDir, p.getName())), 295,413,  Boolean.TRUE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }


    public static void imgDpiModify(String sourceImgPath, String targetImgPath, Integer targetWidth, Integer targetHeight, Boolean dealOptimizeFlag) throws IOException {
        imgDpiModify(new File(sourceImgPath), new File(targetImgPath), targetWidth, targetHeight, dealOptimizeFlag);
    }

    /**
     * 图片按照中心点进行缩放和裁剪，达到需要的图片宽高像素比
     *
     * @param sourceImgFile 原图片文件
     * @param targetImgFile 目标图片文件
     * @param targetWidth 目标图片像素宽度
     * @param targetHeight 目标图片像素高度
     * @return true: 成功，false: 失败
     * @param dealOptimizeFlag 处理优化标识，true：处理优化，如果原图片宽高比大，则自动替换宽和高的值
     *                         eg: 图片宽度: 1000，高度: 800，传入参数: targetWidth: 3840, targetHeight: 2160，那么会优化成：targetWidth: 2160, targetHeight: 3840，保证最佳展示
     * @throws IOException IO异常
     */
    @SneakyThrows
    public static boolean imgDpiModify(File sourceImgFile, File targetImgFile, Integer targetWidth, Integer targetHeight, Boolean dealOptimizeFlag) {
        // 原始图片
        BufferedImage sourceBufImg = ImageIO.read(sourceImgFile);
        // 1.获取纠正
        RectifyDirection rotateAngle = getRectifyDirection(sourceImgFile);
        // 2.还原图片
        BufferedImage bufferedImage = restoreImage(sourceBufImg, rotateAngle);

        int originalWidth = bufferedImage.getWidth();
        int originalHeight = bufferedImage.getHeight();
        // 优化宽高比
        if (Boolean.TRUE.equals(dealOptimizeFlag)) {
            // 原图片宽度比高度大，那么应该满足: targetWidth >= targetHeight，如果不满足，则替换，反之，原图片宽度比高度小，那么应该满足: targetWidth <= targetHeight，否则替换
            if ((originalWidth > originalHeight && targetWidth < targetHeight)
                    || (originalWidth < originalHeight && targetWidth > targetHeight)) {
                int temp = targetWidth;
                targetWidth = targetHeight;
                targetHeight = temp;
            }
        }

        // 计算图片需要的缩放比例（宽高和原宽高做对比，取大值）【就是满足目标宽度和高度，需要对图片进行的缩放比】
        double imgScaleValue = Math.max((double) targetWidth / originalWidth, (double) targetHeight / originalHeight);
        // 缩放后的图片宽度
        int scaledImgWidth = (int) Math.round(originalWidth * imgScaleValue);
        // 缩放后的图片高度
        int scaledImgHeight = (int) Math.round(originalHeight * imgScaleValue);

        // 对图像进行缩放，获取缩放后的图片BufferedImage
        BufferedImage scaleBufImg = ImageScaleUtil.scale(bufferedImage, scaledImgWidth, scaledImgHeight);

        // 取中间区域的左上角坐标x，y开始截图下标【缩放后变更为要求的图片比例尺寸，进行截取的开始坐标】
        int xStartIndex = (scaledImgWidth - targetWidth) / 2;
        int yStartIndex = (scaledImgHeight - targetHeight) / 2;

        // 对缩放后的图片，从中间区域开始截取（用于获取原始图像中指定区域的子图像），达到希望得到的目标图片的宽高比例
        // 参数分别是：截取图像左上角的 x 坐标、y 坐标， 截取图像的宽度、高度
        BufferedImage croppedTargetBufImg = scaleBufImg.getSubimage(xStartIndex, yStartIndex, targetWidth, targetHeight);
        // 将截取好的目标图像写入到目标文件中，参数1 (RenderedImage): 要写入文件或输出流的图像数据。
        // 参数2 (formatName): 指定要写入的图像格式的名称，如 jpg、png、bmp、gif，（这里默认使用JPG），告知 ImageIO 使用哪个图像编解码器来处理图像数据
        // 参数3 (output): 要写入的文件或输出流。可以是一个 File 对象或 OutputStream 对象
        boolean writeFlag = ImageIO.write(croppedTargetBufImg, "JPG", targetImgFile);

        // 设置DPI
        ImageDPIHandleUtil.handleDpiJPG(targetImgFile, 300, 300);

        log.info("====== sourcePath = {}, targetPath = {}, result success = {}", sourceImgFile.getPath(), targetImgFile.getPath(), writeFlag);

        return writeFlag;
    }


    /**
     * 获取纠正方向
     * @param file
     * @return
     * @throws Exception
     * orientation:
     * 1	Top	        Left side	无需纠正
     * 2	Top	        Right side	水平翻转（镜像）
     * 3	Bottom	    Right side	垂直翻转（旋转180°）
     * 4	Bottom	    Left side	水平翻转+垂直翻转
     * 5	Left side	Top	        水平翻转+旋转90°
     * 6	Right side	Top	        旋转90°
     * 7	Right side	Bottom	    水平翻转+旋转270°
     * 8	Left side	Bottom	    +旋转270°
     */
    public static RectifyDirection getRectifyDirection(File file) throws Exception {
        // 1.获取图片的元数据
        Metadata metadata = ImageMetadataReader.readMetadata(file);
        // 2.图片元数据处理那种方向
        int orientation = 0;

        for (Directory directory : metadata.getDirectories()) {
            // 3.只需要TAG_ORIENTATION的数据即可。
            if(directory.containsTag(ExifIFD0Directory.TAG_ORIENTATION)){
//                for (Tag tag : directory.getTags()) {
//                    log.info("tag:{}", tag);
//                }
                orientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
            }
        }
        log.info("fileName:{},orientation:{}", file.getName(), orientation);
        // 4.根据不同的orientation创建不同的处理方式
        switch (orientation){
            case 2:
                return new RectifyDirection(0,true);
            case 3:
                return new RectifyDirection(180,false);
            case 4:
                return new RectifyDirection(180, true);
            case 5:
                return new RectifyDirection(90,true);
            case 6:
                return new RectifyDirection(90,false);
            case 7:
                return new RectifyDirection(270,true);
            case 8:
                return new RectifyDirection(270,false);
            default:
                return null;
        }
    }

    static class RectifyDirection{
        /**
         * 角度
         */
        public int angel;
        /**
         * 是否镜像
         */
        public boolean isMirror;

        public RectifyDirection(int angel, boolean isMirror) {
            this.angel = angel;
            this.isMirror = isMirror;
        }
    }

    /**
     * 还原图片
     * @param sourceImage
     * @param rotateAngle
     * @return
     */
    public static BufferedImage restoreImage(BufferedImage sourceImage, RectifyDirection rotateAngle) {

        if (rotateAngle == null) {
            return sourceImage;
        }

        // 创建一个 AffineTransform 对象，用于执行图像变换
        AffineTransform transform = new AffineTransform();

        // 图像的原点（左上角）平移至图像的中心
        transform.translate(sourceImage.getHeight() / 2.0, sourceImage.getWidth() / 2.0);
        // 旋转
        transform.rotate(Math.toRadians(rotateAngle.angel));
        // 是否翻转
        if (rotateAngle.isMirror) {
            // 水平翻转
            transform.scale(-1, 1);
        }
        // 图像的原点（左上角）平移至原处
        transform.translate(-sourceImage.getWidth() / 2.0, -sourceImage.getHeight() / 2.0);

        // 创建一个 AffineTransformOp 对象，用于执行 AffineTransform
        AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);

        // 创建一个新的 BufferedImage 对象，用于存储变换后的图像
        BufferedImage transformedImage = new BufferedImage(sourceImage.getHeight(), sourceImage.getWidth(), sourceImage.getType());

        // 执行 AffineTransform
        op.filter(sourceImage, transformedImage);

        return transformedImage;
    }

    /**
     * 图片缩放工具类
     */
    public static class ImageScaleUtil {

        /**
         * 定义默认的最优插值算法映射表（根据图像类型自动选择最优插值算法，以获得最佳效果）
         *
         * 主要是三种插值算法：
         * 1. RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR（最近邻插值算法），特点: 像素值直接采用原来最近的像素值，优势和场景: 速度快，适用于图像放大倍数不超过2倍的情况
         * 2. RenderingHints.VALUE_INTERPOLATION_BILINEAR（双线性插值算法），特点：根据周围4个像素计算新像素值， 优势和场景：速度快，适用于图像放大倍数不超过2倍的情况
         * 3. RenderingHints.VALUE_INTERPOLATION_BICUBIC（双三次插值算法）， 特点：根据周围16个像素计算新像素值，图像质量高，适用于图像放大倍数较大的情况
         */
        private static final Map<Integer, Object> DEFAULT_INTERPOLATION_MAP = new HashMap<>();

        static {
            // 将 BufferedImage.TYPE_BYTE_GRAY（表示一个 8 位灰度图像，每个像素值存储在 8 位无符号字节中） 类型的图像对应的最优插值算法设置为 NEAREST_NEIGHBOR（最近邻插值）
            DEFAULT_INTERPOLATION_MAP.put(BufferedImage.TYPE_BYTE_GRAY, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            // 将 BufferedImage.TYPE_USHORT_GRAY（表示一个 16 位灰度图像，每个像素值存储在 16 位无符号 short 类型中） 类型的图像对应的最优插值算法设置为 NEAREST_NEIGHBOR（最近邻插值）
            DEFAULT_INTERPOLATION_MAP.put(BufferedImage.TYPE_USHORT_GRAY, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            // 将 BufferedImage.TYPE_3BYTE_BGR（表示一个 8 位 BGR 颜色分量的图像，颜色存储在 24 位字节数组中） 类型的图像对应的最优插值算法设置为 BILINEAR（双线性插值）
            DEFAULT_INTERPOLATION_MAP.put(BufferedImage.TYPE_3BYTE_BGR, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            // 将 BufferedImage.TYPE_4BYTE_ABGR（表示一个 8 位 ABGR 颜色分量的图像，颜色存储在 32 位字节数组中） 类型的图像对应的最优插值算法设置为 BILINEAR（双线性插值）
            DEFAULT_INTERPOLATION_MAP.put(BufferedImage.TYPE_4BYTE_ABGR, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            // 将 BufferedImage.TYPE_INT_ARGB（表示一个 8 位 RGBA 颜色分量的图像，颜色存储在 32 位整数中） 类型的图像对应的最优插值算法设置为 BICUBIC（双三次插值）
            DEFAULT_INTERPOLATION_MAP.put(BufferedImage.TYPE_INT_ARGB, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            // 将 BufferedImage.TYPE_INT_RGB（表示一个 8 位 RGB 颜色分量的图像，颜色存储在 32 位整数中） 类型的图像对应的最优插值算法设置为 BICUBIC（双三次插值）
            DEFAULT_INTERPOLATION_MAP.put(BufferedImage.TYPE_INT_RGB, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            // 将 BufferedImage.TYPE_CUSTOM（表示一个自定义类型的图像，其颜色模型由开发人员指定） 类型的图像对应的最优插值算法设置为 BICUBIC（双三次插值）
            DEFAULT_INTERPOLATION_MAP.put(BufferedImage.TYPE_CUSTOM, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        }

        /**
         * 对图像进行最优缩放，返回缩放后的图片
         *
         * @param sourceImg 原始图像 BufferedImage
         * @param width 缩放后的宽度
         * @param height 缩放后的高度
         * @return 缩放后的图像 BufferedImage
         */
        public static BufferedImage scale(BufferedImage sourceImg, int width, int height) {

            // 根据图像类型选择最优插值算法，默认为 RenderingHints.VALUE_INTERPOLATION_BICUBICC（双三次插值）
            RenderingHints renderingHints = new RenderingHints(RenderingHints.KEY_INTERPOLATION,
                    DEFAULT_INTERPOLATION_MAP.getOrDefault(sourceImg.getType(), RenderingHints.VALUE_INTERPOLATION_BICUBIC));

            // 创建 BufferedImage，对图片宽高进行缩放，并使用原始图像相同的颜色模型，参数分别为：图像宽度、图像高度、图像的颜色模型类型（这里使用原图像类型）
            BufferedImage scaledImage = new BufferedImage(width, height, sourceImg.getType());

            // 创建缩放图像上进行绘制操作的 Graphics2D 对象（图形上下文）
            Graphics2D graphics2D = scaledImage.createGraphics();
            try {
                // 设置 最优插值算法
                graphics2D.setRenderingHints(renderingHints);
                // 缩放裁剪图片，将指定的图像绘制在Graphics2D对象的当前坐标系中，在目标矩形区域内进行缩放和裁剪。
                // 就是：如果目标矩形区域和源图像的宽高比不同，则源图像将按照目标区域的宽高比例进行缩放。如果目标矩形区域超出了源图像的边界，则会进行裁剪
                // observer为null，表示使用默认的图像观察者（java.awt.image.ImageObserver）
                graphics2D.drawImage(sourceImg, 0, 0, width, height, null);
            } finally {
                // 释放 Graphics2D 资源
                graphics2D.dispose();
            }

            // 返回缩放完成后的图片内容
            return scaledImage;
        }
    }
}