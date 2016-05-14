package com.boyuanitsm.fort.repository.search;

import com.boyuanitsm.fort.domain.SecurityRole;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the SecurityRole entity.
 */
public interface SecurityRoleSearchRepository extends ElasticsearchRepository<SecurityRole, Long> {
}
