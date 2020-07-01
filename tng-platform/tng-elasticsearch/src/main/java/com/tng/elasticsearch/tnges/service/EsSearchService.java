package com.tng.elasticsearch.tnges.service;

import com.tng.elasticsearch.tnges.document.UserDocument;
import com.tng.elasticsearch.tnges.page.Page;
import org.elasticsearch.index.query.QueryBuilder;

import java.util.List;
import java.util.Map;

public interface EsSearchService extends BaseSearchService<UserDocument> {
    /**
     * 保存
     */
    void save(UserDocument... productDocuments);

    /**
     * 删除
     * @param id
     */
    void delete(String id);

    /**
     * 清空索引
     */
    void deleteAll();

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    UserDocument getById(String id);

    /**
     * 查询全部
     * @return
     */
    List<UserDocument> getAll();
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
    Page<Map<String, Object>> searchDataPage(String index, String type, int pageNo, int pageSize, QueryBuilder query, String fields, String sortField, String highlightField);
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
    List<Map<String, Object>> searchListData(String index, String type, QueryBuilder query, Integer size, String fields, String sortField, String highlightField);
}
