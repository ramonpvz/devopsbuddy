package com.devopsbuddy.test.integration;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
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
import com.devopsbuddy.enums.PlanEnum;
import com.devopsbuddy.enums.RolesEnum;
import com.devopsbuddy.utils.UserUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT) //@SpringApplicationConfiguration (deprecated)
public class RepositoriesIntegrationTest {

	@Autowired
	private PlanRepository planRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private UserRepository userRepository;
	
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

		/*Plan basicPlan = createPlan(PlanEnum.BASIC);
		planRepository.save(basicPlan);

		User basicUser = UserUtils.createBasicUser();
		basicUser.setPlan(basicPlan);

		Role basicRole = createRole(RolesEnum.BASIC);
		Set<UserRole> userRoles = new HashSet<>();
		UserRole userRole = new UserRole(basicUser, basicRole);
		userRoles.add(userRole);

		basicUser.getUserRoles().addAll(userRoles);

		for (UserRole ur: userRoles) {
			roleRepository.save(ur.getRole());
		}*/

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

	//--------------------------> Private methods

	private Plan createPlan(PlanEnum planEnum) {
		return new Plan(planEnum);
	}

	private Role createRole(RolesEnum rolesEnum) {
		return new Role(rolesEnum);
	}

	private User createUser(String username, String email) {

		Plan basicPlan = createPlan(PlanEnum.BASIC);
		planRepository.save(basicPlan);
		
		User basicUser = UserUtils.createBasicUser(username, email);
		basicUser.setPlan(basicPlan);
		
		Role basicRole = createRole(RolesEnum.BASIC);
		roleRepository.save(basicRole);

		Set<UserRole> userRoles = new HashSet<>();
		UserRole userRole = new UserRole(basicUser, basicRole);
		userRoles.add(userRole);

		basicUser.getUserRoles().addAll(userRoles);
		basicUser = userRepository.save(basicUser);

		return basicUser;

	}

}
