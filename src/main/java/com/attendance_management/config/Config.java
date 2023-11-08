package com.attendance_management.config;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.attendance_management.service.HrService;

@Configuration
@ComponentScan(basePackages = "com")
public class Config {

	private static EntityManagerFactory emf;

	@Bean("serviceBean")
	public HrService getService() {
		return new HrService();
	}

	@Bean("emf")
	public EntityManagerFactory getEMF() {
		if (emf == null)
			emf = Persistence.createEntityManagerFactory("attendance_management");
		return emf;
	}

	@Bean("em")
	public EntityManager getEM() {
		return getEMF().createEntityManager();
	}

	@Bean
	public MultipartResolver multipartResolver() {
		CommonsMultipartResolver resolver = new CommonsMultipartResolver();
		resolver.setDefaultEncoding("utf-8");
		return resolver;
	}
	
	@Bean("emptyList")
	public List<?> getList(){
		return new ArrayList<>();
	}

}
