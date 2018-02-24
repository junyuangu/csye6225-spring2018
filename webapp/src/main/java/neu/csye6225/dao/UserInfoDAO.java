package neu.csye6225.dao;


import neu.csye6225.entity.UserInfo;
<<<<<<< HEAD
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
=======
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.List;

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
>>>>>>> Assignment2
	}

	@Override
	public void insertUserInfo( UserInfo newUser ) throws SQLException {
<<<<<<< HEAD
		String sql = "INSERT into userinfo( id, uname, password, role, enabled  ) VALUES (?, ?, ?, ?, ?)";
		jdbcTemplate.update(
=======
		String sql = "INSERT into userinfo( id, username, password, role, enabled  ) VALUES (?, ?, ?, ?, ?)";
		this.jdbcTemplate.update(
>>>>>>> Assignment2
				sql,  new Object[] { newUser.getId(), newUser.getUsername(), newUser.getPassword(),
						newUser.getRole(), newUser.getEnabled() });
	}


	@Override
	public UserInfo findByNameAndPw(String username, String password) {
		String sql = "SELECT * FROM userinfo WHERE username = ? AND password = ?";
<<<<<<< HEAD
		UserInfo userInfo = (UserInfo)jdbcTemplate.queryForObject(
=======
		UserInfo userInfo = (UserInfo)this.jdbcTemplate.queryForObject(
>>>>>>> Assignment2
				sql, new Object[] { username, password }, new UserInfoRowMapper());

		return userInfo;
	}

	@Override
	public UserInfo findByUsername( String username ) {
		String sql = "SELECT * FROM userinfo WHERE username = ?";
<<<<<<< HEAD
		UserInfo userInfo = (UserInfo)jdbcTemplate.queryForObject(
				sql, new Object[] { username }, new UserInfoRowMapper());
=======
		UserInfo userInfo;
		RowMapper<UserInfo> rowMapper = new BeanPropertyRowMapper<UserInfo>(UserInfo.class);
		try {
			userInfo = jdbcTemplate.queryForObject(sql, new Object[]{username}, rowMapper);
		} catch (EmptyResultDataAccessException e) {
			userInfo = null;
		}
>>>>>>> Assignment2

		return userInfo;
	}

	@Override
	public UserInfo getActiveUser() {
		UserInfo activeUserInfo = new UserInfo();
		short enabled = 1;
		String sql = "SELECT * FROM userinfo WHERE enabled = ?";
<<<<<<< HEAD
		List<?> list = jdbcTemplate.queryForList(sql, new Object[]{enabled}, new UserInfoRowMapper() );
=======
		List<?> list = this.jdbcTemplate.queryForList(sql, new Object[]{enabled}, new UserInfoRowMapper() );
>>>>>>> Assignment2

		if( !list.isEmpty() ) {
			activeUserInfo = (UserInfo)list.get(0);
		}
		return activeUserInfo;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserInfo> getAllUserInfos() {
<<<<<<< HEAD
		String sql = "SELECT * FROM users";
=======
		String sql = "SELECT * FROM userinfo";
>>>>>>> Assignment2
		RowMapper<UserInfo> rowMapper = new UserInfoRowMapper();

		return (List<UserInfo>)this.jdbcTemplate.query(sql, rowMapper);
	}

}