package com.devopsbuddy.web.controllers;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.devopsbuddy.backend.persistence.domain.backend.PasswordResetToken;
import com.devopsbuddy.backend.persistence.domain.backend.User;
import com.devopsbuddy.backend.service.EmailService;
import com.devopsbuddy.backend.service.I18NService;
import com.devopsbuddy.backend.service.PasswordResetTokenService;
import com.devopsbuddy.utils.UserUtils;

@Controller
public class ForgotMyPasswordController {
	
	private static final Logger LOG = LoggerFactory.getLogger(ForgotMyPasswordController.class);
	
	public static final String EMAIL_ADDRESS_VIEW_NAME = "forgotmypassword/emailForm";

	public static final String FORGOT_PASSWORD_URL_MAPPING = "/forgotmypassword";

	public static final String MAIL_SENT_KEY = "mailSent";

	public static final String CHANGE_PASSWORD_PATH = "/changeuserpassword";

	public static final String EMAIL_MESSAGE_TEXT_PROPERTY_NAME = "forgotmypassword.email.text";
	
	public static final String CHANGE_PASSWORD_VIEW_NAME = "forgotmypassword/changePassword";
	
	@Autowired
	private I18NService i18NService;

	@Autowired
	private EmailService emailService;

	@Value("${webmaster.email}")
	private String webmasterEmail;

	@Autowired
	private PasswordResetTokenService passwordResetTokenService;

	@RequestMapping(value = FORGOT_PASSWORD_URL_MAPPING, method=RequestMethod.GET)
	public String forgotPasswordGet() {
		LOG.debug("forgotPasswordGet");
		return EMAIL_ADDRESS_VIEW_NAME;
	}

	@RequestMapping(value = FORGOT_PASSWORD_URL_MAPPING, method = RequestMethod.POST)
	public String forgotPasswordPost(HttpServletRequest request, 
								@RequestParam("email") String email, 
								ModelMap model) {
		
		PasswordResetToken passwordResetToken = passwordResetTokenService.createPasswordResetTokenForEmail(email);
		
		if (null == passwordResetToken) {
			LOG.warn("Couldn't find a password reset token for email {}", email);
		} else {
			
			User user = passwordResetToken.getUser();
			String token = passwordResetToken.getToken();
			
			String resetPasswordUrl = UserUtils.createPasswordResetUrl(request, user.getId(), token);
			
			LOG.debug("Reset Password URL {}", resetPasswordUrl);
			
			String emailText = i18NService.getMessage(EMAIL_MESSAGE_TEXT_PROPERTY_NAME, request.getLocale());
			
			SimpleMailMessage mailMessage = new SimpleMailMessage();
			mailMessage.setTo(user.getEmail());
			mailMessage.setSubject("[Devopsbuddy]: How to Reset Your Password");
			mailMessage.setText(emailText + "\r\n" + resetPasswordUrl);
			mailMessage.setFrom(webmasterEmail);

			emailService.sendGenericEmailMessage(mailMessage);

		}

		return EMAIL_ADDRESS_VIEW_NAME;

	}
	
	@RequestMapping(value = CHANGE_PASSWORD_PATH, method = RequestMethod.GET)
	public String changeUserPasswordGet(@RequestParam("id") long id,
										@RequestParam("token") String token,
										Locale locale,
										ModelMap model) {
		return CHANGE_PASSWORD_VIEW_NAME;
	}

}
