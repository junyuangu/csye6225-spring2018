package neu.csye6225.service;

import neu.csye6225.dao.IUserInfoDAO;
import neu.csye6225.entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
@Service
public class UserInfoService implements IUserInfoService {
	@Autowired
	private IUserInfoDAO userInfoDAO;

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
