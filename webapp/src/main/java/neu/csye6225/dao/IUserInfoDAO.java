package neu.csye6225.dao;
import neu.csye6225.entity.UserInfo;
import org.springframework.context.annotation.Profile;

import java.sql.SQLException;
import java.util.List;

/**
 * @author  Junyuan GU
 * @NUid    001825583
 */
@Profile("dev")
public interface IUserInfoDAO {
	void deleteByName(String name);
	void insertUserInfo( UserInfo newUser ) throws SQLException;
	UserInfo findByNameAndPw(String username, String password);
	UserInfo findByUsername( String username );
	UserInfo getActiveUser();
	List<UserInfo> getAllUserInfos();
}