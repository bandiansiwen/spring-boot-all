package com.imp.all.file.request;

/**
 * 客户端仅用到 IM , 文件服务后台用于区分来源
 */
public enum From {
    IM,
    RED_IM,
    MPM,
    MAM,
    IMM,
    MUC,
    CONTACT
}
