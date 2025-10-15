package com.imp.all.dao;

import com.imp.all.entity.AdminUser;
import com.imp.all.framework.mybatis.core.mapper.BaseMapperX;
import com.imp.all.multiDatasource.DBTypeEnum;
import com.imp.all.multiDatasource.DSRoute;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Longlin
 * @date 2021/3/16 23:57
 * @description
 */
@Mapper
public interface UserMapper extends BaseMapperX<AdminUser> {

    AdminUser getUserByName(String name);

    int updateUserNickName(String userName, String nickName);

    int insertUser(String username, String password, String username2, String password2);

    @DSRoute(DBTypeEnum.MASTER)
    List<AdminUser> getAllUsers();
}
