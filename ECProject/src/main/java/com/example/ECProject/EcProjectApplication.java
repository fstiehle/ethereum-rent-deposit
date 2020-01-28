package com.example.ECProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@SpringBootApplication
public class EcProjectApplication extends WebMvcConfigurationSupport{

	public static void main(String[] args) {
		SpringApplication.run(EcProjectApplication.class, args);
	}

	protected void addResourceHandlers(ResourceHandlerRegistry registry){
		registry.addResourceHandler("/**").addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX+"/templates/");
		registry.addResourceHandler("/**").addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX+"/static/");
		registry.addResourceHandler("/imgs/**").addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX+"/imgs/");
		super.addResourceHandlers(registry);
	}


	
}
