package com.central.user.service;

import com.central.common.model.PageResult;
import com.central.user.feignfactory.EsFeignFactory;
import com.central.user.model.UserDocument;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: client-sso-demo
 * @description: es服务调用
 * @author: Jue
 * @create: 2020-06-15 09:23
 **/
@FeignClient(value = "tng-esearch",fallbackFactory = EsFeignFactory.class)
public interface SearchFeignService {


    /**
     *
     * @param pageNo
     * @param pageSize
     * @param userDocument
     * @return
     */
    @GetMapping(value = "/esearch/search_userPage")
    PageResult<UserDocument> searchUserPage(@RequestParam(value = "pageNo",required = false) Integer pageNo, @RequestParam(value = "pageSize",required = false) Integer pageSize , @RequestBody(required = false) UserDocument userDocument);
}
