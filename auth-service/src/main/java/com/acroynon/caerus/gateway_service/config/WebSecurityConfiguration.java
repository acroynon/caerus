package com.acroynon.caerus.gateway_service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.acroynon.caerus.gateway_service.filter.CustomAuthorizationFilter;
import com.acroynon.caerus.gateway_service.util.JwtUtil;
import com.acroynon.caerus.security_module.config.WebSecurityConfigurationBase;

@Configuration @EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurationBase {

	private final UserDetailsService userDetailsService;
	private final PasswordEncoder passwordEncoder;
	
	@Autowired
	public WebSecurityConfiguration(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
		super(jwtUtil);
		this.userDetailsService = userDetailsService;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		super.configure(http);
		
		http.authorizeRequests().antMatchers("/authenticate", "/register", "/refresh").anonymous();

		// TODO: testing end-points, should be removed
		http.authorizeRequests().antMatchers("/any").permitAll()
		.and().authorizeRequests().antMatchers("/user", "/status").hasRole("USER")
		.and().authorizeRequests().antMatchers("/admin").hasRole("ADMIN");

		http.authorizeRequests().anyRequest().anonymous();
	}
	
	@Bean @Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
}
