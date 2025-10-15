package com.imp.all.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.imp.all.framework.common.validation.calibrator.CaseMode;
import com.imp.all.framework.common.validation.calibrator.CheckCase;
import com.imp.all.framework.web.log.annotation.HiddenField;
import com.imp.all.framework.web.log.annotation.HiddenMode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

/**
 * @author Longlin
 * @date 2021/3/16 23:50
 * @description 注解	                校验功能
 * -            @AssertFalse        必须是false
 * -            @AssertTrue         必须是true
 * -            @DecimalMax         小于等于给定的值
 * -            @DecimalMin         大于等于给定的值
 * -            @Digits             可设定最大整数位数和最大小数位数
 * -            @Email              校验是否符合Email格式
 * -            @Future             必须是将来的时间
 * -            @FutureOrPresent    当前或将来时间
 * -            @Max                最大值
 * -            @Min                最小值
 * -            @Negative           负数（不包括0）
 * -            @NegativeOrZero     负数或0
 * -            @NotBlank           不为null并且包含至少一个非空白字符
 * -            @NotEmpty           不为null并且不为空
 * -            @NotNull            不为null
 * -            @Null               为null
 * -            @Past               必须是过去的时间
 * -            @PastOrPresent      必须是过去的时间，包含现在
 * -            @PositiveOrZero     正数或0
 * -            @Size               校验容器的元素个数
 */
@Setter
@Getter
@TableName("tb_sys_user")
public class AdminUser {

    private Long id;
    @NotEmpty(message="用户名不能为空")
    @CheckCase(value = CaseMode.LOWER, message = "大小写不正确")
    private String username;
    @NotEmpty(message = "密码不能为空")
    @HiddenField
    private String password;
    private String icon;
    private String phone;
    @HiddenField(type = HiddenMode.EMAIL, endIndex = "3")
    private String email;
    @HiddenField(type = HiddenMode.ID_CARD)
    private String idCard;
    @HiddenField(type = HiddenMode.BANK_CARD)
    private String bankCard;
    private String nickName;
    private String note;
    private Date createTime;
    private Date loginTime;
    private Integer status;
}
