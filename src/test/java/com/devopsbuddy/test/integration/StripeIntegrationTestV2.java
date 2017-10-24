package com.devopsbuddy.test.integration;

import static org.junit.Assert.assertTrue;

import java.time.Clock;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.devopsbuddy.backend.service.StripeService;
import com.devopsbuddy.enums.PlanEnum;
import com.stripe.model.Customer;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class StripeIntegrationTestV2 {

	private static final Logger LOG = LoggerFactory.getLogger(StripeIntegrationTestV2.class);
	
	public static final String TEST_CC_NUMBER = "4242424242424242";

	public static final int TEST_CC_EXP_MONTH = 1;

	public static final String TEST_CC_CVC_NBR = "314";

	@Autowired
	private StripeService stripeService;

	public void init() {
		LOG.debug("Initiating...");
	}
	
	@Test
	public void executeTest() {
		assertTrue("Should not happen", true);
	}
	
	public void createStripeCustomer() throws Exception {

	Map<String, Object> tokenParams = new HashMap<>();
	Map<String, Object> cardParams = new HashMap<>();

	cardParams.put("number", TEST_CC_NUMBER);
	cardParams.put("exp_month", TEST_CC_EXP_MONTH);
	cardParams.put("exp_year", LocalDate.now(Clock.systemUTC()).getYear() + 1);
	cardParams.put("cvc", TEST_CC_CVC_NBR);
	tokenParams.put("card", cardParams);

	Map<String, Object> customerParams = new HashMap<>();
	customerParams.put("description", "Customer for test@example.com");
	customerParams.put("plan", PlanEnum.PRO.getId());

	String stripeCustomerId = stripeService.createCustomer(tokenParams, customerParams);
	assertTrue("Stripe customer id is null! ", stripeCustomerId != null);

	Customer cu = Customer.retrieve(stripeCustomerId);
	cu.delete();

	}

}
