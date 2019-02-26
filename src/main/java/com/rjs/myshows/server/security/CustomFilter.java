package com.rjs.myshows.server.security;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

import org.springframework.web.filter.GenericFilterBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomFilter extends GenericFilterBean {
	private final Logger logger = LoggerFactory.getLogger(CustomFilter.class);

	public CustomFilter() {
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//		String auth = ((HttpServletRequest) servletRequest).getHeader("Authorization");
//		logger.info(String.format("Auth header: %s", auth));

		filterChain.doFilter(servletRequest, servletResponse);
	}
}
