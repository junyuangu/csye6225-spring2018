package neu.csye6225.controller;

import neu.csye6225.Util.BCryptUtil;
import neu.csye6225.entity.UserInfo;
import neu.csye6225.service.IUserInfoService;
import neu.csye6225.service.UserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/")
public class UserInfoController {

	private final static Logger logger = LoggerFactory.getLogger(UserInfoController.class);

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
	public ModelAndView loginCheck(@Valid UserInfo user, BindingResult result,
								   HttpServletRequest request, Model model ) {
		String username = request.getParameter("app_username");
		String password = request.getParameter("app_password");
		logger.info("Username: " + username);
		logger.info("Password: " + password);
		if( result.hasErrors() ) {
			List<ObjectError> errors = result.getAllErrors();
			String errMsg = errors.stream().map( e -> e.getDefaultMessage() ).findFirst().get();
			return new ModelAndView( "403", "errorMessage", errMsg );
		}


		UserInfoService userInfoService = new UserInfoService();

		boolean exists = userInfoService.checkUserByName(username);
		if (!exists) {
			return new ModelAndView("403", "errorMessage", "Account Not Found");
		}

		String enPassword = BCrypt.hashpw(password, BCryptUtil.SALT);
		System.out.println( enPassword );
		boolean checked = userInfoService.checkAccount(username, enPassword);
		if (!checked) {
			return new ModelAndView("403", "errorMessage", "Username or Password Invalid");
		} else {
			ModelAndView mav = new ModelAndView();
			mav.addObject("user", username);
			mav.addObject("currentTime", new Date().toString());
			mav.setViewName("authUser");
			return mav;
		}
	}

	@RequestMapping(value = "signup-check", method = {RequestMethod.POST})
	public ModelAndView signUpCheck(@Valid UserInfo user, BindingResult result, HttpServletRequest request, Model model) {
		logger.info( user.toString() );
		if (result.hasErrors()) {
			List<ObjectError> errors = result.getAllErrors();
			String eMessage = errors.stream().map(e -> e.getDefaultMessage()).findFirst().get();
			return new ModelAndView("errorpage", "errorMessage", eMessage);
		}

		String password = user.getPassword();

		String enPassword = BCrypt.hashpw(password, BCryptUtil.SALT);
		UserInfo newUser = new UserInfo(request.getParameter("uemail"), enPassword);
		boolean exists = userInfoService.checkUserByName(newUser.getUsername());
		if (exists) {
			return new ModelAndView("errorpage", "errorMessage", "Account Already Exists");
		}
		boolean uCheck = userInfoService.save(newUser);
		if( uCheck != true ) {
			return new ModelAndView("index");
		} else {
			return new ModelAndView("errorpage", "errorMessage", "Save User Failed");
		}

	}

	@Autowired
	private IUserInfoService userInfoService;

	@GetMapping("login")
	public ModelAndView login() {
		    ModelAndView mav = new ModelAndView();
		    mav.setViewName("custom-login");
		    return mav;
    }
	@GetMapping("authUser")
	public ModelAndView authuser() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("authUser");
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