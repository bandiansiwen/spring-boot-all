package com.imp.all.entity;


import com.imp.all.framework.web.log.annotation.HiddenField;
import com.imp.all.framework.web.log.annotation.HiddenMode;
import lombok.Data;

/**
 * @author Longlin
 * @date 2022/8/29 15:46
 * @description
 */
@Data
public class Teacher {

    private String username;
    @HiddenField
    private String password;
    @HiddenField(type = HiddenMode.PHONE)
    private String phone;
    @HiddenField(type = HiddenMode.EMAIL)
    private String email;
    @HiddenField(type = HiddenMode.ID_CARD)
    private String idCard;
    @HiddenField(type = HiddenMode.BANK_CARD)
    private String bankCard;
    private Integer age;

}
