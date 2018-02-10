package com.de.service.oauth;

import com.de.model.entity.UserProfile;
import com.de.model.entity.UserSession;



// TODO: Auto-generated Javadoc
/**
 * The Interface UserProfileService.
 */
public interface UserProfileService {

	/**
	 * Do login.
	 *
	 * @param users the users
	 * @param deviceId the device id
	 * @return the response entity
	 */
	public UserSession doLogin(UserProfile users, String deviceId);
	
	/**
	 * Gets the users object.
	 *
	 * @param request the request
	 * @return the users object
	 */
	public UserProfile getUsersObject(String accessToken);
	
	/**
	 * Validate secret key.
	 *
	 * @param string the string
	 * @return the string
	 */
	public String validateSecretKey(String string);
	
}
