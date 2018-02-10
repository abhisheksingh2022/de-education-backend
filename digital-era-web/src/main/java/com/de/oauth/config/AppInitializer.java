package com.de.oauth.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

public class AppInitializer implements WebApplicationInitializer {
    @Override
	public void onStartup(ServletContext container) throws ServletException {
    	AnnotationConfigWebApplicationContext ctx = null;
    	try{
    		ctx = new AnnotationConfigWebApplicationContext();
    		ctx.register(SpringAnnotationWebInitializer.class);

    		ctx.setServletContext(container);
    	}finally{
    		if(ctx!=null)
    			ctx.close();
    	}
	}
}
