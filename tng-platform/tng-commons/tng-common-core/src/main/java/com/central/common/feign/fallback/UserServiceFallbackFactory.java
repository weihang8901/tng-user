package com.central.common.feign.fallback;

import com.central.common.feign.UserService;
import com.central.common.model.LoginAppUser;
import com.central.common.model.Result;
import com.central.common.model.UserInfo;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * userService降级工场
 *
 * @author zlt
 * @date 2019/1/18
 */
@Slf4j
public class UserServiceFallbackFactory implements FallbackFactory<UserService> {
    @Override
    public UserService create(Throwable throwable) {
        return new UserService() {

            @Override
            public UserInfo selectByUsername(String username) {
                log.error("通过登录名查询用户异常:{}", username, throwable);
                return new UserInfo();
            }

            @Override
            public LoginAppUser findByUsername(String username) {
                log.error("通过用户名查询用户异常:{}", username, throwable);
                return new LoginAppUser();
            }

            @Override
            public LoginAppUser findByMobile(String mobile) {
                log.error("通过手机号查询用户异常:{}", mobile, throwable);
                return new LoginAppUser();
            }

            @Override
            public LoginAppUser findByOpenId(String openId) {
                log.error("通过openId查询用户异常:{}", openId, throwable);
                return new LoginAppUser();
            }

            @Override
            public Result saveOrUpdateUser(UserInfo userInfo){
                log.error("通过openId查询用户异常:{}", userInfo, throwable);
                return new Result();
            }
        };
    }
}
