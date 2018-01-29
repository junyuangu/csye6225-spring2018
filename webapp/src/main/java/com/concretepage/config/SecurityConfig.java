package com.concretepage.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(securedEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private MyAppUserDetailsService myAppUserDetailsService;	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	  http.authorizeRequests()
		//.antMatchers("/","/home", "/index","/signup","/login","/403").permitAll()
		.antMatchers("/signup","/index").permitAll()
			  .anyRequest().authenticated()
		.antMatchers("/authUser").hasAnyRole("ADMIN","USER")
//		.antMatchers("/","/index","/signup").permitAll()
//			  .anyRequest().authenticated()
		//.antMatchers("/","/index","/signup").hasAnyRole("ADMIN","USER")
		.and()
		.formLogin()  //login configuration
			.loginPage("/login")
			.loginProcessingUrl("/app-login")
			.usernameParameter("app_username")
			.passwordParameter("app_password")
			.defaultSuccessUrl("/articles")
			.permitAll()
		.and().logout()    //logout configuration
			.logoutUrl("/app-logout")
			.logoutSuccessUrl("/")
			.permitAll()
		.and().exceptionHandling() //exception handling configuration
		.accessDeniedPage("/app/error");
	} 
    @Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        auth.userDetailsService(myAppUserDetailsService).passwordEncoder(passwordEncoder);
	}
}