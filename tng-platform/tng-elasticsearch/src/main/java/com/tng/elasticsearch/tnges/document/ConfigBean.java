package com.tng.elasticsearch.tnges.document;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class ConfigBean {

    private String indexName = "index_tnguser_20200609";

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }
}
