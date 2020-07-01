package com.central.common.model;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * 用户基础信息表
 *
 * @author wangweijun
 * @date 2020-04-09 14:21:39
 */
@Getter
@Setter
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tng_user")
public class UserInfo extends SuperEntity {
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private Boolean enabled;
    private String type;
    @TableLogic
    private boolean isDel;

    private String tngUserName;
    private String tngUserSex;
    private String tngUserPhone;
    private String tngIdType;
    private String tngUserCredentials;
    private String tngUserOpenid;
    private String tngUserMail;
    private String tngUserMicroblog;
    private String tngUserSource;
    private String tngUserEntryway;
    private String tngUserType;
    private String tngNickName;
    @JsonIgnore
    private String tngUser_2;
    private String tngUserRemark;
    @TableField(fill = FieldFill.INSERT)
    private Date tngUserSynctime;
    @JsonIgnore
    @TableField(exist = false)
    private String oldPassword;
    @JsonIgnore
    @TableField(exist = false)
    private String newPassword;

//    @TableField(exist = false)
//    private List<CategoryInfo> categorys;
}
