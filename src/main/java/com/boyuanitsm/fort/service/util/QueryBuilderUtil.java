package com.boyuanitsm.fort.service.util;

import com.boyuanitsm.fort.security.SecurityUtils;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.filteredQuery;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * The Elasticsearch query builder util.
 *
 * @author zhanghua on 6/1/16.
 */
public class QueryBuilderUtil {

    /**
     * Only search own created data.
     *
     * @param query the query string
     * @return
     */
    public static QueryBuilder build(String query) {
        List<String> idList = new ArrayList<String>();
        idList.add(SecurityUtils.getCurrentUserLogin());
        FilterBuilder filter = FilterBuilders.termsFilter("createdBy", idList);
        return filteredQuery(queryStringQuery(query), filter);
    }
}
