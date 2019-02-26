package com.rjs.myshows.server.security;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AppUser {
	public final String username;
	public final String pw;
	public final String role;
//	public final List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
}
