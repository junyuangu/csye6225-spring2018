package neu.csye6225.service;

import neu.csye6225.entity.Description;
import neu.csye6225.entity.Picture;
import neu.csye6225.entity.UserInfo;
import org.springframework.context.annotation.Profile;

import java.util.List;
@Profile("production")
public interface IUserServiceProduct {
    void deleteByName(String name);
    void save(UserInfo user);
    boolean checkAccount( String name, String encodePw );
    boolean checkUserByName( String name );
    UserInfo findByUsername( String username );
    //@Secured ({"ROLE_ADMIN"})
    List<UserInfo> getAllUserInfos();
    String findAboutmeByUsername( String username );
    Description findDescriptionByUsername(String username );
    String findPicPathByUsername(String username );

    void updatePicture( String filename, String loginUsername  );
    void updateProfile( String aboutMe, String loginUsername );
    Picture findPicByUsername(String username );
}
