package com.tng.elasticsearch.tnges.repository;

import com.tng.elasticsearch.tnges.document.UserDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;


@Component
public interface UserDocumentRepository extends ElasticsearchRepository<UserDocument,String> {
}
