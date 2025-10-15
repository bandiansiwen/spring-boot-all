package com.imp.all.file.type;

public class ThumbnailLevel {

    /**
     * 原图
     */
    public static final int SRC = 0;

    /**
     * 图片缩略图，裁切成长480，宽480，原比例缩放
     */
    public static final int TN1 = 1;

    /**
     * 图片缩略图，裁切成长480，宽480，截取
     */
    public static final int TN2 = 2;

    /**
     * 图片缩略图，裁切成长960，宽640，截取
     */
    public static final int TN3 = 3;

    /**
     * 图片缩略图，裁切成长1280，宽960，原比例缩放
     */
    public static final int TN4 = 4;

    /**
     * 图片缩略图，裁切成长180，宽180，原比例缩放
     */
    public static final int TN5 = 5;

    private static final int[] SUPPORTED_TN = {SRC, TN1, TN5};

    /**
     * 获取下一个缩略图等级
     *
     * @param level
     * @return
     */
    public static int getSecondaryLevel(int level) {
        for (int i = SUPPORTED_TN.length - 1; i >= 0; i--) {
            if (level > SUPPORTED_TN[i]) {
                return SUPPORTED_TN[i];
            } else if (level == SUPPORTED_TN[i]) {
                return level;
            }
        }
        return SRC;
    }
}