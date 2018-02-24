package neu.csye6225.dao;
import neu.csye6225.entity.UserInfo;

import java.sql.SQLException;
import java.util.List;

public interface IUserInfoDAO {
<<<<<<< HEAD
=======
	void deleteByName(String name);
>>>>>>> Assignment2
	void insertUserInfo( UserInfo newUser ) throws SQLException;
	UserInfo findByNameAndPw(String username, String password);
	UserInfo findByUsername( String username );
	UserInfo getActiveUser();
	List<UserInfo> getAllUserInfos();
}