package com.tng.elasticsearch.tnges.controller;

import com.tng.elasticsearch.tnges.document.UserDocument;
import com.tng.elasticsearch.tnges.service.EsSearchService;
import com.tng.elasticsearch.tnges.page.Page;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
public class EsSearchController {
    private Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private EsSearchService esSearchService;

    /**
     * 新增 / 修改索引
     * @return
     */
    @RequestMapping("save")
    public String add(@RequestBody UserDocument productDocument) {
        esSearchService.save(productDocument);
        return "success";
    }

    /**
     * 删除索引
     * @return
     */
    @RequestMapping("delete/{id}")
    public String delete(@PathVariable String id) {
        esSearchService.delete(id);
        return "success";
    }
    /**
     * 清空索引
     * @return
     */
    @RequestMapping("delete_all")
    public String deleteAll(@PathVariable String id) {
        esSearchService.deleteAll();
        return "success";
    }

    /**
     * 根据ID获取
     * @return
     */
    @RequestMapping("get/{id}")
    public UserDocument getById(@PathVariable String id){
        return esSearchService.getById(id);
    }

    /**
     * 根据获取全部
     * @return
     */
    @RequestMapping("get_all")
    public List<UserDocument> getAll(){
        return esSearchService.getAll();
    }

    /**
     * 搜索
     * @param keyword
     * @return
     */
    @RequestMapping("query/{keyword}")
    public List<UserDocument> query(@PathVariable String keyword){
        return esSearchService.query(keyword, UserDocument.class);
    }

    /**
     * 搜索，命中关键字高亮
     * @param keyword   关键字
     * @param indexName 索引库名称
     * @param fields    搜索字段名称，多个以“，”分割
     * @return
     */
    @RequestMapping("query_hit")
    public List<Map<String,Object>> queryHit(@RequestParam String keyword, @RequestParam String indexName, @RequestParam String fields){
        String[] fieldNames = {};
        if(fields.contains(",")) fieldNames = fields.split(",");
        else fieldNames[0] = fields;
        return esSearchService.queryHit(keyword,indexName,fieldNames);
    }

    /**
     * 搜索，命中关键字高亮
     * @param pageNo    当前页
     * @param pageSize  每页显示的数据条数
     * @param keyword   关键字
     * @param indexName 索引库名称
     * @param fields    搜索字段名称，多个以“，”分割
     * @return
     */
    @RequestMapping("query_hit_page")
    public Page<Map<String,Object>> queryHitByPage(@RequestParam int pageNo,@RequestParam int pageSize
                                                    ,@RequestParam String keyword, @RequestParam String indexName, @RequestParam String fields){
        String[] fieldNames = {};
        if(fields.contains(",")) fieldNames = fields.split(",");
        else fieldNames = new String[]{fields};
        return esSearchService.queryHitByPage(pageNo,pageSize,keyword,indexName,fieldNames);
    }

    /**
     * 删除索引库
     * @param indexName
     * @return
     */
    @RequestMapping("delete_index/{indexName}")
    public String deleteIndex(@PathVariable String indexName){
        esSearchService.deleteIndex(indexName);
        return "success";
    }

    /**
     * 测试
     * @return
     */
    @RequestMapping("query_hit_xin")
    public Page<Map<String,Object>> queryHitXin(){
        return esSearchService.searchDataPage("mysql_order","doc",1,10,QueryBuilders.multiMatchQuery("admin","username").operator(Operator.OR),"","id","");
    }
    /**
     * 测试
     * @return
     */
    @RequestMapping("query_hit_xin1")
    public List<Map<String, Object>> queryHitXin1(){
        return esSearchService.searchListData("mysql_order","doc",QueryBuilders.multiMatchQuery("admin","username").operator(Operator.OR),1000,"","id","username");
    }
}
