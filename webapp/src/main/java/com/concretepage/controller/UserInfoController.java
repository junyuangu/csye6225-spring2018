package com.concretepage.controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.concretepage.service.IUserInfoService;

@Controller
@RequestMapping("/")
public class UserInfoController {

	private final static Logger logger = LoggerFactory.getLogger(UserInfoController.class);

	@RequestMapping(value = {"", "/home","/index"}, method= RequestMethod.GET)
	public String index(){
		return "index";
	}

	@RequestMapping(value = {"/signup"}, method= RequestMethod.GET)
	public String signup(){
		return "signup";
	}

	@Autowired
	private IUserInfoService userInfoService;

	/*@GetMapping("signup")
	public ModelAndView signup() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("signup");
		return mav;
	}*/

	@GetMapping("login")
	public ModelAndView login() {
		    ModelAndView mav = new ModelAndView();
		    mav.setViewName("custom-login");
		    return mav;
    }
	@GetMapping("authuser")
	public ModelAndView authuser() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("authUser");
		return mav;
	}
	@GetMapping("secure/article-details")
	public ModelAndView getAllUserArticles() {
		    ModelAndView mav = new ModelAndView();
		    mav.addObject("userArticles", userInfoService.getAllUserArticles());
		    mav.setViewName("articles");
		    return mav;
    }
	@GetMapping("error")
	public ModelAndView error() {
		    ModelAndView mav = new ModelAndView();
		    String errorMessage= "You are not authorized for the requested data.";
		    mav.addObject("errorMsg", errorMessage);
		    mav.setViewName("403");
		    return mav;
    }		
} 