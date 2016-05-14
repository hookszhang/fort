package com.boyuanitsm.fort.repository.search;

import com.boyuanitsm.fort.domain.SecurityUser;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the SecurityUser entity.
 */
public interface SecurityUserSearchRepository extends ElasticsearchRepository<SecurityUser, Long> {
}
