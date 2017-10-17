package com.devopsbuddy.test.unit;

import static org.junit.Assert.assertTrue;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import com.devopsbuddy.backend.persistence.domain.backend.User;
import com.devopsbuddy.utils.UserUtils;
import com.devopsbuddy.web.controllers.ForgotMyPasswordController;
import com.devopsbuddy.web.domain.frontend.BasicAccountPayload;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

public class UserUtilUnitTest {
	
	private MockHttpServletRequest mockHttpServletRequest;
	
	private PodamFactory podamFactory;
	
	@Before
	public void init() {
		mockHttpServletRequest = new MockHttpServletRequest();
		podamFactory = new PodamFactoryImpl();
	}

	@Test
	public void testPasswordResetEmailUrlConstruction() throws Exception {
		
		mockHttpServletRequest.setServerPort(8080); //Default is 80
		
		String token = UUID.randomUUID().toString();
		
		long userId = 123456;
		
		String expectedUrl = "http://localhost:8080" + ForgotMyPasswordController.CHANGE_PASSWORD_PATH + "?id=" + userId + "&token=" + token;
		
		String actualUrl = UserUtils.createPasswordResetUrl(mockHttpServletRequest, userId, token);
		
		assertTrue("Url is incorrect", expectedUrl.equals(actualUrl));
		
	}
	
	@Test
	public void mapWebUserToDomainUser() {

		BasicAccountPayload webUser = podamFactory.manufacturePojo(BasicAccountPayload.class, String.class);
		webUser.setEmail("acustiko@gmail.com");

		User user = UserUtils.fromWebUserToDomainUser(webUser);
		
		assertTrue("User is null", user != null);
		assertTrue("Username is incorrect", webUser.getUsername().equals(user.getUsername()));
		assertTrue("Password is incorrect", webUser.getPassword().equals(user.getPassword()));
		assertTrue("FirstName is incorrect", webUser.getFirstName().equals(user.getFirstName()));
		assertTrue("LastName is incorrect", webUser.getLastName().equals(user.getLastName()));
		assertTrue("Email is incorrect", webUser.getEmail().equals(user.getEmail()));
		assertTrue("PhoneNumber is incorrect", webUser.getPhoneNumber().equals(user.getPhoneNumber()));
		assertTrue("Country is incorrect", webUser.getCountry().equals(user.getCountry()));
		assertTrue("Description is incorrect", webUser.getDescription().equals(user.getDescription()));

	}

}
