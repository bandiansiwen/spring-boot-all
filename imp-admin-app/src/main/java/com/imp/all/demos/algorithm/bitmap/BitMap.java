package com.imp.all.demos.algorithm.bitmap;

/**
 * @author Longlin
 * @date 2023/1/31 16:29
 * @description 位图法
 */
public class BitMap {
    /**
     * 位图提供的最大长度,
     * 比如unsigned int的最大值为4294967295,
     * 则需要的length为4294967296
     */
    private long length;

    /**
     * 位图桶
     */
    private static int[] bitmapBucket;

    /**
     * int用来表示32位二进制数,
     * BIT_VALUE[0]表示第1个二进制数存在、
     * BIT_VALUE[1]表示第2个二进制数存在，以此类推
     * <p>
     * BIT_VALUE[0] = 00000000 00000000 00000000 00000001
     * BIT_VALUE[1] = 00000000 00000000 00000000 00000010
     * BIT_VALUE[2] = 00000000 00000000 00000000 00000100
     * ...
     * BIT_VALUE[31] = 10000000 00000000 00000000 00000000
     */
    private static final int[] BIT_VALUE = {
            0x00000001, 0x00000002, 0x00000004, 0x00000008,
            0x00000010, 0x00000020, 0x00000040, 0x00000080,
            0x00000100, 0x00000200, 0x00000400, 0x00000800,
            0x00001000, 0x00002000, 0x00004000, 0x00008000,
            0x00010000, 0x00020000, 0x00040000, 0x00080000,
            0x00100000, 0x00200000, 0x00400000, 0x00800000,
            0x01000000, 0x02000000, 0x04000000, 0x08000000,
            0x10000000, 0x20000000, 0x40000000, 0x80000000};

    /**
     * length为1 - 32: 需要1个桶
     * length为33 - 64: 需要2个桶
     * ...
     * 以此类推
     *
     * @param length
     */
    public BitMap(long length) {
        this.length = length;
        // 根据长度算出，所需位图桶个数
        bitmapBucket = new int[(int) (length >> 5) +
                ((length & 31) > 0 ? 1 : 0)];
    }

    /**
     * 查找number是否存在于位图桶中
     *
     * @param number 要查询的数字
     * @return true: number在位图桶中, 否则false
     */
    public boolean getBit(long number) {
        if (number < 0 || number > length) {
            throw new IllegalArgumentException("非法参数");
        }
        // 计算该number在哪个桶
        int belowIndex = (int) (number >> 5);
        // 求出该number在桶里的下标（下标0 - 31）
        int offset = (int) (number & 31);
        // 拿到该桶的值
        int currentValue = bitmapBucket[belowIndex];
        // 计算该number是否存在
        return ((currentValue & BIT_VALUE[offset])) == 0 ? false : true;
    }

    /**
     * 将number在位图桶中标记为存在
     *
     * @param number 要标记的数字
     */
    public void setBit(long number) {
        // 合法性校验
        if (number < 0 || number >= length) {
            throw new IllegalArgumentException("非法参数");
        }
        // 计算该number在哪个桶 2^5=32
        int belowIndex = (int) (number >> 5);
        // 求出该number在桶里的下标（下标0 - 31）
        int offset = (int) (number & 31);
        // 拿到该桶的当前值
        int currentValue = bitmapBucket[belowIndex];
        // 将number在桶里标记
        bitmapBucket[belowIndex] =
                currentValue | BIT_VALUE[offset];
    }

    public static void main(String[] args) {
        BitMap bitMap = new BitMap(4294967296L);

        bitMap.setBit(295L);
        System.out.println(bitMap.getBit(295L));
        System.out.println(bitMap.getBit(294L));

        bitMap.setBit(4294967295L);
        System.out.println(bitMap.getBit(4294967295L));
        System.out.println(bitMap.getBit(4294967294L));
    }
}
