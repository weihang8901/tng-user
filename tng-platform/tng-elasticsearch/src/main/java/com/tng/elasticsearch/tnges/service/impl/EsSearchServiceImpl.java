package com.tng.elasticsearch.tnges.service.impl;

import com.alibaba.fastjson.JSON;
import com.tng.elasticsearch.tnges.page.Page;
import com.tng.elasticsearch.tnges.service.EsSearchService;
import com.tng.elasticsearch.tnges.document.UserDocument;
import com.tng.elasticsearch.tnges.repository.UserDocumentRepository;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;


@Service
public class EsSearchServiceImpl extends BaseSearchServiceImpl<UserDocument> implements EsSearchService {
    private Logger log = LoggerFactory.getLogger(getClass());
    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;
    @Resource
    private UserDocumentRepository productDocumentRepository;

    @Override
    public void save(UserDocument... productDocuments) {
        elasticsearchTemplate.putMapping(UserDocument.class);
        if(productDocuments.length > 0){
            /*Arrays.asList(productDocuments).parallelStream()
                    .map(productDocumentRepository::save)
                    .forEach(productDocument -> log.info("【保存数据】：{}", JSON.toJSONString(productDocument)));*/
            log.info("【保存索引】：{}",JSON.toJSONString(productDocumentRepository.saveAll(Arrays.asList(productDocuments))));
        }
    }

    @Override
    public void delete(String id) {
        productDocumentRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        productDocumentRepository.deleteAll();
    }

    @Override
    public UserDocument getById(String id) {
        return productDocumentRepository.findById(id).get();
    }

    @Override
    public List<UserDocument> getAll() {
        List<UserDocument> list = new ArrayList<>();
        productDocumentRepository.findAll().forEach(list::add);
        elasticSerchTest();
        return list;
    }

    /**
     * 使用分词查询,并分页
     *
     * @param index 索引名称
     * @param type 类型名称,可传入多个type逗号分隔
     * @param pageNo 当前页
     * @param pageSize 每页显示条数
     * @param query 查询条件
     * @param fields 需要显示的字段，逗号分隔（缺省为全部字段）
     * @param sortField 排序字段
     * @param highlightField 高亮字段
     * @return
     */
    public Page<Map<String, Object>> searchDataPage(String index, String type, int pageNo, int pageSize, QueryBuilder query, String fields, String sortField, String highlightField) {
        SearchRequestBuilder searchRequestBuilder = elasticsearchTemplate.getClient().prepareSearch(index);
        if (StringUtils.isNotEmpty(type)) {
            searchRequestBuilder.setTypes(type.split(","));
        }
        searchRequestBuilder.setSearchType(SearchType.QUERY_THEN_FETCH);

        // 需要显示的字段，逗号分隔（缺省为全部字段）
        if (StringUtils.isNotEmpty(fields)) {
            searchRequestBuilder.setFetchSource(fields.split(","), null);
        }

        //排序字段
        if (StringUtils.isNotEmpty(sortField)) {
            searchRequestBuilder.addSort(sortField, SortOrder.DESC);
        }

        if (StringUtils.isNotEmpty(highlightField)) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();

            highlightBuilder.preTags("<span style='color:red' >");//设置前缀
            highlightBuilder.postTags("</span>");//设置后缀

            // 设置高亮字段
            highlightBuilder.field(highlightField);
            searchRequestBuilder.highlighter(highlightBuilder);
        }

        searchRequestBuilder.setQuery(QueryBuilders.matchAllQuery());//查询全部
        //searchRequestBuilder.setQuery(query);//条件查询

        // 分页应用
        searchRequestBuilder.setFrom((pageNo-1) * pageSize)
                .setSize(pageNo * pageSize);

        // 设置是否按查询匹配度排序
        searchRequestBuilder.setExplain(true);

        // 执行搜索,返回搜索响应信息
        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();

        long totalHits = searchResponse.getHits().totalHits;
        long length = searchResponse.getHits().getHits().length;

        log.debug("共查询到[{}]条数据,处理数据条数[{}]", totalHits, length);

