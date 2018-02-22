package neu.csye6225.config;

import neu.csye6225.service.MyAppUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	@Qualifier(  )
	private MyAppUserDetailsService myAppUserDetailsService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
	  http.authorizeRequests()
		.antMatchers("/","/#","/signup","/index","/login","/403", "/login-check").permitAll()
			  .anyRequest().authenticated()
		.antMatchers("/authUser").hasAnyRole("ADMIN","USER")
		.and()
		.formLogin()  //login configuration
			.loginPage("/login")
			.loginProcessingUrl("/login-check")
			.usernameParameter("app_username")
			.passwordParameter("app_password")
			.defaultSuccessUrl("/authUser")
			.permitAll()
		.and().logout()    //logout configuration
			.logoutUrl("/app-logout")
			.logoutSuccessUrl("/index")
			.permitAll()
		.and().exceptionHandling() //exception handling configuration
		.accessDeniedPage("/403");
	} 
    @Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        auth.userDetailsService(myAppUserDetailsService).passwordEncoder(passwordEncoder);
	}
}