package com.rjs.myshows.server.security;

import java.util.ArrayList;
import java.util.List;

//@Service
//public class UserDetailsServiceImpl implements UserDetailsService {
public class UserDetailsServiceImpl {
	private final List<AppUser> users = new ArrayList<>();

/*
	public UserDetailsServiceImpl(BCryptPasswordEncoder passwordEncoder) {
		AppUser user = new AppUser("restUser", passwordEncoder.encode("1971Duster"), "USER");
		user.grantedAuthorities.addAll(AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER"));
		users.add(user);

		user = new AppUser("admin", passwordEncoder.encode("Noah8SpenceD3!"), "ADMIN");
		user.grantedAuthorities.addAll(AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ADMIN"));
		users.add(user);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		for (AppUser  user : users) {
			if (user.username.equals(username)) {
				return new User(user.username, user.pw, user.grantedAuthorities);
			}
		}

		throw new UsernameNotFoundException(String.format("Username %s not found.", username));
	}
*/
}
