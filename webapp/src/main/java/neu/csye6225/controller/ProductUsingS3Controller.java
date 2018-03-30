package neu.csye6225.controller;

import com.amazonaws.services.s3.model.*;
import neu.csye6225.Util.BCryptUtil;
import neu.csye6225.entity.UserInfo;
import neu.csye6225.service.IUserServiceProduct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.iterable.S3Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping("/")
@Profile("aws")
public class ProductUsingS3Controller {
    private final static Logger logger = LoggerFactory.getLogger(ProductUsingS3Controller.class);
    private final static String imgPlaceHolder = "http://via.placeholder.com/240x320";

    private boolean authState = false;

    @Autowired
    private IUserServiceProduct userInfoServiceProduct;

    @Autowired
    private HttpSession session;

    @Autowired
    private Environment env;

    @ModelAttribute("userInfo")
    public UserInfo getUserInfo(){
        UserInfo userInfo  = new UserInfo( "elfred012@gmail.com", "root1234");
        return userInfo;
    }

    @RequestMapping(value = {"", "#","index"}, method= {RequestMethod.GET})
    public ModelAndView indexProduct( HttpServletRequest request ){
        session = request.getSession();
        return new ModelAndView("index");
    }

    @RequestMapping(value = "signup", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView signUpProduct() {
        logger.info("entering signUp");
        return new ModelAndView("signup");
    }

    @RequestMapping(value = "signup-check", method = {RequestMethod.POST})
    public ModelAndView signUpCheckProduct(@Valid UserInfo user, BindingResult result, HttpServletRequest request, Model model) {

        String param_uemail = request.getParameter("uemail");
        String param_newpw = request.getParameter("newpw");
        String param_newpw2 = request.getParameter("newpw2");
        logger.info("Production Username: " + param_uemail);
        logger.info("Production Password: " + param_newpw);
        if( param_newpw==null || param_newpw2==null ) {
            logger.info("signUpCheckProduct method: Password cannot be null.");
            return new ModelAndView("403", "errorMessage", "Password cannot be null!");
        }
        else if( !param_newpw.equals( param_newpw2 ) ) {
            logger.info("signUpCheck method: confirm password does not match.");
            return new ModelAndView("403", "errorMessage", "Confirm Password again!");
        }

        if (result.hasErrors()) {
            logger.info("signUpCheckProduct method: result has errors.");
            List<ObjectError> errors = result.getAllErrors();
            String eMessage = errors.stream().map(e -> e.getDefaultMessage()).findFirst().get();
            return new ModelAndView("403", "errorMessage", eMessage);
        }

        String enPassword = BCrypt.hashpw(param_newpw, BCryptUtil.SALT);
        UserInfo newUser = new UserInfo( param_uemail, enPassword );
        logger.info( newUser.toString() );
        boolean exists = userInfoServiceProduct.checkUserByName( newUser.getUsername() );
        if (exists) {
            logger.info("signUpCheck method: Account already existed.");
            return new ModelAndView("403", "errorMessage", "Account Already Exists");
        }

        userInfoServiceProduct.save(newUser);
        boolean uCheck = userInfoServiceProduct.checkUserByName( newUser.getUsername() );
        if( uCheck != false ) {
            return new ModelAndView("login");
        } else {
            return new ModelAndView("403", "errorMessage", "Save User Failed");
        }

    }

    @RequestMapping( value = "login", method = {RequestMethod.GET, RequestMethod.POST} )
    public ModelAndView loginProduct() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("login");
        return mav;
    }

    @PostMapping("logout")
    public ModelAndView logoutProduct() {
        authState = false;
        ModelAndView mav = new ModelAndView();
        mav.setViewName("index");
        return mav;
    }

    @GetMapping("error")
    public ModelAndView errorProduct() {
        logger.info( "Entering errorProduct method." );
        ModelAndView mav = new ModelAndView();
        String errorMessage= "You are not authorized for the requested data.";
        mav.addObject("errorMsg", errorMessage);
        mav.setViewName("403");
        return mav;
    }

