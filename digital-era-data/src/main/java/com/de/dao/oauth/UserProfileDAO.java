package com.de.dao.oauth;

import com.de.model.entity.UserProfile;
import com.de.model.entity.UserSession;

// TODO: Auto-generated Javadoc
/**
 * The Interface UserProfileDAO.
 */
public interface UserProfileDAO {

	/**
	 * Gets the login.
	 *
	 * @param users the users
	 * @return the login
	 */
	public String getLogin(UserProfile users);

	/**
	 * Update user session.
	 *
	 * @param session the session
	 * @return the user session
	 */
	public UserSession updateUserSession(UserSession session);
	/**
	 * Fetch user.
	 *
	 * @param users the users
	 * @return the users
	 */
	public UserProfile fetchUser(UserProfile users);
	
	 /**
	   * Validate token.
	   *
	   * @param header the header
	   * @return the user session
	   */
	public  UserSession validateToken(String header);

	  /**
	   * Fetch user by user id.
	   *
	   * @param id the id
	   * @return the users
	   */
	public  UserProfile fetchUserByUserId(Integer id);

		
}
