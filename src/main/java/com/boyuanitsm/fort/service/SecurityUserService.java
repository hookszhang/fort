package com.boyuanitsm.fort.service;

import com.boyuanitsm.fort.bean.enumeration.OnUpdateSecurityResourceOption;
import com.boyuanitsm.fort.domain.SecurityApp;
import com.boyuanitsm.fort.domain.SecurityLoginEvent;
import com.boyuanitsm.fort.domain.SecurityUser;
import com.boyuanitsm.fort.repository.SecurityAppRepository;
import com.boyuanitsm.fort.repository.SecurityLoginEventRepository;
import com.boyuanitsm.fort.repository.SecurityUserRepository;
import com.boyuanitsm.fort.repository.search.SecurityUserSearchRepository;
import com.boyuanitsm.fort.security.SecurityUtils;
import com.boyuanitsm.fort.service.util.QueryBuilderUtil;
import com.boyuanitsm.fort.web.rest.dto.SecurityUserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

import java.time.ZonedDateTime;
import java.util.List;

import static com.boyuanitsm.fort.bean.enumeration.OnUpdateSecurityResourceClass.SECURITY_USER;
import static com.boyuanitsm.fort.bean.enumeration.OnUpdateSecurityResourceOption.DELETE;
import static com.boyuanitsm.fort.bean.enumeration.OnUpdateSecurityResourceOption.POST;
import static com.boyuanitsm.fort.bean.enumeration.OnUpdateSecurityResourceOption.PUT;

/**
 * Service Implementation for managing SecurityUser.
 */
@Service
@Transactional
public class SecurityUserService {

    private final Logger log = LoggerFactory.getLogger(SecurityUserService.class);

    @Inject
    private PasswordEncoder passwordEncoder;

    @Inject
    private SecurityUserRepository securityUserRepository;

    @Inject
    private SecurityUserSearchRepository securityUserSearchRepository;

    @Inject
    private SecurityAppRepository securityAppRepository;

    @Inject
    private SecurityLoginEventRepository securityLoginEventRepository;

    @Inject
    private SecurityLoginEventService securityLoginEventService;

    @Inject
    private SecurityResourceUpdateService updateService;

    /**
     * Save a securityUser.
     *
     * @param securityUser the entity to save
     * @return the persisted entity
     */
    public SecurityUser save(SecurityUser securityUser) {
        log.debug("Request to save SecurityUser : {}", securityUser);

        OnUpdateSecurityResourceOption option = securityUser.getId() == null ? POST : PUT;

        if (PUT.equals(option)) {
            SecurityUser old = securityUserRepository.findOne(securityUser.getId());
            if (!securityUser.getPasswordHash().equals(old.getPasswordHash())) {
                // encode password
                String passwordHash = securityUser.getPasswordHash();
                securityUser.setPasswordHash(passwordEncoder.encode(passwordHash));
            }
        } else {
            // encode password
            String passwordHash = securityUser.getPasswordHash();
            securityUser.setPasswordHash(passwordEncoder.encode(passwordHash));
        }

        SecurityUser result = securityUserRepository.save(securityUser);
        securityUserSearchRepository.save(result);

        if (PUT.equals(option)) {
            List<SecurityLoginEvent> events = securityLoginEventRepository.findByUserIdAndTokenOverdueTime(securityUser.getId(), ZonedDateTime.now());
            if (!events.isEmpty()) {
                for (SecurityLoginEvent event: events) {
                    // send update message
                    updateService.send(option, SECURITY_USER, new SecurityUserDTO(securityUser, event));
                }
            }
        }

        return result;
    }

    /**
     *  Get all the securityUsers.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SecurityUser> findAll(Pageable pageable) {
        log.debug("Request to get all SecurityUsers");
        Page<SecurityUser> result = securityUserRepository.findOwnAll(pageable);
        return result;
    }

    /**
     *  Get one securityUser by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public SecurityUser findOne(Long id) {
        log.debug("Request to get SecurityUser : {}", id);
        SecurityUser securityUser = securityUserRepository.findOneWithEagerRelationships(id);
        return securityUser;
    }

    /**
     *  Delete the  securityUser by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete SecurityUser : {}", id);
        securityUserRepository.delete(id);
        securityUserSearchRepository.delete(id);
    }

    /**
     * Search for the securityUser corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SecurityUser> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SecurityUsers for query {}", query);
        return securityUserSearchRepository.search(QueryBuilderUtil.build(query), pageable);
    }

    /**
     * Is authorization access app server
     *
     * @return if authorization success return SecurityUser else return null
     */
    @Transactional
    public SecurityUserDTO authorization(SecurityUserDTO securityUserDTO) {
        // get current logged appKey
        SecurityApp app = securityAppRepository.findByAppKey(SecurityUtils.getCurrentUserLogin());
        // get user
        SecurityUser user = securityUserRepository.findByLoginAndApp(securityUserDTO.getLogin(), app);
        if (user == null || !user.isActivated()) {
            return null;
        }

        // validate password
        if (passwordEncoder.matches(securityUserDTO.getPasswordHash(), user.getPasswordHash())){
            user = securityUserRepository.findOneWithEagerRelationships(user.getId());
            keepMaxSessions(user);
            // add login event
            SecurityLoginEvent event = new SecurityLoginEvent(user, securityUserDTO.getIpAddress(), securityUserDTO.getUserAgent(), app.getSessionMaxAge());
            securityLoginEventRepository.save(event);

            return new SecurityUserDTO(user, event);
        }

        return null;
    }

    private void keepMaxSessions(SecurityUser user) {
        SecurityApp app = user.getApp();
        ZonedDateTime now = ZonedDateTime.now();
        List<SecurityLoginEvent> events = securityLoginEventRepository.findByUserIdAndTokenOverdueTime(user.getId(), now);

        if (events.size() > app.getMaxSessions() - 1) {
            // Get early login event, because order by securityLoginEvent.tokenOverdueTime asc so get(0)
            SecurityLoginEvent event = events.get(0);
            securityLoginEventService.overdue(event);
        }
    }

    public SecurityUser findByLoginAndApp(String login, SecurityApp app) {
        return securityUserRepository.findByLoginAndApp(login, app);
    }

    public SecurityUserDTO findByUserToken(String userToken) {
        if (userToken == null || userToken.isEmpty()) {
            return null;
        }

        ZonedDateTime now = ZonedDateTime.now();
        SecurityLoginEvent event = securityLoginEventRepository.findByTokenValueAndTokenOverdueTime(userToken, now);

        if (event == null) {
            return null;
        }

        SecurityUser user = securityUserRepository.findOneWithEagerRelationships(event.getUser().getId());

        return new SecurityUserDTO(user, event);
    }

    /**
     * Logout. userToken set overdue.
     *
     * @param event the token value of the SecurityLoginEvent
     */
    @Transactional
    public void logout(SecurityLoginEvent event) {
        ZonedDateTime now = ZonedDateTime.now();
        event = securityLoginEventRepository.findByTokenValueAndTokenOverdueTime(event.getTokenValue(), now);
        securityLoginEventService.overdue(event);
    }
}
