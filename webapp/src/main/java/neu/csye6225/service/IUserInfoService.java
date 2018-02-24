package neu.csye6225.service;

import neu.csye6225.entity.UserInfo;
<<<<<<< HEAD
import org.springframework.security.access.annotation.Secured;
=======
>>>>>>> Assignment2

import java.util.List;

public interface IUserInfoService {
<<<<<<< HEAD
=======
    void deleteByName(String name);
>>>>>>> Assignment2
    boolean save(UserInfo user);
    boolean checkAccount( String name, String encodePw );
    boolean checkUserByName( String name );
    UserInfo findByUsername( String username );
<<<<<<< HEAD
    @Secured ({"ROLE_ADMIN"})
=======
    //@Secured ({"ROLE_ADMIN"})
>>>>>>> Assignment2
    List<UserInfo> getAllUserInfos();

}
