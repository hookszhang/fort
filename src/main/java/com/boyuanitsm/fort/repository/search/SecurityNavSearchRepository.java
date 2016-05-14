package com.boyuanitsm.fort.repository.search;

import com.boyuanitsm.fort.domain.SecurityNav;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the SecurityNav entity.
 */
public interface SecurityNavSearchRepository extends ElasticsearchRepository<SecurityNav, Long> {
}
