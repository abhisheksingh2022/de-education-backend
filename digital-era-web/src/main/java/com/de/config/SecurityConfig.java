package com.de.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.de.oauth.config.CustomAccessDeniedHandler;
import com.de.oauth.config.Http403ForbiddenEntryPoint;
import com.de.oauth.config.PreAuthenitcationFilter;
import com.de.service.oauth.PreAuthenticationUserService;

/**
 * The Class SecurityConfig.
 * 
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	/**
	 * Preauth auth provider.
	 *
	 * @return the pre authenticated authentication provider
	 */
	@SuppressWarnings("unchecked")
	@Bean
	public PreAuthenticatedAuthenticationProvider preauthAuthProvider() {
		PreAuthenticatedAuthenticationProvider preAuthenticatedProvider = new PreAuthenticatedAuthenticationProvider();
		preAuthenticatedProvider.setPreAuthenticatedUserDetailsService(preAuthUserService());
		return preAuthenticatedProvider;
	}
	
	/**
	 * Pre auth user service.
	 *
	 * @return the authentication user details service
	 */
	@SuppressWarnings("rawtypes")
	@Bean
	public AuthenticationUserDetailsService preAuthUserService() {
	    return new PreAuthenticationUserService();
	}

	/**
	 * Pre auth filter.
	 *
	 * @return the pre authenitcation filter
	 * @throws Exception the exception
	 */
	@Bean
	public PreAuthenitcationFilter preAuthFilter() throws Exception {
		PreAuthenitcationFilter preAuthFilter = new PreAuthenitcationFilter();
		preAuthFilter.setAuthenticationManager(authenticationManager());
		return preAuthFilter;
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder)
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception{
		
		auth.authenticationProvider(preauthAuthProvider());
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.web.builders.WebSecurity)
	 */
	@Override
	public void configure(WebSecurity web) throws Exception{
		web.ignoring()
			.antMatchers("/resources/**")
			.antMatchers("/static/**")
			.antMatchers("/fonts/**")
			.antMatchers("/css/**")
			.antMatchers("/images/**")
			.antMatchers("/app/**")
			.antMatchers("/*.html")
			.antMatchers("/*.js");
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.web.builders.HttpSecurity)
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	  
	   http.exceptionHandling().authenticationEntryPoint(new Http403ForbiddenEntryPoint());
	  
	   http.csrf().disable()
		.addFilterBefore(preAuthFilter(), BasicAuthenticationFilter.class)
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.enableSessionUrlRewriting(false)
		.and()
		.authorizeRequests()
			.antMatchers(HttpMethod.POST,"/api/v1/users/session/*").permitAll()
			.antMatchers(HttpMethod.GET,"/api/v1/users/test").permitAll()
			.antMatchers(HttpMethod.GET,"/api/v1/users/test1").permitAll()
			.antMatchers(HttpMethod.GET,"/api/logout").hasAuthority("ROLE_USER")
//			.antMatchers(HttpMethod.GET,"/api/logout").permitAll()
			.anyRequest().denyAll().and().httpBasic()
			.and().exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())
			.authenticationEntryPoint(new Http403ForbiddenEntryPoint());
	}
    
}
