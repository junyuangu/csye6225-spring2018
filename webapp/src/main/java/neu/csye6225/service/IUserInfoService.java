package neu.csye6225.service;

import neu.csye6225.entity.UserInfo;
import org.springframework.security.access.annotation.Secured;

import java.util.List;

public interface IUserInfoService {
    boolean save(UserInfo user);
    boolean checkAccount( String name, String encodePw );
    boolean checkUserByName( String name );
    UserInfo findByUsername( String username );
    @Secured ({"ROLE_ADMIN"})
    List<UserInfo> getAllUserInfos();

}