        if (searchResponse.status().getStatus() == 200) {
        // 解析对象
            List<Map<String, Object>> sourceList = setSearchResponse(searchResponse, highlightField);
            System.out.println(Arrays.asList(sourceList));

            Page<Map<String, Object>> page = new Page<>(pageNo,pageSize,(int) totalHits);
            page.setList(sourceList);

            return page;
        }
        return null;

    }

    /**
     * 使用分词查询
     *
     * @param index 索引名称
     * @param type 类型名称,可传入多个type逗号分隔
     * @param query 查询条件
     * @param size 文档大小限制
     * @param fields 需要显示的字段，逗号分隔（缺省为全部字段）
     * @param sortField 排序字段
     * @param highlightField 高亮字段
     * @return
     */
    public List<Map<String, Object>> searchListData(String index, String type, QueryBuilder query, Integer size, String fields, String sortField, String highlightField) {

        SearchRequestBuilder searchRequestBuilder = elasticsearchTemplate.getClient().prepareSearch(index);
        if (StringUtils.isNotEmpty(type)) {
            searchRequestBuilder.setTypes(type.split(","));
        }

        if (StringUtils.isNotEmpty(highlightField)) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();
        // 设置高亮字段
            highlightBuilder.field(highlightField);
            searchRequestBuilder.highlighter(highlightBuilder);
        }

        searchRequestBuilder.setQuery(query);

        if (StringUtils.isNotEmpty(fields)) {
            searchRequestBuilder.setFetchSource(fields.split(","), null);
        }
        searchRequestBuilder.setFetchSource(true);

        if (StringUtils.isNotEmpty(sortField)) {
            searchRequestBuilder.addSort(sortField, SortOrder.DESC);
        }

        if (size != null && size > 0) {
            searchRequestBuilder.setSize(size);
        }

        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();

        long totalHits = searchResponse.getHits().totalHits;
        long length = searchResponse.getHits().getHits().length;

        System.out.println(totalHits);
        System.out.println(length);

        if (searchResponse.status().getStatus() == 200) {
            // 解析对象
            return setSearchResponse(searchResponse, highlightField);
        }
        return null;

    }


    /**
     * 高亮结果集 特殊处理
     *
     * @param searchResponse
     * @param highlightField
     */
    private static List<Map<String, Object>> setSearchResponse(SearchResponse searchResponse, String highlightField) {
        List<Map<String, Object>> sourceList = new ArrayList<Map<String, Object>>();
        StringBuffer stringBuffer = new StringBuffer();

        for (SearchHit searchHit : searchResponse.getHits().getHits()) {
            searchHit.getSourceAsMap().put("id", searchHit.getId());

            if (StringUtils.isNotEmpty(highlightField)) {

                Text[] text = searchHit.getHighlightFields().get(highlightField).getFragments();

                if (text != null) {
                    for (Text str : text) {
                        stringBuffer.append(str.string());
                    }
                    //遍历 高亮结果集，覆盖 正常结果集
                    searchHit.getSourceAsMap().put(highlightField, stringBuffer.toString());
                }
            }
            sourceList.add(searchHit.getSourceAsMap());
        }

        return sourceList;
    }


    public List<UserDocument> elasticSerchTest() {
        //1.创建QueryBuilder(即设置查询条件)这儿创建的是组合查询(也叫多条件查询),后面会介绍更多的查询方法
        /*组合查询BoolQueryBuilder
         * must(QueryBuilders)   :AND
         * mustNot(QueryBuilders):NOT
         * should:               :OR
         */
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        //builder下有must、should以及mustNot 相当于sql中的and、or以及not

        //设置模糊搜索,博客的简诉中有学习两个字
        //builder.must(QueryBuilders.fuzzyQuery("tngUserName", "weihang"));

        //设置要查询博客的标题中含有关键字
        //builder.must(new QueryStringQueryBuilder("tngAccountName").field("15389018967"));
        //builder.must(QueryBuilders.fuzzyQuery("tngAccountName","15389018967"));
        builder.must(QueryBuilders.wildcardQuery ("tngAccountName","*1538901*"));

        //按照博客的评论数的排序是依次降低
        FieldSortBuilder sort = SortBuilders.fieldSort("id").order(SortOrder.DESC);

        //设置分页(从第一页开始，一页显示10条)
        //注意开始是从0开始，有点类似sql中的方法limit 的查询
        PageRequest page = new PageRequest(0, 10);

        //2.构建查询
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        //将搜索条件设置到构建中
        nativeSearchQueryBuilder.withQuery(builder);
        //将分页设置到构建中
        //nativeSearchQueryBuilder.withPageable(page);
        //将排序设置到构建中
        nativeSearchQueryBuilder.withSort(sort);
        //生产NativeSearchQuery
        NativeSearchQuery query = nativeSearchQueryBuilder.build();

        //3.执行方法1
        //Page<UserDocument> pagexx = productDocumentRepository .search(query);
        //执行方法2：注意，这儿执行的时候还有个方法那就是使用elasticsearchTemplate
        //执行方法2的时候需要加上注解
        //@Autowired
        //private ElasticsearchTemplate elasticsearchTemplate;
        List<UserDocument> blogList = elasticsearchTemplate.queryForList(query, UserDocument.class);

        return null;
    }
}
