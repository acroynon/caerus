package com.acroynon.caerus.security_module.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.acroynon.caerus.security_module.filter.CustomAuthorizationFilter;
import com.acroynon.caerus.security_module.util.JwtUtil;
 
public class WebSecurityConfigurationBase extends WebSecurityConfigurerAdapter {

	private final JwtUtil jwtUtil;
	
	public WebSecurityConfigurationBase(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		configure(http, new ArrayList<>());
	}
	
	protected void configure(HttpSecurity http, List<String> unauthorisedPaths) throws Exception {
		http.csrf().disable();
		http.httpBasic().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.addFilterBefore(new CustomAuthorizationFilter(unauthorisedPaths, jwtUtil), UsernamePasswordAuthenticationFilter.class);
	}
	
}
