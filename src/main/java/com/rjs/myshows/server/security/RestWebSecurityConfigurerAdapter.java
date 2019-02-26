package com.rjs.myshows.server.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class RestWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
	private RestAuthenticationEntryPoint restAuthenticationEntryPoint;
	private BCryptPasswordEncoder passwordEncoder;

	public RestWebSecurityConfigurerAdapter(RestAuthenticationEntryPoint restAuthenticationEntryPoint, BCryptPasswordEncoder passwordEncoder) {
		this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
		this.passwordEncoder = passwordEncoder;
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder builder) throws Exception {
		builder.inMemoryAuthentication()
			.withUser("admin").password(passwordEncoder.encode("Noah8SpenceD3!")).authorities("ROLE_USER", "ROLE_ADMIN")
			.and()
			.withUser("restUser").password(passwordEncoder.encode("1971Duster")).authorities("ROLE_USER");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/admin/**").access("hasRole('ADMIN')")
			.antMatchers("/**").access("hasRole('USER')")
//			.antMatchers("/*").permitAll()
			.anyRequest().authenticated()
			.and()
			.csrf().disable()
			.httpBasic()
			.authenticationEntryPoint(restAuthenticationEntryPoint);

		http.addFilterAfter(new CustomFilter(), BasicAuthenticationFilter.class);
	}
}
