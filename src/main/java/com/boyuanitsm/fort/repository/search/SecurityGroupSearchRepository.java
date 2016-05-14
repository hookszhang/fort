package com.boyuanitsm.fort.repository.search;

import com.boyuanitsm.fort.domain.SecurityGroup;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the SecurityGroup entity.
 */
public interface SecurityGroupSearchRepository extends ElasticsearchRepository<SecurityGroup, Long> {
}
