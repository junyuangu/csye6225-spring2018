package neu.csye6225.service;

import neu.csye6225.entity.UserInfo;

import java.util.List;

public interface IUserInfoService {
    void deleteByName(String name);
    boolean save(UserInfo user);
    boolean checkAccount( String name, String encodePw );
    boolean checkUserByName( String name );
    UserInfo findByUsername( String username );
    //@Secured ({"ROLE_ADMIN"})
    List<UserInfo> getAllUserInfos();

}
