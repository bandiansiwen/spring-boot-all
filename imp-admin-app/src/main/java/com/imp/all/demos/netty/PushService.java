package com.imp.all.demos.netty;

public interface PushService {
    /**
     * 推送给指定用户
     * @param uid
     * @param msg
     */
    void pushMsgToOne(String uid,String msg);

    /**
     * 推送给所有用户
     * @param msg
     */
    void pushMsgToAll(String msg);
}
