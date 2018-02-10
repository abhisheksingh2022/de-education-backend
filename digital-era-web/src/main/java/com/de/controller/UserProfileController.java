package com.de.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.CookieGenerator;

import com.de.constants.oauth.AppConstants;
import com.de.exception.oauth.ErrorCodes;
import com.de.exception.oauth.RequiredFieldMissingException;
import com.de.model.dto.AccessToken;
import com.de.model.dto.Student;
import com.de.model.entity.UserProfile;
import com.de.model.entity.UserSession;
import com.de.oauth.config.PreAuthenitcationFilter;
import com.de.service.oauth.UserProfileService;


// TODO: Auto-generated Javadoc
/**
 * The Class UserProfileController.
 */
@RestController
@RequestMapping("v1/users")
public class UserProfileController {	
	
	/** Logger */
	private final static Logger logger = LoggerFactory.getLogger(UserProfileController.class);
	   
	  
	/** The user profile service. */
	@Autowired
	public UserProfileService userProfileService;
	
	
	/**
	 * This API is used to do login and generate token for the user using Users and UserSession class. The token expiration  time is 6 hours
	 * This API also takes device id for the consuming parties where they will provide the unique device id with username and password.
	 * The same token will be returned id the provided device already exist in the database and if not they we will generate the token for the User.    
	 *
	 * @param request the request
	 * @param response the response
	 * @param users the users
	 * @param deviceId the device id
	 * @return the response entity
	 */
	
	@RequestMapping(value = "/session/{deviceId}", method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public AccessToken login(HttpServletRequest request,HttpServletResponse response, @RequestBody UserProfile users,@PathVariable String deviceId) {
		logger.info("Entering in UserProfileController login method");
			
		if(users.getUserMail()==null || users.getUserMail().trim().equals("") || users.getPassword()==null || users.getPassword().trim().equals("") || deviceId.trim().equals("")){
			throw new RequiredFieldMissingException(AppConstants.REQUIRED_FIELD_MISSING, ErrorCodes.MISSING_ARGUMENT);
		}
		logger.info("Exiting in UserProfileController login method");
		
		UserSession session = userProfileService.doLogin(users,deviceId);
		AccessToken token = new AccessToken(session.getToken(), null);
		
		populateAccessTokenCookie(true, response,token);
		
//		System.out.println("Token : " + token);
		return token;
	}
	 public static void populateAccessTokenCookie(boolean supportCookie, HttpServletResponse response, AccessToken body) {
	        if (supportCookie && body != null && StringUtils.isNotEmpty(body.getAccessToken())) {
	            CookieGenerator cookieGenerator = new CookieGenerator();
	            cookieGenerator.setCookieSecure(true);

	            cookieGenerator.setCookieName(PreAuthenitcationFilter.AUTH_PARAM_OAUTH);
	            cookieGenerator.setCookiePath("/");
	            cookieGenerator.setCookieHttpOnly(true);
//	            long millis = body.getExpiresIn() - System.currentTimeMillis();
//	            cookieGenerator.setCookieMaxAge((int) TimeUnit.SECONDS.convert(millis, TimeUnit.MILLISECONDS));
	            cookieGenerator.addCookie(response, body.getAccessToken());
	        }
	    }
	
	/**
	 * Logout.
	 *
	 * @param request the request
	 * @param response the response
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("hiu");
	}
	
	
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Student>  test(HttpServletRequest request,HttpServletResponse response) {
		logger.info("Entering in UserProfileController login method");
	
		Student s = new Student();
		s.setName("Shailesh");
		s.setRollNumber("112312312");
		
		return new ResponseEntity<Student>(s,HttpStatus.OK);
	}
	@RequestMapping(value = "/test1", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public String  test1(HttpServletRequest request,HttpServletResponse response) {
		logger.info("Entering in UserProfileController login method");
	
		/*Student s = new Student();
		s.setName("Shailesh");
		s.setRollNumber("112312312");
		return s;*/
		return "abhishek";
	}
	

}