package com.tng.elasticsearch.tnges.controller;

import com.central.common.model.PageResult;
import com.tng.elasticsearch.tnges.document.UserDocument;
import com.tng.elasticsearch.tnges.service.EsUserSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/esearch")
public class EsUserController {
    private Logger log = LoggerFactory.getLogger(EsUserController.class);

    @Resource
    private EsUserSearchService esUserSearchService;

    @RequestMapping("search_userPage")
    public PageResult<UserDocument> searchUserPage(@RequestParam Integer pageNo, @RequestParam Integer pageSize , @RequestBody UserDocument userDocument, HttpServletRequest request, HttpServletResponse response){

        if(null == pageNo){
            pageNo = 1;
        }
        if(null == pageSize){
            pageSize = 10;
        }
        return esUserSearchService.searchPage("", "",pageNo, pageSize,userDocument);
    }
}
