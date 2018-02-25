package neu.csye6225;

import neu.csye6225.controller.UserInfoController;
import neu.csye6225.entity.UserInfo;
import neu.csye6225.service.UserInfoService;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

//@RunWith(SpringRunner.class)
//@SpringBootTest
//@WebAppConfiguration
//@AutoConfigureMockMvc

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MockServletContext.class)
@WebAppConfiguration
public class MyApplicationTest {

	private MockMvc mockMvc;

	@Mock
	private UserInfoService userService;

	@InjectMocks
	private UserInfoController userController;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
	}


	@Test
	public void testUserInfo() {

		//Create a UserInfo object which is to be tested
		String username = "root123@163.com";
		String password = "root1234";
		UserInfo userInfo = new UserInfo(username, password);


		//Create the mock object of stock service
		UserInfo userInfoMock = mock(UserInfo.class);

		// mock the behavior of stock service to return the value of various stocks
		when(userInfoMock.getUsername()).thenReturn("root123@163.com");
		when(userInfoMock.getPassword()).thenReturn(password);


	}

	@Test
	public void testUserController() throws Exception {


		RequestBuilder request = null;
/*
		request = get("/login");
		mockMvc.perform(request)
				.andExpect(MockMvcResultMatchers.status().isOk());

		request = get("/signup");
		mockMvc.perform(request)
				.andExpect(MockMvcResultMatchers.status().isOk());
*/
		request = post("/login-check")
				.param("id", "1")
				.param("username", "root123@163.com")
				.param("password", "1234567!");
		mockMvc.perform(request)
				.andExpect(MockMvcResultMatchers.status().isOk());

		request = post("/signup-check")
				.param("username", "root123@163.com")
				.param("password", "1234567!");
		mockMvc.perform(request)
				.andExpect(MockMvcResultMatchers.status().isOk());

	}

	@Test
	public void test_UserController_save_return_success() throws Exception {
		//mock
		//Mockito.doNothing().when(userService).save(Mockito.any());

		//build request
		RequestBuilder request = MockMvcRequestBuilders.get("/");

		//assertion
		mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk());

	}

	/*@Test
	public void test_UserController_save_return_start_error() throws Exception {
		//mock
		Mockito.doThrow(new IllegalArgumentException("user error !")).when(userService).save(Mockito.any());

		//build request
		RequestBuilder request = MockMvcRequestBuilders.post("/index");

		//assertion
		mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(
						result -> {
							Assertions.assertThat(result.getResponse().getContentAsString()).startsWith("Welcome");
						}
				);
	}*/

}
