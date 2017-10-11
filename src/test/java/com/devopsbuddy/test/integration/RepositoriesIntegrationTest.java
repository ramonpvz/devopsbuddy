package com.devopsbuddy.test.integration;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.devopsbuddy.backend.persistence.domain.backend.Plan;
import com.devopsbuddy.backend.persistence.domain.backend.Role;
import com.devopsbuddy.backend.persistence.domain.backend.User;
import com.devopsbuddy.backend.persistence.domain.backend.UserRole;
import com.devopsbuddy.backend.persistence.repositories.PlanRepository;
import com.devopsbuddy.backend.persistence.repositories.RoleRepository;
import com.devopsbuddy.backend.persistence.repositories.UserRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT) //@SpringApplicationConfiguration (deprecated)
public class RepositoriesIntegrationTest {

	@Autowired
	private PlanRepository planRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	private final int BASIC_PLAN_ID = 1;
	private final int BASIC_ROLE_ID = 1;

	@Before
	public void init() {
		assertTrue("PlanRepository is null", planRepository != null);
		assertTrue("RoleRepository is null", roleRepository != null);
		assertTrue("UserRepository is null", userRepository != null);
	}
	
	@Test
	public void testCreateNewPlan() throws Exception {
		Plan basicPlan = createBasicPlan();
		planRepository.save(basicPlan);
		Plan retrievedPlan = planRepository.findOne(BASIC_PLAN_ID);
		assertTrue("Retrieved plan is null", retrievedPlan != null);
	}
	
	@Test
	public void testCreateNewRole() throws Exception {
		Role userRole = createBasicRole();
		roleRepository.save(userRole);
		Role retrievedRole = roleRepository.findOne(BASIC_ROLE_ID);
		assertTrue("Retrieved role is null", retrievedRole != null);
	}
	
	@Test
	public void createNewUser() throws Exception {
		
		Plan basicPlan = createBasicPlan();
		planRepository.save(basicPlan);
		
		User basicUser = createBasicUser();
		basicUser.setPlan(basicPlan);
		
		Role basicRole = createBasicRole();
		Set<UserRole> userRoles = new HashSet<>();
		UserRole userRole = new UserRole();
		userRole.setUser(basicUser);
		userRole.setRole(basicRole);
		userRoles.add(userRole);
		
		basicUser.getUserRoles().addAll(userRoles);
		
		for (UserRole ur: userRoles) {
			roleRepository.save(ur.getRole());
		}
		
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

	//--------------------------> Private methods

	private Plan createBasicPlan() {
		Plan plan = new Plan();
		plan.setId(BASIC_PLAN_ID);
		plan.setName("Basic");
		return plan;
	}

	private Role createBasicRole() {
		Role role = new Role();
		role.setId(BASIC_ROLE_ID);
		role.setName("ROLE_USER");
		return role;
	}

	private User createBasicUser() {
		User user = new User();
		user.setUsername("basicUser");
		user.setPassword("secret");
		user.setEmail("me@example.com");
		user.setFirstName("firstName");
		user.setLastName("lastName");
		user.setPhoneNumber("123456789");
		user.setCountry("GB");
		user.setEnabled(true);
		user.setDescription("A basic user");
		user.setProfileImageUrl("https://blabla.images.com/basicuser");
		return user;
	}

}
