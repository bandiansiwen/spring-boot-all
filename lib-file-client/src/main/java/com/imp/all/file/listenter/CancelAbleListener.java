package com.imp.all.file.listenter;

/**
 * 可取消的Listener
 *
 * @author liangyj3
 */
public class CancelAbleListener {

    private boolean cancel;

    /**
     * 是否可以取消
     *
     * @return 布尔值
     */
    public boolean isCancel() {
        return cancel;
    }

    /**
     * 设置是否可以取消
     *
     * @param cancel 布尔值
     */
    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }
}
