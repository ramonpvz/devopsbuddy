package com.devopsbuddy.test.integration;

import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.devopsbuddy.backend.persistence.domain.backend.PasswordResetToken;
import com.devopsbuddy.backend.persistence.domain.backend.User;
import com.devopsbuddy.backend.service.PasswordResetTokenService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT) //@SpringApplicationConfiguration (deprecated)
public class PasswordResetTokenServiceIntegrationTest extends AbstractServiceIntegrationTest {

	@Autowired
	private PasswordResetTokenService passwordResetTokenService;

	@Rule public TestName testName = new TestName();

	@Test
	public void testCreateNewTokenForUserEmail() throws Exception {

		User user = createUser(testName);
		
		PasswordResetToken passwordResetToken = passwordResetTokenService.createPasswordResetTokenForEmail(user.getEmail());
		
		assertTrue("1) Password reset token is null" , passwordResetToken != null);
		
		assertTrue("2) Password reset token is null" , passwordResetToken.getToken() != null);
		
	}
	
	@Test
	public void testFindByToken() throws Exception {
		
		User user = createUser(testName);

		PasswordResetToken passwordResetToken = passwordResetTokenService.createPasswordResetTokenForEmail(user.getEmail());
		
		assertTrue("1) Password reset token is null" , passwordResetToken != null);
		
		assertTrue("2) Password reset token is null" , passwordResetToken.getToken() != null);

		PasswordResetToken token = passwordResetTokenService.findByToken(passwordResetToken.getToken());

		assertTrue("Token is null !" , token != null);

	}
	
}
