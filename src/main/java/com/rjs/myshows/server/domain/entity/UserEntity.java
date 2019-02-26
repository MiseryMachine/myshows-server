package com.rjs.myshows.server.domain.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.rjs.myshows.domain.User;
import com.rjs.myshows.domain.security.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@Document(collection = "user")
@Entity
@Table(name = "user")
public class UserEntity extends JpaBaseEntity implements User {
	@Column(name = "username", length = 40, unique = true, nullable = false)
	private String username;
	@Column(name = "email", length = 80)
	private String email;
	@Column(name = "first_name", length = 40)
	private String firstName;
	@Column(name = "last_name", length = 40)
	private String lastName;
	@Column(name = "enabled", nullable = false)
	private boolean enabled = false;
	@Column(name = "dob")
	private LocalDate dateOfBirth;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
	@Enumerated(value = EnumType.STRING)
	@Column(name = "role")
	private Set<Role> roles = new HashSet<>();

	public UserEntity() {
	}

	public void grantRole(Role role) {
		if (role != null) {
			roles.add(role);
		}
	}

	public void revokeRole(Role role) {
		if (role != null) {
			roles.remove(role);
		}
	}
}
