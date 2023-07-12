package com.security.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.security.model.ERole;
import com.security.model.Role;
import com.security.repository.RoleRepository;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SetupRunner implements CommandLineRunner {

	@Autowired
	private RoleRepository repository;
	
	public void run(String... args) throws Exception {
		if(!repository.findByName(ERole.ROLE_USER).isPresent()) {
			Role r = new Role();
			r.setName(ERole.ROLE_USER);
			repository.save(r);
		}
		if(!repository.findByName(ERole.ROLE_ADMIN).isPresent()) {
			Role r = new Role();
			r.setName(ERole.ROLE_ADMIN);
			repository.save(r);
		}
		if(!repository.findByName(ERole.ROLE_HR).isPresent()) {
			Role r = new Role();
			r.setName(ERole.ROLE_HR);
			repository.save(r);
		}
	}
}