    //---------------------------- Assign5 codes--------------------
    @GetMapping("editProfile")
    public ModelAndView editProfileProduct() {
        logger.info( "Entering editProfileProduct method" );
        ModelAndView mav = new ModelAndView();
        if( !authState ) {
            logger.info( "editProfileProduct method: Unauthorized User." );
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
            logger.info( "editProfileProduct method: cannot find the User." );
            String errorMessage = "You are not authorized for editing profile, please login first.";
            mav.addObject("errorMessage", errorMessage);
            mav.setViewName("403");
            return mav;
        }

        return mav;
    }

    @GetMapping("myProfile")
    public ModelAndView myProfileProduct() {
        logger.info( "Entering myProfileProduct method." );
        ModelAndView mav = new ModelAndView();
        if( !authState ) {
            logger.info( "myProfileProduct method: Unauthorized User." );
            mav.setViewName("myProfile");
            mav.addObject("loginUser", "No LoginUser");
            mav.addObject( "currentTime", new Date().toString() );
            mav.addObject("authState", "false" );
            return mav;
        }

        String userName = session.getAttribute("loginUserName").toString();
        if( userName==null ) {
            logger.info( "myProfileProduct method: cannot find the User." );
            mav.setViewName("myProfile");
            mav.addObject("loginUser", userName);
            mav.addObject( "currentTime", new Date().toString() );

        } else {
            logger.info( "myProfileProduct method: show the profile of the User." );
            mav.setViewName("myProfile");
            mav.addObject( "loginUser", userName );
            mav.addObject( "currentTime", new Date().toString() );

            String reFilePath = new String();
            String filePath = userInfoServiceProduct.findPicPathByUsername( userName );
            if( filePath==null )
                reFilePath = imgPlaceHolder;
            else
                reFilePath = productRetrieveFileFromS3( filePath, userName );
            logger.info( reFilePath );
            mav.addObject("fileTemporaryPath", reFilePath);
            mav.addObject( "defaultPath", "../upload/default.png" );
            String aboutMe = userInfoServiceProduct.findAboutmeByUsername( userName );
            if( aboutMe != null ) {
                mav.addObject( "imgInfoText", aboutMe );
            }
        }

        return mav;
    }


    //for S3 op
    @RequestMapping(value = "login-check", method = {RequestMethod.POST, RequestMethod.PUT})
    public ModelAndView loginCheckProduct(@Valid @ModelAttribute("userInfo") UserInfo user, BindingResult result, HttpServletRequest request, Model model ) {
        session = request.getSession();
        String username = request.getParameter("app_username");
        String password = request.getParameter("app_password");
        logger.info("Username: " + username);
        logger.info("Password: " + password);
        if( result.hasErrors() ) {
            logger.info("loginCheckProduct method: result has errors.");
            List<ObjectError> errors = result.getAllErrors();
            String errMsg = errors.stream().map( e -> e.getDefaultMessage() ).findFirst().get();
            logger.info(errMsg);
            return new ModelAndView( "403", "errorMessage", errMsg );
        }

        boolean exists = userInfoServiceProduct.checkUserByName(username);
        if (!exists) {
            logger.info("loginCheckProduct method: username does not exist.");
            return new ModelAndView("403", "errorMessage", "Account Not Found.");
        }

        String enPassword = BCrypt.hashpw(password, BCryptUtil.SALT);
        logger.info( "encoded PW: " + enPassword );
        boolean checked = userInfoServiceProduct.checkAccount(username, enPassword);
        if (!checked) {
            logger.info("loginCheckProduct method: password does not match username.");
            return new ModelAndView("403", "errorMessage", "Username does not Match Password.");
        } else {
            ModelAndView mav = new ModelAndView("authUser");
            logger.info("loginCheckProduct method: go to autherUser Page");
            session.setAttribute("loginUserName", username);
            mav.addObject( "loginUser", username );
            mav.addObject( "currentTime", new Date().toString() );

            String reFilePath = new String();
            String fileName = userInfoServiceProduct.findPicPathByUsername( username );
            if( fileName==null )
                reFilePath = imgPlaceHolder;
            else
                //Retrieve the Image URI from S3 Bucket
                reFilePath = productRetrieveFileFromS3( fileName, username );
            logger.info( "File Path read from S3: " + reFilePath );
            mav.addObject( "fileTemporaryPath", reFilePath );

            String aboutMe = userInfoServiceProduct.findAboutmeByUsername(username);
            mav.addObject( "aboutMeDescription", aboutMe );
            authState = true;
            return mav;
        }
    }

