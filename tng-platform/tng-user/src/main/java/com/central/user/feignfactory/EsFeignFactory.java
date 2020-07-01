package com.central.user.feignfactory;

import com.central.common.model.PageResult;
import com.central.user.model.UserDocument;
import com.central.user.service.SearchFeignService;
import feign.hystrix.FallbackFactory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: client-sso-demo
 * @description: es服务降级工厂
 * @author: Jue
 * @create: 2020-06-15 10:36
 **/
@Slf4j
@Component
public class EsFeignFactory implements FallbackFactory<SearchFeignService> {
    @Override
    public SearchFeignService create(Throwable throwable){
        return new SearchFeignService() {
            @Override
            public PageResult<UserDocument> searchUserPage(Integer pageNo, Integer pageSize, UserDocument userDocument) {
                return new PageResult<UserDocument>();
            }
        };
    }
}
