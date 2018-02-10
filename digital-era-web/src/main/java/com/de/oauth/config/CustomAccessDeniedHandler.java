package com.de.oauth.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.de.exception.oauth.ErrorCodes;

public class CustomAccessDeniedHandler implements AccessDeniedHandler{

	/* (non-Javadoc)
	 * @see org.springframework.security.web.access.AccessDeniedHandler#handle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.security.access.AccessDeniedException)
	 */
	@Override
	public void handle(HttpServletRequest request,HttpServletResponse response,AccessDeniedException accessDeniedException) throws IOException,ServletException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
	    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
	    response.getOutputStream().println("{\"httpCode\": "+HttpServletResponse.SC_FORBIDDEN+",\"errorCode\": "+ErrorCodes.INVALID_REQUEST_DATA+",\"message\": \"Invalid Request\"}");
		
	}

}
