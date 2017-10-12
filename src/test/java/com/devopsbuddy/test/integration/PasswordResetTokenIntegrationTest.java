package com.devopsbuddy.test.integration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.devopsbuddy.backend.persistence.domain.backend.PasswordResetToken;
import com.devopsbuddy.backend.persistence.domain.backend.User;
import com.devopsbuddy.backend.persistence.repositories.PasswordResetTokenRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT) //@SpringApplicationConfiguration (deprecated)
public class PasswordResetTokenIntegrationTest extends AbstractIntegrationTest {
	
	@Value("${token.expiration.length.minutes}")
	private int expirationTimeInMinutes;
	
	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;
	
	@Rule public TestName testName = new TestName();
	
	@Before
	public void init() { 
		assertFalse("", expirationTimeInMinutes == 0); 
	}
	
	@Test
	public void testTokenExpirationLength() throws Exception {
		
		User user = createUser(testName);
		assertNotNull("User is null", user != null);
		assertTrue("User id is invalid", user.getId() != 0);
		
		LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
		String token = UUID.randomUUID().toString();
		
		LocalDateTime expectedTime = now.plusMinutes(expirationTimeInMinutes);
		
		PasswordResetToken passwordResetToken = createPasswordResetToken(token, user, now);

		LocalDateTime actualTime = passwordResetToken.getExpiryDate();
		assertNotNull("Actual time is null", actualTime != null);
		assertTrue("Expected time is incorrect", expectedTime.equals(actualTime));

	}
	
	@Test
	public void testDeleteToken() throws Exception {
		
		User user = createUser(testName);
		
		String token = UUID.randomUUID().toString();
		
		LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
		
		PasswordResetToken passwordResetToken = createPasswordResetToken(token, user, now);
		long tokenId = passwordResetToken.getId();
		passwordResetTokenRepository.delete(tokenId);
		
		PasswordResetToken shouldNotExistToken = passwordResetTokenRepository.findOne(tokenId);
		assertTrue("Token should not exist !", shouldNotExistToken == null);
		
	}
	
	@Test
	public void testCascadeDeleteFromUserEntity() throws Exception {
		
		User user = createUser(testName);
		
		String token = UUID.randomUUID().toString();
		
		LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
		
		PasswordResetToken passwordResetToken = createPasswordResetToken(token, user, now);
		
		passwordResetToken.getId();
		
		userRepository.delete(user.getId());
		
		Set<PasswordResetToken> shouldBeEmpty = passwordResetTokenRepository.findAllByUserId(user.getId());
		
		assertTrue("Password reset token should be empty !" , shouldBeEmpty.isEmpty());
		
	}
	
	@Test
	public void testMultipleTokensAreReturnedQueringByUserId() throws Exception {
		
		User user = createUser(testName);
		
		LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
		
		String token1 = UUID.randomUUID().toString();
		String token2 = UUID.randomUUID().toString();
		String token3 = UUID.randomUUID().toString();
		
		Set<PasswordResetToken> tokens = new HashSet<>();
		tokens.add(createPasswordResetToken(token1, user, now));
        tokens.add(createPasswordResetToken(token2, user, now));
        tokens.add(createPasswordResetToken(token3, user, now));

        passwordResetTokenRepository.save(tokens);

        User founduser = userRepository.findOne(user.getId());

        Set<PasswordResetToken> actualTokens = passwordResetTokenRepository.findAllByUserId(founduser.getId());
        assertTrue(actualTokens.size() == tokens.size());
        List<String> tokensAsList = tokens.stream().map(prt -> prt.getToken()).collect(Collectors.toList());
        List<String> actualTokensAsList = actualTokens.stream().map(prt -> prt.getToken()).collect(Collectors.toList());
        assertTrue(tokensAsList.equals(actualTokensAsList));

	}

	//------------------> Private methods

    private PasswordResetToken createPasswordResetToken(String token, User user, LocalDateTime now) {
        PasswordResetToken passwordResetToken = new PasswordResetToken(token, user, now, expirationTimeInMinutes);
        passwordResetTokenRepository.save(passwordResetToken);
        assertNotNull("Password reset token id is null !", passwordResetToken.getId() != 0);
        return passwordResetToken;
    }

}