    @RequestMapping("aboutMe-searchByUser")
    public ModelAndView showAboutMeBySearch( @RequestParam("aboutMeSearch") String username ) {

        if( username==null ) {
            logger.info("showAboutMeBySearch method: no upload image.");
            String errMsg = "Please input a registered email.";
            logger.info(errMsg);
            return new ModelAndView( "403", "errorMessage", errMsg );
        }

        String aboutMeText = userInfoServiceProduct.findAboutmeByUsername(username);

        String aboutMe = new String();
        if( aboutMeText==null || aboutMeText.equals("") ) {
            aboutMe = "none";
        } else if( aboutMeText.length() > 140 ) {
            aboutMe = aboutMeText.substring(0, 139);
        } else
            aboutMe = aboutMeText;

        ModelAndView mav = new ModelAndView();
        mav.addObject("loginUser", username);
        mav.addObject( "currentTime", new Date() );
        mav.addObject("imgInfoText", aboutMe);
        mav.setViewName("myProfile");

        return mav;
    }

    @RequestMapping( method = {RequestMethod.GET,RequestMethod.POST}, value = "deletePic" )
    public ModelAndView DeleteFileInS3Bucket (  Model model )  {
        String app_username = session.getAttribute("loginUserName").toString();
        logger.info( "app_username: " + app_username );

        AmazonS3 s3client = AmazonS3ClientBuilder.standard().build();
        String toDelete = "";
        for(S3ObjectSummary summary: S3Objects.inBucket(s3client, env.getProperty("bucket.name"))){
            String picName = summary.getKey();
            int i = picName.lastIndexOf('.');
            String ownerName = picName.substring(0, i);
            if(ownerName.equals(app_username)){
                toDelete = picName;
                break;
            }
        }
        if(!toDelete.equals("")){
            s3client.deleteObject(env.getProperty("bucket.name"), toDelete);
        }

        userInfoServiceProduct.updatePicture("default.png", app_username);

        String picPath = imgPlaceHolder;
        ModelAndView mav = new ModelAndView("authUser");
        mav.addObject( "loginUser", app_username );
        mav.addObject( "fileTemporaryPath", picPath );
        mav.addObject( "currentTime", new Date() );
        return mav;
    }

    @RequestMapping(method = RequestMethod.POST, value = "upload-toDb")
    public ModelAndView UploadFileToS3Bucket( @RequestParam("uploadImg") MultipartFile file ) {
        ModelAndView mav = new ModelAndView();
        String app_username = session.getAttribute("loginUserName").toString();
        if( app_username == null && authState == false ) {
            logger.info("UploadFileToS3Bucket method: no authenticated user.");
            String errMsg = "No Authenticated user, please login first.";
            logger.info(errMsg);
            return new ModelAndView( "403", "errorMessage", errMsg );
        }
        if( file.isEmpty() ) {
            logger.info("UploadFileToS3Bucket method: no upload image.");
            String errMsg = "Please choose an image first.";
            logger.info(errMsg);
            return new ModelAndView( "403", "errorMessage", errMsg );
        }
        String fileName = file.getOriginalFilename();

        String reFilePath = productSaveFileToS3( file, fileName, app_username );
        logger.info( "File Path read from S3: " + reFilePath );
        mav.addObject( "fileTemporaryPath", reFilePath );

        mav.setViewName("myProfile");
        mav.addObject( "loginUser", app_username );
        mav.addObject( "currentTime", new Date() );
        return mav;
    }

