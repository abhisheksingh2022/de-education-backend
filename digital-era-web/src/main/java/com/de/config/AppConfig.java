package com.de.config;

import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.de.oauth.config.RestJsonExceptionResolver;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;


@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.de.controller"})
@PropertySources({ @PropertySource("classpath:application.properties")})
@Import({ SecurityConfig.class, ServiceConfig.class})
public class AppConfig extends WebMvcConfigurerAdapter {

    @Autowired
    public Environment environment;
  
//	@Bean
//	public ResourceBundleViewResolver resourceBundleViewResolver() {
//		ResourceBundleViewResolver resolver = new ResourceBundleViewResolver();
//		resolver.setBasename("messages");
//		resolver.setOrder(1);
//		return resolver;
//	}

	@Bean
	public ViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("");
		viewResolver.setOrder(0);
		return viewResolver;
	}
	
	@Override
	 public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

		ObjectMapper skipNullMapper = new ObjectMapper();
		skipNullMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		skipNullMapper.setDateFormat(formatter);
		
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
	  
		converter.setObjectMapper(skipNullMapper);
	  
		converters.add(converter);
	 }
	
	@Override
	public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
	  exceptionResolvers.add(restJsonExceptionResolver());
	}

	@Bean
    public RestJsonExceptionResolver restJsonExceptionResolver() {
        RestJsonExceptionResolver bean = new RestJsonExceptionResolver();
        RestJsonExceptionResolver.registerExceptionWithHTTPCode(org.springframework.beans.TypeMismatchException.class, 400);
        RestJsonExceptionResolver.registerExceptionWithHTTPCode(MissingServletRequestParameterException.class, 400);
        bean.setOrder(100);
        return bean;
    }
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations("/static/**");
		registry.addResourceHandler("/app/**").addResourceLocations(
				"/static/app/");
		registry.addResourceHandler("/images/**").addResourceLocations(
				"/static/images/");
	}

//	@Bean
//	public MessageSource messageSource() {
//		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
//		messageSource.setBasename("messages");
//		return messageSource;
//	}

//	@Bean
//	public CommonsMultipartResolver multipartResolver() {
//		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
//		commonsMultipartResolver.setDefaultEncoding("utf-8");
//		commonsMultipartResolver.setMaxUploadSize(50000000);
//		return commonsMultipartResolver;
//
//	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	
	

}
