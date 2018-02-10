package com.de.dao.oauth;

import com.de.model.entity.UserSession;

public interface UserSessionDao {

	public UserSession findByAccessToken(String token);
}
