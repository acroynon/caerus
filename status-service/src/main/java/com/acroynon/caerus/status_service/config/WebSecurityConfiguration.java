package com.acroynon.caerus.status_service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import com.acroynon.caerus.security_module.config.WebSecurityConfigurationBase;
import com.acroynon.caerus.security_module.util.JwtUtil;


@Configuration @EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurationBase {

	@Autowired
	public WebSecurityConfiguration(JwtUtil jwtUtil) {
		super(jwtUtil);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		super.configure(http);
		
		http.authorizeRequests().antMatchers("/status").hasRole("USER");
	}
	
}
