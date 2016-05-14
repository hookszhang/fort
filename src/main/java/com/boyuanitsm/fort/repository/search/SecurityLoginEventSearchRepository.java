package com.boyuanitsm.fort.repository.search;

import com.boyuanitsm.fort.domain.SecurityLoginEvent;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the SecurityLoginEvent entity.
 */
public interface SecurityLoginEventSearchRepository extends ElasticsearchRepository<SecurityLoginEvent, Long> {
}
