package neu.csye6225.service;

import neu.csye6225.dao.UserInfoDAO;
import neu.csye6225.entity.UserInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.List;

//@EnableWebMvc
@Service
@Transactional
public class UserInfoService implements IUserInfoService {
	//@Autowired
	@Resource
	private UserInfoDAO userInfoDAO;

	@Override
	public void deleteByName(String name) {
		userInfoDAO.deleteByName(name);
	}

	@Override
	public boolean save(UserInfo user) {
		//System.out.println(user);
		try {
			userInfoDAO.insertUserInfo(user);
		} catch( SQLException sqle) {
			sqle.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean checkUserByName( String name ) {
		UserInfo user = userInfoDAO.findByUsername(name);
		if(user==null)
			return false;
		else {
			String foundName = user.getUsername();
			if( foundName.equals(name) )
				return true;
		}
		return false;
	}

	@Override
	public boolean checkAccount( String name, String encodePw ) {
		UserInfo userInfo = userInfoDAO.findByNameAndPw( name, encodePw );
		if( userInfo == null )
			return false;
		else
			return true;
	}

	@Override
	public List<UserInfo> getAllUserInfos(){
		return userInfoDAO.getAllUserInfos();
	}

	@Override
	public UserInfo findByUsername( String username ) {
		return userInfoDAO.findByUsername(username);
	}
}
