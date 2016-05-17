package com.boyuanitsm.fort.repository;

import com.boyuanitsm.fort.security.AuthoritiesConstants;
import com.boyuanitsm.fort.security.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.io.Serializable;
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
            Path<String> appPath = root.get("app").get("appKey");
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(cb.lower(appPath), appKey));
            query.where(predicates.toArray(new Predicate[predicates.size()]));
            return query.getRestriction();
        }, var1);
    }
}
