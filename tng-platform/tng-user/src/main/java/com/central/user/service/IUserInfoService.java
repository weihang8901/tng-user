package com.central.user.service;

import com.central.common.model.*;
import com.central.common.service.ISuperService;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

/**
 * 用户基础信息表
 *
 * @author wangweijun
 * @date 2020-04-09 14:21:39
 */
public interface IUserInfoService extends ISuperService<UserInfo> {

    /**
     * 获取UserDetails对象
     * @param username
     * @return
     */
    LoginAppUser findByUsername(String username);
    LoginAppUser findByOpenId(String username);
    LoginAppUser findByMobile(String username);

    /**
     * 通过UserInfo 转换为 LoginAppUser
     * @param userinfo
     * @return
     */
    LoginAppUser getLoginAppUser(UserInfo userinfo);


    /**
     * 列表
     * @param params
     * @return
     */
    PageResult<UserInfo> findList(Map<String, Object> params);

    /**
     * 新增/更新用户信息
     * @param userInfo
     * @return
     */
    Result saveOrUpdateUser(UserInfo userInfo);

    /**

     * 注册用户信息
     * @param userInfo
     * @return
     */
    Result register(UserInfo userInfo);

    /**
     * 通过id 获取用户实体
     * @param id
     * @return
     */
    UserInfo getUserById(Long id);

    /**
     * 通过用户名获取用户实体
     * @param username
     * @return
     */
    Result selectByUsername(String username);

    /**
     * 重置密码
     * @param username 用户名
     * @param oldPasswrd  旧密码
     * @param newPassword 新密码
     * @return
     */
    Result passwordReset(String username,String oldPasswrd,String newPassword);


    Result saveList(List<UserInfo> userInfo);

    /**
     * 解锁/锁定用户状态
     * @param userInfo
     * @return
     */
    Result lockUser(UserInfo userInfo);
}

