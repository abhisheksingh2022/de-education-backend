package com.de.dao.oauth;

import java.util.ArrayList;
import java.util.Locale;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Repository;

import com.de.exception.oauth.BusinessException;
import com.de.model.entity.UserSession;



@Repository
public class UserSessionIDaoImpl implements UserSessionDao {

	
	/** Logger */
	private final static Logger logger = LoggerFactory.getLogger(UserSessionIDaoImpl.class);
	
	/** The message source. */
	@Autowired
	@Qualifier(value = "messageSource")
	MessageSource messageSource;
	
	/** The locale. */
	Locale locale = LocaleContextHolder.getLocale();

	/** The session factory. */
	@Autowired
	private SessionFactory sessionFactory;
	
	/**
	 * Gets the current session.
	 *
	 * @return the current session
	 * @throws BusinessException the business exception
	 */
	private Session getCurrentSession() throws BusinessException {

		if (sessionFactory != null && sessionFactory.getCurrentSession() != null) {
			return sessionFactory.getCurrentSession();
		}
		return null;

	}
	
	@SuppressWarnings("unchecked")
	@Override
	public UserSession findByAccessToken(String token) {
		  logger.info("Entering in UserSessionIDaoImpl findByAccessToken method");
		  Criteria criteria=getCurrentSession().createCriteria(UserSession.class);
	      criteria.add(Restrictions.eq("token",token));
		  ArrayList<UserSession> userSessionList=(ArrayList<UserSession>) criteria.list();
	      if(userSessionList!=null && !userSessionList.isEmpty()){
	       	UserSession tempSession=userSessionList.get(0);
	        logger.info("UserSesion Exiting ");
	        return tempSession;
	      }
		return null;
	}

}
