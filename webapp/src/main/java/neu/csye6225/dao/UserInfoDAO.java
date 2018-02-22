package neu.csye6225.dao;


import neu.csye6225.entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;


@Repository
@Transactional
public class UserInfoDAO implements IUserInfoDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public UserInfoDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void insertUserInfo( UserInfo newUser ) throws SQLException {
		String sql = "INSERT into userinfo( id, uname, password, role, enabled  ) VALUES (?, ?, ?, ?, ?)";
		jdbcTemplate.update(
				sql,  new Object[] { newUser.getId(), newUser.getUsername(), newUser.getPassword(),
						newUser.getRole(), newUser.getEnabled() });
	}


	@Override
	public UserInfo findByNameAndPw(String username, String password) {
		String sql = "SELECT * FROM userinfo WHERE username = ? AND password = ?";
		UserInfo userInfo = (UserInfo)jdbcTemplate.queryForObject(
				sql, new Object[] { username, password }, new UserInfoRowMapper());

		return userInfo;
	}

	@Override
	public UserInfo findByUsername( String username ) {
		String sql = "SELECT * FROM userinfo WHERE username = ?";
		UserInfo userInfo = (UserInfo)jdbcTemplate.queryForObject(
				sql, new Object[] { username }, new UserInfoRowMapper());

		return userInfo;
	}

	@Override
	public UserInfo getActiveUser() {
		UserInfo activeUserInfo = new UserInfo();
		short enabled = 1;
		String sql = "SELECT * FROM userinfo WHERE enabled = ?";
		List<?> list = jdbcTemplate.queryForList(sql, new Object[]{enabled}, new UserInfoRowMapper() );

		if( !list.isEmpty() ) {
			activeUserInfo = (UserInfo)list.get(0);
		}
		return activeUserInfo;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserInfo> getAllUserInfos() {
		String sql = "SELECT * FROM users";
		RowMapper<UserInfo> rowMapper = new UserInfoRowMapper();

		return (List<UserInfo>)this.jdbcTemplate.query(sql, rowMapper);
	}

}