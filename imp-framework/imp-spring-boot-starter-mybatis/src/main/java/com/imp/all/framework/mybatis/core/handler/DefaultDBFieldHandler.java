package com.imp.all.framework.mybatis.core.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.imp.all.framework.mybatis.core.dataobject.BaseDO;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author Longlin
 * @date 2022/12/20 16:08
 * @description
 */
public class DefaultDBFieldHandler implements MetaObjectHandler {

    @Autowired(required = false)
    private DBFieldInterface dbFieldInterface;

    @Override
    public void insertFill(MetaObject metaObject) {
        if (Objects.nonNull(metaObject) && metaObject.getOriginalObject() instanceof BaseDO) {
            BaseDO baseDO = (BaseDO) metaObject.getOriginalObject();

            LocalDateTime current = LocalDateTime.now();
            // 创建时间为空，则以当前时间为插入时间
            if (Objects.isNull(baseDO.getCreateTime())) {
                baseDO.setCreateTime(current);
            }
            // 更新时间为空，则以当前时间为更新时间
            if (Objects.isNull(baseDO.getUpdateTime())) {
                baseDO.setUpdateTime(current);
            }

            if (dbFieldInterface != null) {
                String userId = dbFieldInterface.getUserId();
                // 当前登录用户不为空，创建人为空，则当前登录用户为创建人
                if (Objects.nonNull(userId) && Objects.isNull(baseDO.getCreator())) {
                    baseDO.setCreator(userId);
                }
                // 当前登录用户不为空，更新人为空，则当前登录用户为更新人
                if (Objects.nonNull(userId) && Objects.isNull(baseDO.getUpdater())) {
                    baseDO.setUpdater(userId);
                }
            }
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 更新时间为空，则以当前时间为更新时间
        Object modifyTime = getFieldValByName("updateTime", metaObject);
        if (Objects.isNull(modifyTime)) {
            setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        }

        if (dbFieldInterface != null) {
            String userId = dbFieldInterface.getUserId();
            // 当前登录用户不为空，更新人为空，则当前登录用户为更新人
            Object modifier = getFieldValByName("updater", metaObject);
            if (Objects.nonNull(userId) && Objects.isNull(modifier)) {
                setFieldValByName("updater", userId, metaObject);
            }
        }
    }
}
