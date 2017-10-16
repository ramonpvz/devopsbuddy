package com.devopsbuddy.utils;

import javax.servlet.http.HttpServletRequest;

import org.springframework.mock.web.MockHttpServletRequest;

import com.devopsbuddy.backend.persistence.domain.backend.User;
import com.devopsbuddy.web.controllers.ForgotMyPasswordController;

public class UserUtils {
	
	private UserUtils() {
		throw new AssertionError("Non instantiable");
	}
	
	/**
	 * Creates a user with basic attributes set.
	 * @param username The username
	 * @param email The email
	 * @return A user entity
	 */
	public static User createBasicUser(String username, String email) {
		User user = new User();
		user.setUsername(username);
		user.setPassword("secret");
		user.setEmail(email);
		user.setFirstName("firstName");
		user.setLastName("lastName");
		user.setPhoneNumber("123456789");
		user.setCountry("GB");
		user.setEnabled(true);
		user.setDescription("A basic user");
		user.setProfileImageUrl("https://blabla.images.com/basicuser");
		return user;
	}

	public static String createPasswordResetUrl(HttpServletRequest request, long userId,
			String token) {
		
		String passwordResetUrl = 
				request.getScheme() + 
				"://" + 
				request.getServerName() + 
				":" + 
				request.getServerPort() + 
				request.getContextPath() + 
				ForgotMyPasswordController.CHANGE_PASSWORD_PATH + 
				"?id=" + 
				userId +
				"&token=" + 
				token;
		
		return passwordResetUrl;

	}

}
