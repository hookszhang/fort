package com.boyuanitsm.fort.repository.search;

import com.boyuanitsm.fort.domain.SecurityApp;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the SecurityApp entity.
 */
public interface SecurityAppSearchRepository extends ElasticsearchRepository<SecurityApp, Long> {
}
