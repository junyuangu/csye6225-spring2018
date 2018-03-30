package neu.csye6225.dao;


import neu.csye6225.entity.UserInfo;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author  Junyuan GU
 * @NUid    001825583
 */
@Profile("dev")
public class UserInfoRowMapper implements RowMapper<UserInfo> {
    @Override
    public UserInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(rs.getInt("id"));
        userInfo.setUsername(rs.getString("username"));
        userInfo.setPassword(rs.getString("password"));
        userInfo.setRole(rs.getString("role"));
        userInfo.setEnabled(rs.getShort("enabled"));
        return userInfo;
    }
}
