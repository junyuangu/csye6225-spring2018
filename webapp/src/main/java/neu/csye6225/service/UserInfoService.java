package neu.csye6225.service;

import neu.csye6225.dao.IDescriptionRepository;
import neu.csye6225.dao.IPictureRepository;
import neu.csye6225.dao.UserInfoDAO;
import neu.csye6225.entity.Description;
import neu.csye6225.entity.Picture;
import neu.csye6225.entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.List;

/**
 * @author  Junyuan GU
 * @NUid    001825583
 */
//@EnableWebMvc
@Service
@Transactional
public class UserInfoService implements IUserInfoService {
	//@Autowired
	@Resource
	private UserInfoDAO userInfoDAO;

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
		if( userInfo==null )
			return false;

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

	@Override
	public String getPicturePath( int userId ) {
		String picPath = new String();
		for (Picture picture : pictureRepository.findAll()) {
			if( picture.getUserId()==userId ){
				 picPath = picture.getPicpath();
				 break;
			}
		}
		return picPath;
	}

	@Override
	public String getDescriptionContent( int userId ) {
		String descriptionContent = new String();
		for( Description desContent : descriptionRepository.findAll() ) {
			if( desContent.getUserId()==userId ){
				descriptionContent = desContent.getDescription();
				break;
			}
		}
		return descriptionContent;
	}

}
