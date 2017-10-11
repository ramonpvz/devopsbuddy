package com.devopsbuddy.test.integration;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.devopsbuddy.backend.persistence.domain.backend.Role;
import com.devopsbuddy.backend.persistence.domain.backend.User;
import com.devopsbuddy.backend.persistence.domain.backend.UserRole;
import com.devopsbuddy.backend.service.UserService;
import com.devopsbuddy.enums.PlanEnum;
import com.devopsbuddy.enums.RolesEnum;
import com.devopsbuddy.utils.UserUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT) //@SpringApplicationConfiguration (deprecated)
public class UserServiceIntegrationTest {

	@Autowired
	private UserService userService;
	
	@Test
	public void testCreateNewUser() throws Exception {
		
		Set<UserRole> userRoles = new HashSet<>();
		User basicUser = UserUtils.createBasicUser();
		userRoles.add(new UserRole(basicUser, new Role(RolesEnum.BASIC)));
		
		User user = userService.createUser(basicUser, PlanEnum.BASIC, userRoles);
		
		assertTrue("User is null", user != null);
		assertTrue("Id is null", user.getId() != 0 );

	}

}
