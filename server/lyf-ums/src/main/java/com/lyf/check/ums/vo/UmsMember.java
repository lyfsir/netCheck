package com.lyf.check.ums.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author lyf
 * @date 2020/6/23-1:10
 */
@Data
public class UmsMember {
    private Integer id;
    /**
     * 用户名
     */
    @NotNull
    private String username;
    /**
     * 密码
     */
    @NotNull
    private String password;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 手机号码
     */
    @NotNull
    private String mobile;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 头像
     */
    private String header;
    /**
     * 性别
     */
    private Integer gender;
    /**
     * 生日
     */
    private Date birth;
    /**
     * 所在城市
     */
    private String city;
    /**
     * 职业
     */
    private String job;
    /**
     * 个性签名
     */
    private String sign;
    /**
     * 用户来源
     */
    private String sourceType;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 注册时间
     */
    private Date createTime;
}
