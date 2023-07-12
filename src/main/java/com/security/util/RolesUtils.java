package com.security.util;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.security.model.ERole;
import com.security.model.Role;
import com.security.repository.RoleRepository;

@Component
public class RolesUtils {

	@Autowired
	private RoleRepository repository;

	public void mapRoles(Set<String> userRoles, Set<Role> dbRoles) {

		if(userRoles == null || userRoles.isEmpty()) {
			Role userRole = repository.findByName(ERole.ROLE_USER)
					.orElseThrow(()->new RuntimeException("Error: Role is not found"));
			dbRoles.add(userRole);
		} else {
			userRoles.forEach(role->{
				switch (role) {
				case "ROLE_ADMIN":
					Role adminRole = repository.findByName(ERole.ROLE_ADMIN)
					.orElseThrow(()->new RuntimeException("Error: Role is not found"));
					dbRoles.add(adminRole);
					break;

				case "ROLE_HR":
					Role modRole = repository.findByName(ERole.ROLE_HR)
					.orElseThrow(()->new RuntimeException("Error: Role is not found"));
					dbRoles.add(modRole);
					break;

				default:
					Role userRole = repository.findByName(ERole.ROLE_USER)
					.orElseThrow(()->new RuntimeException("Error: Role is not found"));
					dbRoles.add(userRole);
					break;
				}
			});
		}

	}
}
