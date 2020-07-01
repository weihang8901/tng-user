package com.central.user.controller;

import com.central.common.annotation.LoginUser;
import com.central.common.model.*;
import com.central.user.model.UserDocument;
import com.central.user.service.IUserInfoService;
import com.central.user.service.SearchFeignService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 用户基础信息表
 *
 * @date 2020-04-09 14:21:39
 */
@Slf4j
@RestController
@RequestMapping("/userinfo")
@Api(tags = "用户基础信息表")
public class UserInfoController {
    @Autowired
    private IUserInfoService userInfoService;
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private SearchFeignService searchFeignService;

//    @Value("${server.port}")
//    private String port;

    /**
     * 查询用户登录对象LoginAppUser
     * @param username
     */
    @GetMapping(value = "/users-anon/login", params = "username")
    @ApiOperation(value = "根据用户名查询用户")
    public LoginAppUser findByUsername(String username) {
//        System.out.println("====findByUsername====当前tngUser的端口号："+port);
        return userInfoService.findByUsername(username);
    }
    /**
     * 根据OpenId查询用户信息
     * @param openId
     */
    @GetMapping(value = "/users-anon/openId", params = "openId")
    @ApiOperation(value = "根据OpenId查询用户")
    public LoginAppUser findByOpenId(String openId) {
        return userInfoService.findByOpenId(openId);
    }
    /**
     * 通过手机号查询用户、角色信息
     *
     * @param mobile 手机号
     */
    @GetMapping(value = "/users-anon/mobile", params = "mobile")
    @ApiOperation(value = "根据手机号查询用户")
    public LoginAppUser findByMobile(String mobile) {
        return userInfoService.findByMobile(mobile);
    }
    /**
     * 当前登录用户 LoginAppUser
     * @return
     */
    @ApiOperation(value = "根据access_token当前登录用户")
    @GetMapping("/user")
    public Result<LoginAppUser> getLoginAppUser(@LoginUser(isFull = true) UserInfo userInfo) {
        return Result.succeed(userInfoService.getLoginAppUser(userInfo));
    }


    /**
     * 通过用户名查询
     */
    @ApiOperation(value = "通过登录名查询用户信息")
    @GetMapping("/user/{username}")
    public Result selectByUsername(@PathVariable String username) {
        return userInfoService.selectByUsername(username);
//        res.setTngUserRemark(port); //edit by muqch
//        return res;
    }


    /**
     * 列表
     */
    @ApiOperation(value = "查询列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "分页起始位置", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "limit", value = "分页结束位置", required = true, dataType = "Integer")
    })
    @GetMapping
    public PageResult list(@RequestParam Map<String, Object> params) {
        return userInfoService.findList(params);
    }

    /**
     * @deprecated   es调用列表信息
     * @param params
     * @return
     */
    @ApiOperation(value = "查询列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "分页起始位置", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "limit", value = "分页结束位置", required = true, dataType = "Integer")
    })
    @GetMapping("/lists")
    public PageResult getList(@RequestParam Map<String, Object> params) {
        Integer pageNo = 1;
        Integer pageSize = 10;
        pageNo = MapUtils.getInteger(params, "page");
        pageSize = MapUtils.getInteger(params, "limit");
        UserDocument userDocument = new UserDocument();
        String username = MapUtils.getString(params,"username");
        if(StringUtils.isNotEmpty(username)){
            userDocument.setUsername(username);
        }
        String tngUserName = MapUtils.getString(params,"tngUserName");
        if(StringUtils.isNotEmpty(tngUserName)){
            userDocument.setTngUserName(tngUserName);
        }
        String tngUserPhone = MapUtils.getString(params,"tngUserPhone");
        if(StringUtils.isNotEmpty(tngUserPhone)){
            userDocument.setTngUserPhone(tngUserPhone);
        }
        String tngNickName = MapUtils.getString(params,"tngNickName");;
        if(StringUtils.isNotEmpty(tngNickName)){
            userDocument.setTngNickName(tngNickName);
        }
        String tngUserCredentials = MapUtils.getString(params,"tngUserCredentials");;
        if(StringUtils.isNotEmpty(tngUserCredentials)){
            userDocument.setTngUserCredentials(tngUserCredentials);
        }
        return searchFeignService.searchUserPage(pageNo,pageSize,userDocument);
    }


    /**
     * 通过ID查询
     */
    @ApiOperation(value = "查询")
    @GetMapping("/{id}")
    public Result findUserById(@PathVariable Long id) {
        UserInfo model = userInfoService.getUserById(id);
        return Result.succeed(model, "查询成功");
    }

    /**
     * 新增or更新
     */
    @ApiOperation(value = "保存")
    @PostMapping("/updateUserInfo")
    public Result save(@RequestBody UserInfo userInfo) {
//        Result result = (Result) amqpTemplate.convertSendAndReceive(RabbitMqConstant.QUEUE_SAVE_NAME, userInfo);
//        return result;
        return userInfoService.saveOrUpdateUser(userInfo);
    }

    /**
     * 注册
     */
    @ApiOperation(value = "注册")
    @PostMapping("register")
    public Result register(@RequestBody UserInfo userInfo) {
        return userInfoService.register(userInfo);
    }
    /**
     * 重置密码
     */
    @ApiOperation(value = "重置密码")
    @PutMapping("/passwordReset")
    public Result passwordReset(@RequestBody UserInfo userInfo) {
        return userInfoService.passwordReset(userInfo.getUsername(),userInfo.getOldPassword(),userInfo.getNewPassword());
    }
    /**
     * 批量增加
     */
    @ApiOperation(value = "批量增加")
    @PostMapping("/saveList")
    public Result saveList(@RequestBody List<UserInfo> userInfo){
        return userInfoService.saveList(userInfo);
    }

    /**
     * 锁定/解锁用户
     * @param userInfo
     * @return
     */
    @ApiOperation(value = "锁定/解锁用户")
    @PutMapping("/lockUser")
    public Result lockUser(@RequestBody UserInfo userInfo) {
        return userInfoService.lockUser(userInfo);
    }
}
