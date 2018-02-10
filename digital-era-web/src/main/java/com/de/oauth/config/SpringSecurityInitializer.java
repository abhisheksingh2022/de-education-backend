package com.de.oauth.config;

import org.springframework.core.annotation.Order;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

@Order(1)
public class SpringSecurityInitializer extends AbstractSecurityWebApplicationInitializer{

}
