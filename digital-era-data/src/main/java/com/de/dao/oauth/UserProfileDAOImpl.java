package com.de.dao.oauth;


import java.util.ArrayList;
import java.util.List;
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
import org.springframework.transaction.annotation.Transactional;

import com.de.constants.oauth.CommonConstant;
import com.de.exception.oauth.AuthenticationException;
import com.de.exception.oauth.BusinessException;
import com.de.exception.oauth.ErrorCodes;
import com.de.exception.oauth.RequiredFieldMissingException;
import com.de.model.entity.UserProfile;
import com.de.model.entity.UserSession;


// TODO: Auto-generated Javadoc
/**
 * The Class UserProfileDAOImpl.
 */
@Repository
public class UserProfileDAOImpl implements UserProfileDAO {
	
	/** Logger */
	private final static Logger logger = LoggerFactory.getLogger(UserProfileDAOImpl.class);
	  
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
	
	/* (non-Javadoc)
	 * @see com.raastech.mobile.rest.dao.UserProfileDAO#getLogin(com.raastech.mobile.rest.entity.Users)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public String getLogin(UserProfile users) {
		  logger.info("Entering in UserProfileDAOImpl getLogin method");
		Criteria criteria = getCurrentSession().createCriteria(UserProfile.class);
		criteria.add(Restrictions.eq("userMail", users.getUserMail()));
		List<UserProfile> usersList = (ArrayList<UserProfile>)criteria.list();

		if(usersList!=null && !usersList.isEmpty()){	
			UserProfile dbUsers = usersList.get(0);		
		
			if(users.getPassword().equals(dbUsers.getPassword())){
				logger.info("Exiting in UserProfileDAOImpl getLogin method  login done");
				return CommonConstant.OK;
			}else{
				logger.info("Exiting in UserProfileDAOImpl getLogin method invalid password");
				return CommonConstant.INVALID;		
			}
		
		}else{
			  logger.info("Exiting in UserProfileDAOImpl getLogin method");
			throw new AuthenticationException(messageSource.getMessage(CommonConstant.LOGIN_ERROR_MESSAGE, null,locale),198);

		}
		
		
	}
	
	/* (non-Javadoc)
	 * @see com.raastech.mobile.rest.dao.UserProfileDAO#updateUserSession(com.raastech.mobile.rest.entity.UserSession)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public UserSession updateUserSession(UserSession session) {
		  logger.info("Entering in UserProfileDAOImpl updateUserSession method");
	
		Criteria criteria=getCurrentSession().createCriteria(UserSession.class);
        criteria.add(Restrictions.eq("userId.id", session.getUserId().getId()));
        ArrayList<UserSession> userSessionList=(ArrayList<UserSession>) criteria.list();
        if(userSessionList!=null && !userSessionList.isEmpty()){
        	UserSession tempSession=userSessionList.get(0);
        	Integer id = tempSession.getToken_id();
			session.setToken_id(id);
        	getCurrentSession().merge(session);
      	  logger.info("Exiting in UserProfileDAOImpl updateUserSession method  updated session object is "+session);
        	return (UserSession) getCurrentSession().get(UserSession.class, id);
        }else{
        	Integer id=(Integer) getCurrentSession().save(session);
      	  logger.info("Exiting in UserProfileDAOImpl updateUserSession method saved session object is"+session);
    		return (UserSession) getCurrentSession().get(UserSession.class, id);
        }
			
		
		
	}

	/* (non-Javadoc)
	 * @see com.raastech.mobile.rest.dao.UserProfileDAO#fetchUser(com.raastech.mobile.rest.entity.Users)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public UserProfile fetchUser(UserProfile users) {
		 logger.info("Entering in UserProfileDAOImpl fetchUser method");
		Criteria criteria = getCurrentSession().createCriteria(UserProfile.class);
		criteria.add(Restrictions.eq("userMail", users.getUserMail()));
		criteria.add(Restrictions.eq("password", users.getPassword()));
		List<UserProfile> usersList =(ArrayList<UserProfile>) criteria.list();
		
		if(usersList!=null && !usersList.isEmpty()){
			  logger.info("Exiting in UserProfileDAOImpl fetchUser method user is"+usersList.get(0));
			return usersList.get(0);
		}
		logger.info("Exiting in UserProfileDAOImpl getLogin method");
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.raastech.mobile.rest.dao.UserProfileDAO#validateToken(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
    public UserSession validateToken(String token) {
		  logger.info("Entering in UserProfileDAOImpl validateToken method");
    Criteria criteria=getCurrentSession().createCriteria(UserSession.class);
    criteria.add(Restrictions.eq("token", token));
        ArrayList<UserSession> sessionList=(ArrayList<UserSession>) criteria.list();
    
        if(sessionList!=null && !sessionList.isEmpty()){
      	  logger.info("Exiting in UserProfileDAOImpl validateToken method got session object"+sessionList.get(0));
          return sessionList.get(0);
        }     
  	  logger.info("Exiting in UserProfileDAOImpl validateToken method");
        throw new RequiredFieldMissingException(CommonConstant.SECRET_TOKEN_INVALID, ErrorCodes.CODE_INVALID_FIELD);
    }
    
    /* (non-Javadoc)
     * @see com.raastech.mobile.rest.dao.UserProfileDAO
     */
    @SuppressWarnings("unchecked")
	@Override
    public UserProfile fetchUserByUserId(Integer id) {
  	  logger.info("Entering in UserProfileDAOImpl fetchUserByUserId method");
        Criteria criteria=getCurrentSession().createCriteria(UserProfile.class);
        criteria.add(Restrictions.eq("id", id));
        ArrayList<UserProfile> usersList=(ArrayList<UserProfile>) criteria.list();
    
        if(usersList!=null && !usersList.isEmpty()){
      	  logger.info("Exiting in UserProfileDAOImpl fetchUserByUserId method user object is"+usersList.get(0));
            return usersList.get(0);
        }
  	  logger.info("Exiting in UserProfileDAOImpl fetchUserByUserId method");
      return null;
    }
	

}
