package com.imp.all.demos.algorithm.mysql;

/**
 * @author Daniel
 *
 * 将IP地址 m.n.x.y 按以下公式计算：
 * Long值 = m×256³ + n×256² + x×256 + y
 * 或通过位运算实现：
 * Long值 = (m << 24) | (n << 16) | (x << 8) | y
 */
public class MysqlRef {

    // IP转Long算法
    public static long ipToLong(String ipAddress) {
        String[] parts = ipAddress.split("\\.");
        long result = 0;
        for (int i = 0; i < 4; i++) {
            int part = Integer.parseInt(parts[i]);
            result |= (part & 0xFFL) << (24 - (8 * i));
        }
        return result;
    }

    // IP转Long
    public static long ipToLong2(String ip) {
        String[] parts = ip.split("\\.");
        return (Long.parseLong(parts[0]) << 24) |
                (Long.parseLong(parts[1]) << 16) |
                (Long.parseLong(parts[2]) << 8)  |
                Long.parseLong(parts[3]);
    }

    // Long转IP算法
    public static String longToIp(long ip) {
        return ((ip >> 24) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                (ip & 0xFF);
    }

    public static void main(String[] args) {
        long l = ipToLong("192.168.1.1");
        System.out.println(l);
        String s = longToIp(l);
        System.out.println(s);

    }
}
