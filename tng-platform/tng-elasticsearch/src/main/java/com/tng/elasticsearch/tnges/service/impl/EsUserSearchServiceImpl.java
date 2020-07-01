package com.tng.elasticsearch.tnges.service.impl;

import com.central.common.model.CodeEnum;
import com.central.common.model.PageResult;
import com.tng.elasticsearch.tnges.document.UserDocument;
import com.tng.elasticsearch.tnges.repository.UserDocumentRepository;
import com.tng.elasticsearch.tnges.service.EsUserSearchService;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;


@Service
public class EsUserSearchServiceImpl extends BaseSearchServiceImpl<UserDocument> implements EsUserSearchService {

    private Logger log = LoggerFactory.getLogger(getClass());
    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;
    @Resource
    private UserDocumentRepository userDocumentRepository;

    public PageResult<UserDocument> searchPage(String indexName, String type, Integer pageNo, Integer pageSize, UserDocument userDocument) {
        /*组合查询BoolQueryBuilder
         * must(QueryBuilders)   :AND
         * mustNot(QueryBuilders):NOT
         * should:               :OR
         */
        PageResult<UserDocument> result = new PageResult<UserDocument>();
        try{
            BoolQueryBuilder builder = QueryBuilders.boolQuery();
            if (null != userDocument) {
                if(StringUtils.isNotBlank(userDocument.getUsername())){
                    builder.must(QueryBuilders.wildcardQuery("username", "*"+userDocument.getUsername()+"*"));//登录账号（手机号）
                }
                if(StringUtils.isNotBlank(userDocument.getTngNickName())){
                    builder.must(QueryBuilders.wildcardQuery ("tngNickName","*"+userDocument.getTngNickName()+"*"));//用户名
                }
                if(StringUtils.isNotBlank(userDocument.getTngUserName())){
                    builder.must(QueryBuilders.wildcardQuery("tngUserName", "*"+userDocument.getTngUserName()+"*"));//姓名
                }
                if(StringUtils.isNotBlank(userDocument.getTngUserPhone())){
                    builder.must(QueryBuilders.wildcardQuery("tngUserPhone", "*"+userDocument.getTngUserPhone()+"*"));//联系方式
                }
                if(StringUtils.isNotBlank(userDocument.getTngUserCredentials())){
                    builder.must(QueryBuilders.wildcardQuery ("tngUserCredentials","*"+userDocument.getTngUserCredentials()+"*"));//身份证号码
                }
            }
            //按照id降序排列
            Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
            //类似sql中的方法limit 的查询
            Pageable pageParam = PageRequest.of((pageNo - 1) * pageSize, pageNo * pageSize, sort);
            //2.构建查询
            NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
            //将搜索条件设置到构建中
            nativeSearchQueryBuilder.withQuery(builder);
            //将分页设置到构建中
            nativeSearchQueryBuilder.withPageable(pageParam);
            //生产NativeSearchQuery
            NativeSearchQuery query = nativeSearchQueryBuilder.build();
            //执行方法
            Page<UserDocument> page = elasticsearchTemplate.queryForPage(query, UserDocument.class);

            result.setCode(CodeEnum.SUCCESS.getCode());
            result.setCount(page.getTotalElements());
            result.setData(page.getContent());
        }catch (Exception e){
            log.error("查询用户列表异常",e);
            result.setCode(CodeEnum.ERROR.getCode());
            result.setData(new ArrayList<>());
        }
        return result;
    }
}
