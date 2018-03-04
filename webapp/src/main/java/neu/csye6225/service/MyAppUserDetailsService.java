package neu.csye6225.service;

import neu.csye6225.dao.IUserInfoDAO;
import neu.csye6225.entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;

/**
 * @author  Junyuan GU
 * @NUid    001825583
 */
public class MyAppUserDetailsService implements UserDetailsService {
	@Autowired
	private IUserInfoDAO userInfoDAO;
	@Override
	public UserDetails loadUserByUsername(String name)
			throws UsernameNotFoundException {
		UserInfo userFound = userInfoDAO.findByUsername(name);

		GrantedAuthority authority = new SimpleGrantedAuthority(userFound.getRole());

		UserDetails userDetails = (UserDetails)new User(userFound.getUsername(),
				userFound.getPassword(), Arrays.asList(authority));
		return userDetails;
	}
}

