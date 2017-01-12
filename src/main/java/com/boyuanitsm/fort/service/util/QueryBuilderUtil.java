/*
 * Copyright 2016-2017 Shanghai Boyuan IT Services Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.boyuanitsm.fort.service.util;

import com.boyuanitsm.fort.security.AuthoritiesConstants;
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
     * Build elasticsearch query. if role ROLE_ADMIN, search all data.
     * if role ROLE_USER, search own created app correlation data.
     * if role ROLE_SECURITY_APP, search this app data.
     *
     * @param query the query string
     * @return the elasticsearch query
     */
    public static QueryBuilder build(String query) {
        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
            // if role ROLE_ADMIN, search all data.
            return queryStringQuery(query);
        }

        List<String> loginList = new ArrayList<String>();
        loginList.add(SecurityUtils.getCurrentUserLogin());


        FilterBuilder filter;

        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.USER)) {
            // if role ROLE_USER, search own created app correlation data.
            filter = FilterBuilders.termsFilter("app.createdBy", loginList);
        } else {
            // if role ROLE_SECURITY_APP, search this app data.
            filter = FilterBuilders.termsFilter("app.appKey", loginList);
        }

        return filteredQuery(queryStringQuery(query), filter);
    }
}
