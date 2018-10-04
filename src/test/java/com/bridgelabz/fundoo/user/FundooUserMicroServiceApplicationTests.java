package com.bridgelabz.fundoo.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParseException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.bridgelabz.fundoo.user.models.User;
import com.bridgelabz.fundoo.user.repositories.UserRepository;
import com.bridgelabz.fundoo.user.repositories.UserRepositoryES;
import com.bridgelabz.fundoo.user.services.UserServiceImplementation;
import com.bridgelabz.fundoo.user.testcasesdemo.Json;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FundooUserMicroServiceApplicationTests {

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext wac;

	private ObjectMapper objectMapper = new ObjectMapper();

	private Resource casesFile;

	private Map<String, Json> cases;
	
	@Mock
	private UserRepository userRepository;

	@Mock
	private UserRepositoryES userRepositoryES;
	
	@InjectMocks
	private UserServiceImplementation userServiceImplementation;

	@Before
	public void setup() throws JsonParseException, JsonMappingException, IOException {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
		casesFile = new ClassPathResource("testcases.json");

		cases = objectMapper.readValue(casesFile.getInputStream(), new TypeReference<Map<String, Json>>() {
		});
	}

	private void test(Json json) throws JsonProcessingException, Exception {
		
        when(userRepository.save(any(User.class))).thenReturn(new User());
        
        when(userRepository.insert(any(User.class))).thenReturn(new User());
        
        when(userRepositoryES.save(any(User.class))).thenReturn(new User());
        
		ResultActions actions = mockMvc
				.perform(getMethod(json).headers(json.getRequest().getHeaders()).contentType(MediaType.APPLICATION_JSON)
						.content(getRequestBody(json)).accept(MediaType.APPLICATION_JSON));

		actions.andExpect(status().is(json.getResponse().getStatus().value()));

		MockHttpServletResponse response = actions.andReturn().getResponse();

		for (String key : json.getResponse().getHeaders().keySet()) {
			assertEquals(json.getResponse().getHeaders().get(key), response.getHeader(key));
		}

		assertEquals(getResponseBody(json), response.getContentAsString());
	}

	private void testLogin(Json json) throws JsonProcessingException, Exception {
		ResultActions actions = mockMvc
				.perform(getMethod(json).headers(json.getRequest().getHeaders()).contentType(MediaType.APPLICATION_JSON)
						.content(getRequestBody(json)).accept(MediaType.APPLICATION_JSON));

		actions.andExpect(status().is(json.getResponse().getStatus().value()));

		MockHttpServletResponse response = actions.andReturn().getResponse();

		assertNotNull(json.getResponse().getHeaders().get("token"));
		
		assertEquals(getResponseBody(json), response.getContentAsString());
	}

	private MockHttpServletRequestBuilder getMethod(Json json) {

		return MockMvcRequestBuilders.request(HttpMethod.resolve(json.getRequest().getMethod()),
				json.getRequest().getUrl());
	}

	private String getRequestBody(Json json) throws JsonProcessingException {
		return objectMapper.writeValueAsString(json.getRequest().getBody());

	}

	private String getResponseBody(Json json) throws JsonProcessingException {
		return objectMapper.writeValueAsString(json.getResponse().getBody());

	}

	@Test
	public void test1() throws JsonProcessingException, Exception {
		Json json = cases.get("successfulRegistration");
		test(json);
	}

	@Test
	public void test2() throws JsonProcessingException, Exception {
		Json json = cases.get("registrationWithWrongEmailIdSyntax");
		test(json);
	}

	@Test
	public void test3() throws JsonProcessingException, Exception {
		Json json = cases.get("registrationWithWrongContactNumSyntax");
		test(json);
	}

	@Test
	public void test4() throws JsonProcessingException, Exception {
		Json json = cases.get("registrationWithEmptyNameField");
		test(json);
	}
	
	@Test
	public void test5() throws JsonProcessingException, Exception {
		Json json = cases.get("registrationWithEmptyEmailField");
		test(json);
	}
	
	@Test
	public void test6() throws JsonProcessingException, Exception {
		Json json = cases.get("registrationWithEmptyPasswordField");
		test(json);
	}
	
	@Test
	public void test7() throws JsonProcessingException, Exception {
		Json json = cases.get("registrationWithEmptyConfirmPasswordField");
		test(json);
	}
	
	@Test
	public void test8() throws JsonProcessingException, Exception {
		Json json = cases.get("registrationWithUnequalPasswordFields");
		test(json);
	}
	
	@Test
	public void test9() throws JsonProcessingException, Exception {
		Json json = cases.get("registrationWithLessPasswordCharecter");
		test(json);
	}
	
	@Test
	public void test10() throws JsonProcessingException, Exception {
		Json json = cases.get("registrationWithMorePasswordCharecter");
		test(json);
	}
	
	@Test
	public void test11() throws JsonProcessingException, Exception {
		Json json = cases.get("reRegistrationWithExistingEmail");
		test(json);
	}
	
	@Test
	public void test12() throws JsonProcessingException, Exception {
		Json json = cases.get("registrationWithNumericPassword");
		test(json);
	}
	
	@Test
	public void test13() throws JsonProcessingException, Exception {
		Json json = cases.get("registrationWithAlphaNumericPhoneField");
		test(json);
	}
	
	@Test
	public void test14() throws JsonProcessingException, Exception {
		Json json = cases.get("registrationWithLessCharInName");
		test(json);
	}
	
	@Test
	public void test15() throws JsonProcessingException, Exception {
		Json json = cases.get("registrationWithGreaterCharInName");
		test(json);
	}
	
	@Test
	public void test16() throws JsonProcessingException, Exception {
		Json json = cases.get("successfulLogin");
		testLogin(json);
	}

	@Test
	public void test17() throws JsonProcessingException, Exception {
		Json json = cases.get("unavailableLoginCredential");
		test(json);
	}
	
	@Test
	public void test18() throws JsonProcessingException, Exception {
		Json json = cases.get("loginWithWrongPassword");
		test(json);
	}
	
	@Test
	public void test19() throws JsonProcessingException, Exception {
		Json json = cases.get("loginWithEmptyEmailField");
		test(json);
	}
	
	@Test
	public void test20() throws JsonProcessingException, Exception {
		Json json = cases.get("loginWithEmptyEmailAndPasswordField");
		test(json);
	}
	
}
