package neu.csye6225.controller;

import com.amazonaws.services.s3.model.*;
import neu.csye6225.dao.IDescriptionRepository;
import neu.csye6225.dao.IPictureRepository;
import neu.csye6225.dao.IUserInfoRepository;
import neu.csye6225.entity.UserInfo;
import neu.csye6225.service.UserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.iterable.S3Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Date;

@Profile("production")
@Controller
public class ProductionForS3Controller {
    private final static Logger logger = LoggerFactory.getLogger(ProductionForS3Controller.class);
    private boolean authState = false;
    @Autowired
    private IUserInfoRepository userRepository;
    @Autowired
    private IPictureRepository picRepository;
    @Autowired
    private IDescriptionRepository desRepository;
    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private HttpSession session;

    @Autowired
    private Environment env;

    @RequestMapping("/aboutMe-searchByUser")
    public ModelAndView showAboutMeBySearch( @RequestParam("aboutMeSearch") String username ) {

        if( username==null ) {
            logger.info("showAboutMeBySearch method: no upload image.");
            String errMsg = "Please input a registered email.";
            logger.info(errMsg);
            return new ModelAndView( "403", "errorMessage", errMsg );
        }

        String aboutMeText = userInfoService.findAboutmeByUsername(username);

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
    
    @RequestMapping(method = {RequestMethod.GET,RequestMethod.POST}, value = "/deletePic")
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

        String picPath = "http://via.placeholder.com/240x180";
        ModelAndView mav = new ModelAndView("authUser");
        mav.addObject( "loginUser", app_username );
        mav.addObject( "fileTemporaryPath", picPath );
        mav.addObject( "currentTime", new Date() );
        return mav;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/upload-toDb")
    public ModelAndView UploadFileToS3Bucket( @RequestParam("uploadImg") MultipartFile file ) throws IOException {
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

        AmazonS3 s3client = AmazonS3ClientBuilder.standard().build();

        String fileName = file.getOriginalFilename();
        String suffix = new String();
        int i = fileName.lastIndexOf('.');
        if ( i != -1 ) {
            suffix = fileName.substring(i+1);
        }

        String bucketName = env.getProperty("bucket.name");
        s3client.createBucket(bucketName);
        String picName = app_username + "." + suffix;

        S3Object retrievedPic = null;

        try{
            InputStream is = file.getInputStream();
            //save on s3 with public read access
            if(!suffix.isEmpty()) {
                s3client.putObject(new PutObjectRequest(bucketName, picName, is, new ObjectMetadata()).withCannedAcl(CannedAccessControlList.PublicRead));
            }

            for(S3ObjectSummary summary: S3Objects.inBucket(s3client, bucketName)){
                String storedPicName = summary.getKey();
                int j = storedPicName.lastIndexOf('.');
                String ownerName = storedPicName.substring( 0, j );
                if( ownerName.equals(app_username) ) {
                    retrievedPic = s3client.getObject(bucketName, storedPicName);
                    break;
                }
            }
            if( retrievedPic != null ) {
                mav.addObject( "fileTemporaryPath", retrievedPic.getObjectContent().getHttpRequest().getURI().toString() );
            }
            else {
                mav.addObject("fileTemporaryPath", "http://via.placeholder.com/240x320");
            }
            //get a reference to the image object

        } catch (IOException e) {
            e.printStackTrace();
        }
        mav.setViewName("myProfile");
        mav.addObject( "loginUser", app_username );
        mav.addObject( "currentTime", new Date() );
        return mav;
    }

}
