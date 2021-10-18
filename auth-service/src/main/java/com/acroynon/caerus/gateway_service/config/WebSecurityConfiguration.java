package com.acroynon.caerus.gateway_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.acroynon.caerus.gateway_service.filter.CustomAuthenticationFilter;
import com.acroynon.caerus.gateway_service.filter.CustomAuthorizationFilter;
import com.acroynon.caerus.gateway_service.service.UserService;
import com.acroynon.caerus.gateway_service.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@Configuration @EnableWebSecurity @RequiredArgsConstructor
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	private final UserDetailsService userDetailsService;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		http.authorizeRequests().antMatchers("/any").permitAll()
		.and().authorizeRequests().antMatchers("/login").anonymous()
		.and().authorizeRequests().antMatchers("/user", "/status").hasRole("USER")
		.and().authorizeRequests().antMatchers("/admin").hasRole("ADMIN");
		
		http.addFilterBefore(new CustomAuthorizationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);
		http.addFilter(new CustomAuthenticationFilter(authenticationManagerBean(), jwtUtil, (UserService) userDetailsService, passwordEncoder));
	}
	
	@Bean @Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

}
