package neu.csye6225.controller;

import neu.csye6225.Util.BCryptUtil;
import neu.csye6225.entity.UserInfo;
import neu.csye6225.service.IUserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author  Junyuan GU
 * @NUid    001825583
 */
@EnableWebMvc //make Autowired effective
@Controller
@RequestMapping("/")
@Profile("dev")
public class UserInfoController {
	private final static Logger logger = LoggerFactory.getLogger(UserInfoController.class);
	private boolean authState = false;
	@Autowired
	private IUserInfoService userInfoService;

	@Autowired
	private HttpSession	 session;

	@ModelAttribute("userInfo")
	public UserInfo getUserInfo(){
		UserInfo userInfo  = new UserInfo( "elfred012@gmail.com", "root1234");
		return userInfo;
	}

	@RequestMapping(value = {"", "#","index"}, method= {RequestMethod.GET})
	public ModelAndView index( HttpServletRequest request ){
		session = request.getSession();
		return new ModelAndView("index");
	}

	@RequestMapping(value = "signup", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView signUp() {
		logger.info("entering signUp");
		return new ModelAndView("signup");
	}

	@RequestMapping(value = "login-check", method = {RequestMethod.POST, RequestMethod.PUT})
	public ModelAndView loginCheck(@Valid @ModelAttribute("userInfo") UserInfo user, BindingResult result,HttpServletRequest request, Model model ) {
		session = request.getSession();

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
			return new ModelAndView("403", "errorMessage", "Account Not Found.");
		}

		String enPassword = BCrypt.hashpw(password, BCryptUtil.SALT);
		logger.info( "encoded PW: " + enPassword );
		boolean checked = userInfoService.checkAccount(username, enPassword);
		if (!checked) {
			logger.info("loginCheck method: password does not match username.");
			return new ModelAndView("403", "errorMessage", "Username does not Match Password.");
		} else {
			ModelAndView mav = new ModelAndView("authUser");
			session.setAttribute("loginUserName", username);
			mav.addObject( "loginUser", username );
			mav.addObject( "currentTime", new Date().toString() );

			String filepath = userInfoService.findPicPathByUsername(username);
			String relativePath = "../" + filepath;
			mav.addObject("fileTemporaryPath", relativePath);
			logger.info( filepath );
			//String aboutMe = userInfoService.findDescriptionByUsername(username);
			//mav.addObject( "aboutMeDescription", aboutMe );
			authState = true;
			return mav;
		}
	}

