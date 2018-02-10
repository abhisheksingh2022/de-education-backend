package com.de.service.oauth;



import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.SimpleTimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.de.constants.oauth.CommonConstant;
import com.de.dao.oauth.UserProfileDAO;
import com.de.exception.oauth.AuthenticationException;
import com.de.exception.oauth.ErrorCodes;
import com.de.exception.oauth.ObjectNotFoundException;
import com.de.model.entity.UserProfile;
import com.de.model.entity.UserSession;
import com.de.utility.oauth.JWTSigner;

/**
 * The Class UserProfileServiceImpl.
 */
@Service
public class UserProfileServiceImpl implements UserProfileService {

	/** Logger */
	private final static Logger logger = LoggerFactory.getLogger(UserProfileServiceImpl.class);


  /** The message source. */
  @Autowired
  @Qualifier(value = "messageSource")
  MessageSource messageSource;

  /** The locale. */
  Locale locale = LocaleContextHolder.getLocale();

  /** The user profile dao. */
  @Autowired
  public UserProfileDAO userProfileDAO;

  /** The iss. */
  @Value("${iss}")
  private String iss;

  /** The sub. */
  @Value("${sub}")
  private String sub;

  /** The salt key. */
  @Value("${saltKey}")
  private String saltKey;

  @Value("${expirationTime}")
  private Integer expirationTime;
  /*
   * (non-Javadoc)
   * 
   * @see
   * com.raastech.mobile.rest.service.UserProfileService#doLogin(com.raastech.mobile.rest.entity
   * .Users)
   */
  @Override
  public UserSession doLogin(UserProfile users,String deviceId) {
	  logger.info("Entering doLogin() of UserProfileServiceImpl ");
    String result = userProfileDAO.getLogin(users);
    UserSession token = null;
    if (result.equals(CommonConstant.OK)) {
      return createSession(users, token,deviceId);
    } else if (result.equals(CommonConstant.LOCKED)) {
    	  logger.info("Entering doLogin() of UserProfileServiceImpl ");
			throw new AuthenticationException(messageSource.getMessage(
					CommonConstant.LOCKED_USER, null, locale), ErrorCodes.CODE_AUTHENTICATION_EXCEPTION);
    } else if (result.equals(CommonConstant.INVALID_WARNING)) {
    	  logger.info("Entering doLogin() of UserProfileServiceImpl ");
      throw new AuthenticationException(messageSource.getMessage(CommonConstant.INVALID_WARNING, null, null),
          ErrorCodes.CODE_INVALID_WARNING);
    } else {
    	  logger.info("Entering doLogin() of UserProfileServiceImpl ");
      throw new AuthenticationException(messageSource.getMessage(
          CommonConstant.INVALID_USERNAME_AND_PASSWORD, null, null), ErrorCodes.CODE_INVALID_USERNAME_AND_PASSWORD);
    }
    	
  }

  //
  /**
   * Creates the session.the session is created on the basic of the device id if the device id i not available 
   * in the database then the generated token is saved in the database and if it is available in database 
   * then it is updated by the new token.
   *
   * @param users the users
   * @param token the token
   * @param deviceId the device id
   * @return the response entity
   */
  private UserSession createSession(UserProfile users, UserSession token, String deviceId) {
    UserSession session = null;
    logger.info("Entering createSession() of UserProfileServiceImpl ");
    UserProfile user = userProfileDAO.fetchUser(users);
    if (user != null) {
      logger.info("Entered createSession() inside for session****************************" + users);
      session = new UserSession();
      session.setUserId(user);;
      Calendar cal = Calendar.getInstance(); // creates calendar
      cal.setTime(new Date()); // sets calendar time/date
      cal.add(Calendar.HOUR_OF_DAY, expirationTime); // adds six hour
     
     // Sets the time in UTC
      SimpleDateFormat sdf = new SimpleDateFormat();
      sdf.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));
      sdf.setCalendar(cal);
      session.setExpiryDateTime(new Timestamp(sdf.getCalendar().getTime().getTime()));
   // returns new date object, six hour in the future
      Map<String, Object> claims = new HashMap<String, Object>();
      claims.put(CommonConstant.ISS, iss);
      claims.put(CommonConstant.SUB, sub);
      claims.put(CommonConstant.EXP, sdf.getCalendar().getTime().getTime());
      
      JWTSigner jwt = new JWTSigner(saltKey);
      session.setToken(jwt.sign(claims));
      session = userProfileDAO.updateUserSession(session);
      if (session != null) {
    	return session;
      }
    }
    logger.info("Exiting createSession() of UserProfileServiceImpl ");
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.raastech.mobile.rest.service.UserProfileService#getUsersObject(javax.servlet.http.
   * HttpServletRequest)
   */
  @Override
  public UserProfile getUsersObject(String accessToken) {
	  logger.info("Entering createSession() of UserProfileServiceImpl ");
    //validate secret token
	  UserSession session = userProfileDAO.validateToken(accessToken);
//    UserSession session = userProfileDAO.validateToken(request.getHeader(CommonConstant.SECRET_TOKEN));
    if (session != null) {
    	UserProfile user = userProfileDAO.fetchUserByUserId(session.getUserId().getId());
      if(user!=null){
    	  logger.info("Exiting createSession() of UserProfileServiceImpl ");
        return user;
      }
    }
    logger.info("Exiting createSession() of UserProfileServiceImpl user object not found");
    throw new ObjectNotFoundException(CommonConstant.USER_OBJECT_NOT_FOUND,
        ErrorCodes.OBJECT_NOT_FOUND);
  }

  /* (non-Javadoc)
   * @see com.raastech.mobile.rest.service.UserProfileService#validateSecretKey(java.lang.String)
   */
  @Override
  @Transactional
  public String validateSecretKey(String token) {
	  logger.info("Entering createSession() of UserProfileServiceImpl ");
    UserSession session = userProfileDAO.validateToken(token);
    if (session != null) {
    	  
    	 Calendar cal = Calendar.getInstance(); // creates calendar
         cal.setTime(new Date()); // sets calendar time/date
         cal.add(Calendar.HOUR_OF_DAY, expirationTime); // adds six hour
      
        // Sets the time in UTC
         SimpleDateFormat sdf = new SimpleDateFormat();
         sdf.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));
         sdf.setCalendar(cal);
         long expireTime = sdf.getCalendar().getTime().getTime();
		 session.setExpiryDateTime(new Timestamp(expireTime));
    	 Map<String, Object> claims = new HashMap<String, Object>();
         claims.put(CommonConstant.ISS, iss);
         claims.put(CommonConstant.SUB, sub);
         claims.put(CommonConstant.EXP, expireTime);
    	JWTSigner jwt = new JWTSigner(saltKey);
        session.setToken(jwt.sign(claims));
        session = userProfileDAO.updateUserSession(session);
        logger.info("Entering createSession() of UserProfileServiceImpl got session object"+session);
        return session.getToken();
    }
    logger.info("Exiting createSession() of UserProfileServiceImpl ");
   return null;
  }


}
