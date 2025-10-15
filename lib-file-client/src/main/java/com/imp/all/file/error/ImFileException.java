package com.imp.all.file.error;

import cn.hutool.core.util.StrUtil;
import com.imp.all.file.type.FileCmdType;

/**
 * @author Longlin
 * @date 2024/2/4 15:50
 * @description
 */
public class ImFileException extends RuntimeException {

    private int sq;
    private FileCmdType cmdType;
    private int code;

    public ImFileException(int sq, FileCmdType cmdType, int code) {
        this(sq, cmdType, code, null);
    }

    public ImFileException(int sq, FileCmdType cmdType, int code, Throwable throwable) {
        super(throwable);
        this.sq = sq;
        this.cmdType = cmdType;
        this.code = code;
    }

    @Override
    public String getMessage() {
        String msg = !StrUtil.isEmpty(FileErrorMap.getMsg(code)) ? FileErrorMap.getMsg(code) : super.getMessage();
        return "sq: " + sq + ", cmdType:" + cmdType + ", code:" + code + ", msg:" + msg;
    }

    @Override
    public String getLocalizedMessage() {
        return FileErrorMap.getMsg(code);
    }
}

