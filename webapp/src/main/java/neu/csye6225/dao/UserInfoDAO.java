package neu.csye6225.dao;


import neu.csye6225.entity.UserInfo;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.List;

/**
 * @author  Junyuan GU
 * @NUid    001825583
 */
@Profile("dev")
//@EnableWebMvc
@Repository
//@Transactional
public class UserInfoDAO implements IUserInfoDAO {
	//@Autowired
	@Resource
	private JdbcTemplate jdbcTemplate;

	@Override
	public void deleteByName(String name) {
		jdbcTemplate.update("delete from userinfo where username = ?", name);
	}

	@Override
	public void insertUserInfo( UserInfo newUser ) throws SQLException {
		String sql = "INSERT into userinfo( id, username, password, role, enabled  ) VALUES (?, ?, ?, ?, ?)";
		this.jdbcTemplate.update(
				sql,  new Object[] { newUser.getId(), newUser.getUsername(), newUser.getPassword(),
						newUser.getRole(), newUser.getEnabled() });
	}


	@Override
	public UserInfo findByNameAndPw(String username, String password)  {
		String sql = "SELECT * FROM userinfo WHERE username = ? AND password = ?";
		UserInfo userInfo;
		try {
			userInfo = (UserInfo)this.jdbcTemplate.queryForObject(
					sql, new Object[] { username, password }, new UserInfoRowMapper());
		}catch( EmptyResultDataAccessException e ) {
			userInfo = null;
		}

		return userInfo;
	}

	@Override
	public UserInfo findByUsername( String username ) {
		String sql = "SELECT * FROM userinfo WHERE username = ?";
		UserInfo userInfo;
		RowMapper<UserInfo> rowMapper = new BeanPropertyRowMapper<UserInfo>(UserInfo.class);
		try {
			userInfo = jdbcTemplate.queryForObject(sql, new Object[]{username}, rowMapper);
		} catch (EmptyResultDataAccessException e) {
			userInfo = null;
		} catch( IncorrectResultSizeDataAccessException e ) {
			userInfo = null;
		}

		return userInfo;
	}

	@Override
	public UserInfo getActiveUser() {
		UserInfo activeUserInfo = new UserInfo();
		short enabled = 1;
		String sql = "SELECT * FROM userinfo WHERE enabled = ?";
		List<?> list = this.jdbcTemplate.queryForList(sql, new Object[]{enabled}, new UserInfoRowMapper() );

		if( !list.isEmpty() ) {
			activeUserInfo = (UserInfo)list.get(0);
		}
		return activeUserInfo;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserInfo> getAllUserInfos() {
		String sql = "SELECT * FROM userinfo";
		RowMapper<UserInfo> rowMapper = new UserInfoRowMapper();

		return (List<UserInfo>)this.jdbcTemplate.query(sql, rowMapper);
	}

}