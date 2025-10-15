package com.imp.all.framework.web.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.system.ApplicationHome;

import java.io.File;

/**
 * @author Longlin
 * @date 2022/10/14 14:54
 * @description
 */
@Slf4j
public class ImpPathUtil {

    /**
     * jar包所在路径
     */
    public static String getPath() {
        ApplicationHome appHome = new ApplicationHome(ImpPathUtil.class);
        File jarFile = appHome.getSource();
        String jarFileParentDir = jarFile.getParentFile().toString();
        log.info("jar包所在路径：{}", jarFileParentDir);
        return jarFileParentDir;
    }
}
