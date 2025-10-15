package com.imp.all.file.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Longlin
 * @date 2024/3/7 14:36
 * @description
 */
public class FileRes {

    @Setter
    @Getter
    private boolean success;

    @Setter
    @Getter
    private String fileKey;

    @Setter
    @Getter
    private String message;

    protected FileRes(boolean suc, String fk, String msg) {
        this.success = suc;
        this.fileKey = fk;
        this.message = msg;
    }

    public static FileRes success(String fk) {
        return new FileRes(true, fk, null);
    }

    public static FileRes failed(String fk, String msg) {
        return new FileRes(false, fk, msg);
    }
}
