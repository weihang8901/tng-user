package com.central.user.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

/**
 * @program: client-sso-demo
 * @description: 用户信息获取
 * @author: Jue
 * @create: 2020-06-15 09:28
 **/
@Data
public class UserDocument implements Serializable {

    @Id
    private String id;

    private String username;
    private String password;
    private String enabled;
    private String type;
    private String isDel;
    private String tngIdType;
    private String tngUserName;
    private String tngUserSex;
    private String tngUserPhone;
    private String tngUserCredentials;
    private String tngUserOpenid;
    private String tngUserMail;
    private String tngUserMicroblog;
    private String tngUserSource;
    private String tngUserEntryway;
    private String tngUserType;
    private String tngNickName;
    private String tngUser_2;
    private String tngUserRemark;
    private Date tngUserSynctime;
    private Date createTime;
    private Date update_time;

}