	@RequestMapping(value = "signup-check", method = {RequestMethod.POST})
	public ModelAndView signUpCheck(@Valid UserInfo user, BindingResult result, HttpServletRequest request, Model model) {

		String param_uemail = request.getParameter("uemail");
		String param_newpw = request.getParameter("newpw");
		String param_newpw2 = request.getParameter("newpw2");
		logger.info("Username: " + param_uemail);
		logger.info("Password: " + param_newpw);
		if( param_newpw==null || param_newpw2==null ) {
			logger.info("signUpCheck method: Password cannot be null.");
			return new ModelAndView("403", "errorMessage", "Password cannot be null!");
		}
		else if( !param_newpw.equals( param_newpw2 ) ) {
			logger.info("signUpCheck method: confirm password does not match.");
			return new ModelAndView("403", "errorMessage", "Confirm Password again!");
		}

		if (result.hasErrors()) {
			logger.info("signUpCheck method: result has errors.");
			List<ObjectError> errors = result.getAllErrors();
			String eMessage = errors.stream().map(e -> e.getDefaultMessage()).findFirst().get();
			return new ModelAndView("403", "errorMessage", eMessage);
		}

		String enPassword = BCrypt.hashpw(param_newpw, BCryptUtil.SALT);
		UserInfo newUser = new UserInfo( param_uemail, enPassword );
		logger.info( newUser.toString() );
		boolean exists = userInfoService.checkUserByName( newUser.getUsername() );
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

	@RequestMapping( value = "login", method = {RequestMethod.GET, RequestMethod.POST} )
	public ModelAndView login() {
		    ModelAndView mav = new ModelAndView();
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

		String userName = new String();
		userName = session.getAttribute("loginUserName").toString();
		mav.addObject("loginUser", userName);
		mav.addObject( "currentTime", new Date().toString() );

		String filepath = userInfoService.findPicPathByUsername(userName);
		mav.addObject("fileTemporaryPath", filepath);
		String aboutMe = userInfoService.findAboutmeByUsername(userName);
		mav.addObject( "aboutMeDescription", aboutMe );
		logger.info( filepath );

		return mav;
	}

	@PostMapping("logout")
	public ModelAndView logout() {
		authState = false;
		ModelAndView mav = new ModelAndView();
		mav.setViewName("index");
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

	//----------------------------add as follows for Assign5--------------------

	@GetMapping("editProfile")
	public ModelAndView editProfile() {
		ModelAndView mav = new ModelAndView();
		if( !authState ) {
			String errorMessage = "You are not authorized for editing profile, please login first.";
			mav.addObject("errorMessage", errorMessage);
			mav.setViewName("403");
			return mav;
		}

		String username = session.getAttribute("loginUserName").toString();
		if( username!=null ) {
			mav.setViewName("editProfile");
			mav.addObject("loginUser", username );
		} else {
			String errorMessage = "You are not authorized for editing profile, please login first.";
			mav.addObject("errorMessage", errorMessage);
			mav.setViewName("403");
			return mav;
		}

		return mav;
	}

	@GetMapping("myProfile")
	public ModelAndView myProfile() {
		ModelAndView mav = new ModelAndView();
		if( !authState ) {
			mav.setViewName("myProfile");
			mav.addObject("loginUser", "No LoginUser");
			mav.addObject( "currentTime", new Date().toString() );
			mav.addObject("authState", "false" );
			return mav;
		}

		String userName = session.getAttribute("loginUserName").toString();
		if( userName==null ) {
			mav.setViewName("myProfile");
			mav.addObject("loginUser", userName);
			mav.addObject( "currentTime", new Date().toString() );

		} else {
			mav.setViewName("myProfile");
			mav.addObject( "loginUser", userName );
			mav.addObject( "currentTime", new Date().toString() );
			String filePath = userInfoService.findPicPathByUsername( userName );
			mav.addObject("fileTemporaryPath", filePath);
			mav.addObject( "defaultPath", "../upload/default.png" );
			String aboutMe = userInfoService.findAboutmeByUsername( userName );
			if( aboutMe != null ) {
				mav.addObject( "imgInfoText", aboutMe );
			}
		}

		return mav;
	}

	@RequestMapping( value = "deletePic", method = {RequestMethod.GET,RequestMethod.POST} )
	public ModelAndView deletePicture( HttpServletRequest request ) {
		String app_username = session.getAttribute("loginUserName").toString();
		if( app_username == null && authState == false ) {
			logger.info("deletePicture method: no authenticated user.");
			String errMsg = "No Authenticated user, please login first.";
			logger.info(errMsg);
			return new ModelAndView( "403", "errorMessage", errMsg );
		}
		String path = System.getProperty("user.dir") + "/src/main/resources/upload/";
		String filename = path + "default.png";
		// update file path in the database
		userInfoService.updatePicture( filename, app_username );

		ModelAndView mav = new ModelAndView("authUser");
		mav.addObject( "loginUser", app_username );
		mav.addObject( "fileTemporaryPath", userInfoService.findPicPathByUsername(app_username) );
		mav.addObject( "currentTime", new Date() );
		return mav;
	}



	@RequestMapping( value = "upload-toDb", method = {RequestMethod.POST} )
	public ModelAndView uploadFile2DB( @RequestParam("uploadImg") MultipartFile multipartFile,
									   HttpServletRequest request ) throws IOException {
		String app_username = session.getAttribute("loginUserName").toString();
		if( app_username == null && authState == false ) {
			logger.info("uploadFile2DB method: no authenticated user.");
			String errMsg = "No Authenticated user, please login first.";
			logger.info(errMsg);
			return new ModelAndView( "403", "errorMessage", errMsg );
		}
		if( multipartFile.isEmpty() ) {
			logger.info("uploadFile2DB method: no upload image.");
			String errMsg = "Please choose an image first.";
			logger.info(errMsg);
			return new ModelAndView( "403", "errorMessage", errMsg );
		}

		// Get the default temporary file path
		//String path = System.getProperty("java.io.tmpdir");
		String path = System.getProperty("user.dir") + "/src/main/resources/";		//Absolute Project Path

		logger.info(path);
		if (!multipartFile.isEmpty()) {
			String filename = multipartFile.getOriginalFilename();
			//check the file type of uploading file, if it is an image.
			String suffix = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
			String filePath = path + "upload/";
			String newFileName =  System.currentTimeMillis() + "." + suffix;
			if ( !suffix.equals("png") && !suffix.equals("jpg") && !suffix.equals("jpeg") ) {
				//FileUtils.copyInputStreamToFile(multipartFile.getInputStream(),new File(path + "//upload//", System.currentTimeMillis() + "." + suffix));
				String errMsg = "Please upload image file( supported type: *.pgn/*.jpeg/*.jpg )";
				logger.info(errMsg);
				return new ModelAndView( "403", "errorMessage", errMsg );
			} else {
				try {
					logger.info( filePath + newFileName );
					// saves the file on disk
					multipartFile.transferTo( new File(filePath, newFileName) );
					//FileUtils.copyInputStreamToFile( multipartFile.getInputStream(), new File(filePath, newFileName) );

					// update file path in the database
					userInfoService.updatePicture( filePath+newFileName, app_username );

					request.setAttribute("message",
							"Upload has been done successfully!");

				} catch (Exception ex) {
					request.setAttribute("message",
							"There was an error: " + ex.getMessage());
				}

				//FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), new File(filePath, newFileName));
			}
		}
		ModelAndView mav = new ModelAndView("myProfile");
		mav.addObject( "loginUser", app_username );
		mav.addObject( "fileTemporaryPath", userInfoService.findPicPathByUsername(app_username) );
		mav.addObject( "currentTime", new Date() );
		return mav;

	}

	@RequestMapping( value = "update-AboutMe", method = { RequestMethod.POST } )
	public ModelAndView updateAboutMe( HttpServletRequest request ) {
		session = request.getSession();

		if( session.getAttribute("loginUserName")==null ) {
			String errMsg = "There is no Login User.";
			ModelAndView mav = new ModelAndView("myProfile", "errorMessage", errMsg );
			mav.setViewName("myProfile");

			return mav;
		}
		String loginUsername = session.getAttribute("loginUserName").toString();

		String aboutMeText = request.getParameter("aboutMeInput");
		String aboutMe = new String();
		if( aboutMeText==null ) {
			aboutMe = "none";
		} else if( aboutMeText.length() > 140 ) {
			aboutMe = aboutMeText.substring(0, 139);
		} else
			aboutMe = aboutMeText;
		//update Database
		userInfoService.updateProfile( aboutMe, loginUsername );

		ModelAndView mav = new ModelAndView();
		mav.addObject("loginUser", loginUsername);
		mav.addObject( "currentTime", new Date() );
		mav.addObject("imgInfoText", aboutMe);
		mav.setViewName("myProfile");

		return mav;

	}


	@RequestMapping( value = "aboutMe-searchByUser", method = {RequestMethod.POST} )
	public ModelAndView getAboutMeBySearchUser( HttpServletRequest request ) {
		String inputSearchUsername = request.getParameter("aboutMeSearch");
		if( inputSearchUsername==null ) {
			logger.info("getAboutMeBySearchUser method: no upload image.");
			String errMsg = "Please input a registered email.";
			logger.info(errMsg);
			return new ModelAndView( "403", "errorMessage", errMsg );
		}

		String aboutMeText = userInfoService.findAboutmeByUsername(inputSearchUsername);

		String aboutMe = new String();
		if( aboutMeText==null || aboutMeText.equals("") ) {
			aboutMe = "none";
		} else if( aboutMeText.length() > 140 ) {
			aboutMe = aboutMeText.substring(0, 139);
		} else
			aboutMe = aboutMeText;
		//update Database
		//userInfoService.updateProfile( aboutMe, loginUsername );
		ModelAndView mav = new ModelAndView();
		mav.addObject("loginUser", inputSearchUsername);
		mav.addObject( "currentTime", new Date() );
		mav.addObject("imgInfoText", aboutMe);
		mav.setViewName("myProfile");

		return mav;

	}

} 