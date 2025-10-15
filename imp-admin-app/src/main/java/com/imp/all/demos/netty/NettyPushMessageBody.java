package com.imp.all.demos.netty;

import lombok.Data;

import java.io.Serializable;

@Data
public class NettyPushMessageBody implements Serializable {

    private static final long serialVersionUID = 1L;

    private String uid;
    private String message;
}
