package com.devopsbuddy.backend.service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devopsbuddy.backend.persistence.domain.backend.PasswordResetToken;
import com.devopsbuddy.backend.persistence.domain.backend.User;
import com.devopsbuddy.backend.persistence.repositories.PasswordResetTokenRepository;
import com.devopsbuddy.backend.persistence.repositories.UserRepository;

@Service
@Transactional(readOnly=true)
public class PasswordResetTokenService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;

	@Value("${token.expiration.length.minutes}")
	private int tokenExpirationInMinutes;
	
	/** The application logger **/
	private static final Logger LOG = LoggerFactory.getLogger(PasswordResetTokenService.class);
	
	/**
	 * Retrieves a Password Reset Token for the given token id.
	 * @param token The token to be returned
	 * @return A Password Reset Token if one was found or null if none was found.
	 */
	public PasswordResetToken findByToken(String token) {return passwordResetTokenRepository.findByToken(token);}
	
	@Transactional
	public PasswordResetToken createPasswordResetTokenForEmail(String email) {
		
		PasswordResetToken passwordResetToken = null;
		
		User user = userRepository.findByEmail(email);
		
		if (null != user) {

			String token = UUID.randomUUID().toString();
			
			LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
			
			passwordResetToken = new PasswordResetToken(token, user, now, tokenExpirationInMinutes);
			
			passwordResetToken = passwordResetTokenRepository.save(passwordResetToken);
			
			LOG.debug("Successfully created token {} for user {}" , token, user.getUsername());

		} else {
			
			LOG.warn("We couldn't find a user for the given email {}", email);
			
		}
		
		return passwordResetToken;

	}
	
}
