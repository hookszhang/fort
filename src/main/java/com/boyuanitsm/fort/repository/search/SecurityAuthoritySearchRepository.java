package com.boyuanitsm.fort.repository.search;

import com.boyuanitsm.fort.domain.SecurityAuthority;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the SecurityAuthority entity.
 */
public interface SecurityAuthoritySearchRepository extends ElasticsearchRepository<SecurityAuthority, Long> {
}
