package com.boyuanitsm.fort.repository.search;

import com.boyuanitsm.fort.domain.SecurityResourceEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the SecurityResourceEntity entity.
 */
public interface SecurityResourceEntitySearchRepository extends ElasticsearchRepository<SecurityResourceEntity, Long> {
}
