package com.de.service.oauth;

import java.util.Date;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.de.dao.oauth.UserSessionDao;
import com.de.model.entity.UserProfile;
import com.de.model.entity.UserSession;


@Service
public class PreAuthenticationUserService implements AuthenticationUserDetailsService<Authentication> {

  private static final Logger log = Logger.getLogger(PreAuthenticationUserService.class.getName());

  @Autowired
  private UserSessionDao userSessionDao;

  @Override
  @Transactional
  public UserDetails loadUserDetails(Authentication authentication){

    /*
     * Find a connection based on the access_token. Check the expiration date. Find the user
     * associated with the connection
     */

    boolean expired = false;
    String accessToken = (String) authentication.getPrincipal();
    if (null != accessToken) {

      log.info("Found access_token : " + accessToken);
      UserSession userSession = userSessionDao.findByAccessToken(accessToken.trim());
      if (null != userSession) {

        log.info("userSession Found");

        if (userSession.getExpiryDateTime().before(new Date())) {
          expired = true;
        }
        UserProfile userDomain = userSession.getUserId();
        if (userDomain != null) {
          log.info("role of the user: " + userDomain.getUserRole().getUserRole());
          return new SystemUser(userDomain, expired);
        }

      }

    }

    log.info("No user found for authentication token " + authentication);

    throw new UsernameNotFoundException(String.format("No user found for authentication token %s",
        authentication));
  }

}
