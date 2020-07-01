package com.tng.elasticsearch.tnges.service;

import com.central.common.model.PageResult;
import com.tng.elasticsearch.tnges.document.UserDocument;

import java.util.Map;

public interface EsUserSearchService extends BaseSearchService<UserDocument> {

    PageResult<UserDocument> searchPage(String indexName, String type, Integer pageNo, Integer pageSize, UserDocument userDocument);
}
