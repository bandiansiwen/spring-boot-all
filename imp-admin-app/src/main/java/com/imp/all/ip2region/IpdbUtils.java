package com.imp.all.ip2region;

import cn.hutool.core.io.IoUtil;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;

/**
 * @author Longlin
 * @date 2024/8/25 17:24
 * @description
 */
@Slf4j
public class IpdbUtils {
    private static Searcher searcher;

    /**
     *
     *   解决打包jar后找不到 ip2region.db 的问题
     */
    public static String getIpAddress(String ip){
        if ("127.0.0.1".equals(ip) || ip.startsWith("192.168")) {
            return "局域网";
        }
        if (searcher == null) {
            try {
                ClassPathResource resource = new ClassPathResource("data/ip2region.xdb");
                InputStream inputStream = resource.getInputStream();
                byte[] bytes = IoUtil.readBytes(inputStream);
                searcher = Searcher.newWithBuffer(bytes);
            } catch (Exception e) {
                log.error("failed to create content cached searcher: ", e);
                return null;
            }
        }
        // 3、查询
        String region=null;
        try {
            region = searcher.search(ip);
        } catch (Exception e) {
            throw new RuntimeException("获取IP地址异常");
        }
        return region;
    }

    public static void main(String[] args) {
        String ipAddress = getIpAddress("1.2.3.4");
        System.out.println(ipAddress);
    }
}
