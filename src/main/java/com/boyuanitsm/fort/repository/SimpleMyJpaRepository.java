package com.boyuanitsm.fort.repository;

import com.boyuanitsm.fort.domain.SecurityNav;
import com.boyuanitsm.fort.security.AuthoritiesConstants;
import com.boyuanitsm.fort.security.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.security.access.method.P;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public class SimpleMyJpaRepository<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements MyJpaRepository<T, ID> {

    private final EntityManager entityManager;

    public SimpleMyJpaRepository(JpaEntityInformation entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        // Keep the EntityManager around to used from the newly introduced methods.
        this.entityManager = entityManager;
    }

    @Override
    public Page<T> findOwnAll(Pageable var1) {
        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)){
            return findOwnAllRoleAdmin(var1);
        } else if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.USER)) {
            return findOwnAllRoleUser(var1);
        } else {
            return findOwnAllRoleSecurityApp(var1);
        }
    }

    @Override
    public Page<T> findAllByAppId(Pageable var1, Long appId) {
        return this.findAll((root, query, cb) -> {
            Path<String> appIdPath = root.get("app").get("id");
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(appIdPath, appId));
            query.where(predicates.toArray(new Predicate[predicates.size()]));
            return query.getRestriction();
        }, var1);
    }

    /**
     * role is ROLE_ADMIN.
     *
     * @param var1
     * @return all data
     */
    private Page<T> findOwnAllRoleAdmin(Pageable var1) {
        return this.findAll(var1);
    }

    /**
     * role is ROLE_USER.
     *
     * @param var1
     * @return own created data.
     */
    private Page<T> findOwnAllRoleUser(Pageable var1) {
        String userLogin = SecurityUtils.getCurrentUserLogin();
        return this.findAll((root, query, cb) -> {
            Path<String> createdByPath = root.get("createdBy");
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(createdByPath, userLogin));
            query.where(predicates.toArray(new Predicate[predicates.size()]));
            return query.getRestriction();
        }, var1);
    }

    /**
     * role is ROLE_SECURITY_APP.
     *
     * @param var1
     * @return this app data.
     */
    private Page<T> findOwnAllRoleSecurityApp(Pageable var1) {
        String appKey = SecurityUtils.getCurrentUserLogin();
        return this.findAll((root, query, cb) -> {
            Path<String> appPath;
            if (root.getJavaType().equals(SecurityNav.class)) {
                appPath = root.get("resource").get("app").get("appKey");
            } else {
                appPath = root.get("app").get("appKey");
            }
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(appPath, appKey));
            query.where(predicates.toArray(new Predicate[predicates.size()]));
            return query.getRestriction();
        }, var1);
    }
}
