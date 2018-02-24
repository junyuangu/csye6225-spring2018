package neu.csye6225.controller;

import neu.csye6225.Util.BCryptUtil;
import neu.csye6225.entity.UserInfo;
import neu.csye6225.service.IUserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@EnableWebMvc //make Autowired effective
@Controller
@RequestMapping("/")
public class UserInfoController {
	private final static Logger logger = LoggerFactory.getLogger(UserInfoController.class);
	private boolean authState = false;
	@Autowired
	private IUserInfoService userInfoService;

	@ModelAttribute("userInfo")
	public UserInfo getUserInfo(){
		UserInfo userInfo  = new UserInfo( "elfred012@gmail.com", "root1234");
		return userInfo;
	}

	@RequestMapping(value = {"", "#","index"}, method= {RequestMethod.GET})
	public ModelAndView index(){

		return new ModelAndView("index");
	}

	@RequestMapping(value = "signup", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView signUp() {
		logger.info("entering signUp");
		return new ModelAndView("signup");
	}

	@RequestMapping(value = "login-check", method = {RequestMethod.POST, RequestMethod.PUT})
	public ModelAndView loginCheck(@Valid @ModelAttribute("userInfo") UserInfo user, BindingResult result,HttpServletRequest request, Model model ) {

		String username = request.getParameter("app_username");
		String password = request.getParameter("app_password");
		logger.info("Username: " + username);
		logger.info("Password: " + password);
		if( result.hasErrors() ) {
			logger.info("loginCheck method: result has errors.");
			List<ObjectError> errors = result.getAllErrors();
			String errMsg = errors.stream().map( e -> e.getDefaultMessage() ).findFirst().get();
			logger.info(errMsg);
			return new ModelAndView( "403", "errorMessage", errMsg );
		}

		boolean exists = userInfoService.checkUserByName(username);
		if (!exists) {
			logger.info("loginCheck method: username does not exist.");
			return new ModelAndView("403", "errorMessage", "Account Not Found");
		}

		String enPassword = BCrypt.hashpw(password, BCryptUtil.SALT);
		logger.info( "encoded PW: " + enPassword );
		boolean checked = userInfoService.checkAccount(username, enPassword);
		if (!checked) {
			logger.info("loginCheck method: password does not match username.");
			return new ModelAndView("403", "errorMessage", "Username or Password Invalid");
		} else {
			ModelAndView mav = new ModelAndView("authUser");
			mav.addObject( "loginUser", username );
			mav.addObject( "currentTime", new Date().toString() );
			authState = true;
			return mav;
		}
	}

	@RequestMapping(value = "signup-check", method = {RequestMethod.POST})
	public ModelAndView signUpCheck(@Valid UserInfo user, BindingResult result, HttpServletRequest request, Model model) {

		logger.info( user.toString() );
		if (result.hasErrors()) {
			logger.info("signUpCheck method: result has errors.");
			List<ObjectError> errors = result.getAllErrors();
			String eMessage = errors.stream().map(e -> e.getDefaultMessage()).findFirst().get();
			return new ModelAndView("403", "errorMessage", eMessage);
		}

		String password = user.getPassword();

		String enPassword = BCrypt.hashpw(password, BCryptUtil.SALT);
		UserInfo newUser = new UserInfo(request.getParameter("uemail"), enPassword);
		boolean exists = userInfoService.checkUserByName(newUser.getUsername());
		if (exists) {
			logger.info("signUpCheck method: Account already existed.");
			return new ModelAndView("403", "errorMessage", "Account Already Exists");
		}
		boolean uCheck = userInfoService.save(newUser);
		if( uCheck != false ) {
			return new ModelAndView("login");
		} else {
			return new ModelAndView("403", "errorMessage", "Save User Failed");
		}

	}

	@GetMapping("login")
	public ModelAndView login() {
		    ModelAndView mav = new ModelAndView();
		    //mav.setViewName("custom-login");
			mav.setViewName("login");
		    return mav;
    }


	@GetMapping("authUser")
	public ModelAndView authuser() {
		if( !authState ) {
			//For convenience of demonstrate the WebApp, just use this piece of code to delete the
			//new user: root@163.com
			String username = "root@163.com";
			boolean exists = userInfoService.checkUserByName(username);
			if( exists ) {
				userInfoService.deleteByName(username);
				logger.info( "The new user: root@163.com has been deleted for convenience of demonstration.");
			}

			return new ModelAndView("403", "errorMessage", "Please Login first.");
		}
		ModelAndView mav = new ModelAndView("authUser");
		mav.addObject( "currentTime", new Date().toString() );

		return mav;
	}

	@PostMapping("logout")
	public ModelAndView logout() {
		authState = false;
		ModelAndView mav = new ModelAndView();
		mav.setViewName("index");
		return mav;
	}

	@GetMapping("secure/userinfo-details")
	public ModelAndView getAllUserInfos() {
		    ModelAndView mav = new ModelAndView();
		    mav.addObject("allUserInfos", userInfoService.getAllUserInfos());
		    mav.setViewName("details");
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