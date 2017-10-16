package com.devopsbuddy.test.integration;

import static org.junit.Assert.assertTrue;

import java.util.Set;
import java.util.UUID;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.devopsbuddy.backend.persistence.domain.backend.Plan;
import com.devopsbuddy.backend.persistence.domain.backend.Role;
import com.devopsbuddy.backend.persistence.domain.backend.User;
import com.devopsbuddy.backend.persistence.domain.backend.UserRole;
import com.devopsbuddy.enums.PlanEnum;
import com.devopsbuddy.enums.RolesEnum;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT) //@SpringApplicationConfiguration (deprecated)
public class UserRepositoryIntegrationTest extends AbstractIntegrationTest {
	
	@Rule public TestName testName = new TestName();

	@Before
	public void init() {
		assertTrue("PlanRepository is null", planRepository != null);
		assertTrue("RoleRepository is null", roleRepository != null);
		assertTrue("UserRepository is null", userRepository != null);
	}
	
	@Test
	public void testCreateNewPlan() throws Exception {
		Plan basicPlan = createPlan(PlanEnum.BASIC);
		planRepository.save(basicPlan);
		Plan retrievedPlan = planRepository.findOne(PlanEnum.BASIC.getId());
		assertTrue("Retrieved plan is null", retrievedPlan != null);
	}
	
	@Test
	public void testCreateNewRole() throws Exception {
		Role userRole = createRole(RolesEnum.BASIC);
		roleRepository.save(userRole);
		Role retrievedRole = roleRepository.findOne(RolesEnum.BASIC.getId());
		assertTrue("Retrieved role is null", retrievedRole != null);
	}

	@Test
	public void createNewUser() throws Exception {

		String username = testName.getMethodName();
		String email = testName.getMethodName() + "@devopsbuddy.com";

		User basicUser = createUser(username, email);

		basicUser = userRepository.save(basicUser);
		User newlyCreatedUser = userRepository.findOne(basicUser.getId());
		
		assertTrue("Newly created user is null", newlyCreatedUser != null);
		assertTrue("Newly created user is invalid", newlyCreatedUser.getId() != 0);
		assertTrue("Newly created user plan", newlyCreatedUser.getPlan() != null);
		assertTrue("Newly created user plan id", newlyCreatedUser.getPlan().getId() != 0);
		Set<UserRole> newlyCreatedUserUserRoles = newlyCreatedUser.getUserRoles();
		
		for (UserRole ur: newlyCreatedUserUserRoles) {
			assertTrue("Role is null", ur.getRole() != null);
			assertTrue("Role id is null", ur.getRole().getId() != 0);
		}

	}

	@Test
	public void testDeleteUser() throws Exception {

		String username = testName.getMethodName();
		String email = testName.getMethodName() + "@devopsbuddy.com";

		User basicUser = createUser(username, email);
		userRepository.delete(basicUser.getId());

	}

	@Test
	public void testGetUserByEmail() throws Exception {

		User user = createUser(testName);

		User newlyFoundUser = userRepository.findByEmail(user.getEmail());
		
		assertTrue("Newly found user does not exist", newlyFoundUser != null);

		assertTrue("Newly found user is invalid" , newlyFoundUser.getId() > 0);

	}
	
	@Test
	public void testUpdateUserPassword() throws Exception {

		User user = createUser(testName);
		assertTrue("User is null", user != null);
		assertTrue("User id is invalid", user.getId() > 0);
		
		String newPassword = UUID.randomUUID().toString();
		
		userRepository.updateUserPassword(user.getId(), newPassword);
		
		user = userRepository.findOne(user.getId());

		assertTrue("Password is incorrect", newPassword.equals(user.getPassword()));

	}

}
