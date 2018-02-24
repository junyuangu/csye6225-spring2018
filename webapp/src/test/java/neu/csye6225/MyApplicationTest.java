package neu.csye6225;

import neu.csye6225.controller.UserInfoController;
import neu.csye6225.service.UserInfoService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@AutoConfigureMockMvc
public class MyApplicationTest {

    private MockMvc mvc;

    @Autowired
    private UserInfoService userInfoService;

    @Before
    public void setUp() throws Exception {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        mvc = MockMvcBuilders.standaloneSetup(new UserInfoController()).setViewResolvers(viewResolver).build();

    }

    @Test
    public void testUserController() throws Exception {
        RequestBuilder request = null;

        request = get("/login");
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk());

        request = get("/signup");
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    public void dboperationTest() throws Exception {
        RequestBuilder request = null;
        request = post("/signup-check")
				.param("id", "10")
				.param("username", "root123@163.com")
				.param("password", "1234567!");
		mvc.perform(request)
				.andExpect(MockMvcResultMatchers.status().isOk());

		request = post("/login-check")
				.param("username", "root123@163.com")
				.param("password", "1234567!");
		mvc.perform(request)
				.andExpect(MockMvcResultMatchers.status().isOk());  //*/
    }

}