    @RequestMapping( value = "update-AboutMe", method = { RequestMethod.POST } )
    public ModelAndView UpdateAboutMeProduct( HttpServletRequest request ) {
        session = request.getSession();

        if( session.getAttribute("loginUserName")==null ) {
            String errMsg = "Production update-AboutMe: There is no Login User.";
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
        userInfoServiceProduct.updateProfile( aboutMe, loginUsername );

        ModelAndView mav = new ModelAndView();
        mav.addObject("loginUser", loginUsername);
        mav.addObject( "currentTime", new Date() );
        mav.addObject("imgInfoText", aboutMe);
        mav.setViewName("myProfile");

        return mav;

    }

    /**
     * product service 1: sava one file to S3
     * @return
     */
    private String productSaveFileToS3( MultipartFile file, String fileName, String app_username ) {
        logger.info( "Entering productSaveFileToS3 method." );
        AmazonS3 s3client = AmazonS3ClientBuilder.standard().build();

        String suffix = new String();
        int i = fileName.lastIndexOf('.');
        if ( i != -1 ) {
            suffix = fileName.substring(i+1);
        }

        String bucketName = env.getProperty("bucket.name");
        s3client.createBucket(bucketName);
        String picName = app_username + "." + suffix;

        String retrievedPicPath = null;

        try{
            InputStream is = file.getInputStream();
            //save on s3 with public read access
            if(!suffix.isEmpty()) {
                userInfoServiceProduct.updatePicture( picName, app_username );
                s3client.putObject(new PutObjectRequest(bucketName, picName, is, new ObjectMetadata()).withCannedAcl(CannedAccessControlList.PublicRead));
                logger.info( "productSaveFileToS3 method: put object into s3 bucket." );
            }

            retrievedPicPath = productRetrieveFileFromS3( fileName, app_username );
            if( retrievedPicPath.equals(imgPlaceHolder) ) {
                userInfoServiceProduct.updatePicture( "default.png", app_username );
                return imgPlaceHolder;
            }
            else if( retrievedPicPath!=null ) {
                userInfoServiceProduct.updatePicture( picName, app_username );
                return retrievedPicPath;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return  imgPlaceHolder;
    }

    /**
     * product service 2: retrieve one file file S3
     * @return
     */
    private String productRetrieveFileFromS3( String fileName, String app_username ) {
        logger.info("Entering productRetrieveFileFromS3 method.");
        AmazonS3 s3client = AmazonS3ClientBuilder.standard().build();

        String suffix = new String();
        int i = fileName.lastIndexOf('.');
        if ( i != -1 ) {
            suffix = fileName.substring(i+1);
        }
        else
            return imgPlaceHolder;
        String bucketName = env.getProperty("bucket.name");
        s3client.createBucket(bucketName);
        String picName = app_username + "." + suffix;

        S3Object retrievedPic = null;


        String storedPicName = new String();
        for( S3ObjectSummary sumObj : S3Objects.inBucket(s3client, bucketName) ) {
            storedPicName = sumObj.getKey();
            int k = storedPicName.lastIndexOf('.');
            String ownerName = storedPicName.substring( 0, k );
            if( ownerName.equals(app_username) ) {
                retrievedPic = s3client.getObject( bucketName, storedPicName );
                break;
            }
        }
        if( retrievedPic != null ) {
            return retrievedPic.getObjectContent().getHttpRequest().getURI().toString();
        }
        else {
            return imgPlaceHolder;
        }


    }

}
