package com.devopsbuddy.test.integration;

import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.devopsbuddy.backend.persistence.domain.backend.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT) //@SpringApplicationConfiguration (deprecated)
public class UserServiceIntegrationTest extends AbstractServiceIntegrationTest {

	@Rule public TestName testName = new TestName();
	
	@Test
	public void testCreateNewUser() throws Exception {
		
		User user = createUser(testName);
		
		assertTrue("User is null", user != null);
		assertTrue("Id is null", user.getId() != 0);

	}

}
