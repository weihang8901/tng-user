package com.tng.elasticsearch.tnges.document;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.joda.time.format.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Document(indexName = "#{configBean.indexName}", type = "doc")
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIsDel() {
        return isDel;
    }

    public void setIsDel(String isDel) {
        this.isDel = isDel;
    }

    public String getTngIdType() {
        return tngIdType;
    }

    public void setTngIdType(String tngIdType) {
        this.tngIdType = tngIdType;
    }

    public String getTngUserName() {
        return tngUserName;
    }

    public void setTngUserName(String tngUserName) {
        this.tngUserName = tngUserName;
    }

    public String getTngUserSex() {
        return tngUserSex;
    }

    public void setTngUserSex(String tngUserSex) {
        this.tngUserSex = tngUserSex;
    }

    public String getTngUserPhone() {
        return tngUserPhone;
    }

    public void setTngUserPhone(String tngUserPhone) {
        this.tngUserPhone = tngUserPhone;
    }

    public String getTngUserCredentials() {
        return tngUserCredentials;
    }

    public void setTngUserCredentials(String tngUserCredentials) {
        this.tngUserCredentials = tngUserCredentials;
    }

    public String getTngUserOpenid() {
        return tngUserOpenid;
    }

    public void setTngUserOpenid(String tngUserOpenid) {
        this.tngUserOpenid = tngUserOpenid;
    }

    public String getTngUserMail() {
        return tngUserMail;
    }

    public void setTngUserMail(String tngUserMail) {
        this.tngUserMail = tngUserMail;
    }

    public String getTngUserMicroblog() {
        return tngUserMicroblog;
    }

    public void setTngUserMicroblog(String tngUserMicroblog) {
        this.tngUserMicroblog = tngUserMicroblog;
    }

    public String getTngUserSource() {
        return tngUserSource;
    }

    public void setTngUserSource(String tngUserSource) {
        this.tngUserSource = tngUserSource;
    }

    public String getTngUserEntryway() {
        return tngUserEntryway;
    }

    public void setTngUserEntryway(String tngUserEntryway) {
        this.tngUserEntryway = tngUserEntryway;
    }

    public String getTngUserType() {
        return tngUserType;
    }

    public void setTngUserType(String tngUserType) {
        this.tngUserType = tngUserType;
    }

    public String getTngNickName() {
        return tngNickName;
    }

    public void setTngNickName(String tngNickName) {
        this.tngNickName = tngNickName;
    }

    public String getTngUser_2() {
        return tngUser_2;
    }

    public void setTngUser_2(String tngUser_2) {
        this.tngUser_2 = tngUser_2;
    }

    public String getTngUserRemark() {
        return tngUserRemark;
    }

    public void setTngUserRemark(String tngUserRemark) {
        this.tngUserRemark = tngUserRemark;
    }

    public Date getTngUserSynctime() {
        return tngUserSynctime;
    }

    public void setTngUserSynctime(Date tngUserSynctime) {
        this.tngUserSynctime = tngUserSynctime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }
}
