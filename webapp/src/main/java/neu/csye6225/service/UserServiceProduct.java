package neu.csye6225.service;

import neu.csye6225.dao.IDescriptionRepository;
import neu.csye6225.dao.IPictureRepository;
import neu.csye6225.dao.IUserInfoRepository;
import neu.csye6225.entity.Description;
import neu.csye6225.entity.Picture;
import neu.csye6225.entity.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.annotation.Resource;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author  Junyuan GU
 * @NUid    001825583
 */

@EnableWebMvc
@Service
@Transactional
@Profile("aws")
public class UserServiceProduct implements IUserServiceProduct {
    private final static Logger logger = LoggerFactory.getLogger(UserInfoService.class);
    @Autowired
    private IUserInfoRepository userInfoRepository;
    /**
     * This means to get the bean called userRepository which is auto-generated by Spring,
     * we will use it to handle the data.
     */
    @Autowired
    private IPictureRepository pictureRepository;

    @Autowired
    private IDescriptionRepository descriptionRepository;

    @Override
    public void deleteByName(String name) {
        for (UserInfo userInfo : userInfoRepository.findAll()) {
            if( userInfo.getUsername().equals(name) ) {
                userInfoRepository.delete(userInfo);
            }
        }
    }

    @Override
    public void save(UserInfo user) {
        userInfoRepository.save(user);
    }

    @Override
    public boolean checkUserByName( String name ) {
        UserInfo user = findByUsername(name);
        if(user!=null) {
            String foundName = user.getUsername();
            if( foundName.equals(name) )
                return true;
        }
        return false;
    }

    @Override
    public boolean checkAccount( String name, String encodePw ) {
        UserInfo user = findByUsername(name);
        if( user==null )
            return false;
        if( user.getUsername().equals(name) && user.getPassword().equals( encodePw ) )
            return true;
        return false;
    }

    @Override
    public List<UserInfo> getAllUserInfos(){
        List<UserInfo> userList = new ArrayList<>();
        Iterable<UserInfo> iterableUser = userInfoRepository.findAll();
        while( iterableUser.iterator().hasNext() ) {
            userList.add( iterableUser.iterator().next() );
        }
        if( userList==null )
            return null;

        return userList;
    }

    @Override
    public UserInfo findByUsername( String username ) {

        for (UserInfo userInfo : userInfoRepository.findAll()) {
            if( userInfo.getUsername().equals(username) ) {
                return userInfo;
            }
        }

        return null;
    }

    @Override
    public String findPicPathByUsername(String username ) {
        String picPath = new String();
        for (Picture picture : pictureRepository.findAll()) {
            if( picture.getUsername().equals(username) ){
                picPath = picture.getPicpath();
                return picPath;
            }
        }
        return null;
    }

    @Override
    public Picture findPicByUsername(String username ) {
        String picPath = new String();
        for (Picture picture : pictureRepository.findAll()) {
            if( picture.getUsername().equals(username) ){
                return picture;
            }
        }
        return null;
    }

    @Override
    public Description findDescriptionByUsername(String username ) {

        for( Description description : descriptionRepository.findAll() ) {
            if( description.getUsername().equals(username) ){
                return description;
            }
        }
        return null;
    }

    @Override
    public String findAboutmeByUsername( String username ) {
        String desContent = new String();
        for (Description description : descriptionRepository.findAll()) {
            if( description.getUsername().equals(username) ){
                desContent = description.getAboutMe();
                return desContent;
            }
        }
        return null;

    }

    @Override
    public void updatePicture( String filename, String loginUsername  ) {
        String picPath =  findPicPathByUsername( loginUsername );
        if( picPath == null ){
            Picture newPic = new Picture( loginUsername, filename );
            logger.info( "Production updatePicture method: " + newPic.toString() );

            try{
                pictureRepository.save(newPic);
            } catch( Exception e ) {
                e.printStackTrace();
            }
        } else {
            Picture pic = findPicByUsername( loginUsername );
            pic.setPicpath(filename);
            pictureRepository.save(pic);
            String filepath= System.getProperty("user.dir") + "/src/main/resources/upload/";
            if(  picPath.equals( filepath + "default.png" ) )
                return;
            try {
                File file = new File( picPath );
                if (file.delete()) {
                    logger.info( "Production updatePicture method: " );
                    logger.info( file.getName() + " has been deleted." );
                } else {
                    logger.info( "delete fail." );
                }
            } catch (Exception e) {
                logger.info("Exception occurred");
                e.printStackTrace();
            }

        }

    }

    @Override
    public void updateProfile( String aboutMe, String loginUsername ) {
        String description =  findAboutmeByUsername( loginUsername );
        if( description == null ){
            Description newProfile = new Description( loginUsername, aboutMe );
            logger.info( "Production updateProfile method: " + newProfile.toString() );
            try{
                descriptionRepository.save(newProfile);
            } catch( Exception e ) {
                e.printStackTrace();
            }
        } else {
            Description des = findDescriptionByUsername( loginUsername );
            des.setAboutMe( aboutMe );
            descriptionRepository.save( des );

        }
    }


}
