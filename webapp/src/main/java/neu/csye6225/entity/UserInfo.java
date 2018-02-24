package neu.csye6225.entity;

<<<<<<< HEAD
import org.hibernate.validator.constraints.NotBlank;
import javax.validation.constraints.NotNull;
=======
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
>>>>>>> Assignment2

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="userinfo")
public class UserInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="id")
	private int id;
<<<<<<< HEAD
	@Column(name="username")
	private String uname;
	@Column(name="password")
	private String password;
=======

	@NotEmpty(message = "Username is Empty")
	@Email( message = "Invalid Email Address" )
	@Length( min = 3, max = 55, message = "Email Address's length is between 3-55")
	@Column(name="username")
	private String uname;

	@NotEmpty(message = "Password is Empty")
	@Length(min = 8, message = "Password Length Less Than 8")
	@Column(name="password")
	private String password;

>>>>>>> Assignment2
	@Column(name="role")	
	private String role;
	@Column(name="enabled")	
	private short enabled;

	public UserInfo() {};

<<<<<<< HEAD
=======
	public UserInfo(String username) {
		this.uname = username;
	}

>>>>>>> Assignment2
	public UserInfo(String username, String password) {
		Date date = new Date();
		int id = (int)date.getTime();
		id = id + (int)(Math.random()*65536);
<<<<<<< HEAD
		this.id = id;
=======
		this.id = Math.abs(id);
>>>>>>> Assignment2
		this.uname = username;
		this.password = password;
		this.role = "ROLE_USER";
		this.enabled = 1;
	}

	// Setter and getter
	public int getId() { return id; }
	public void setId(int id) { this.id = id; }
<<<<<<< HEAD
	@NotBlank(message="username cannot be blank.")
=======
	@NotEmpty(message="username cannot be empty.")
>>>>>>> Assignment2
	public String getUsername() {
		return uname;
	}
	public void setUsername(String username) { this.uname = username; }
<<<<<<< HEAD
	@NotNull(message="password cannot be null.")
=======
	@NotEmpty(message="password cannot be empty.")
>>>>>>> Assignment2
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public short getEnabled() {
		return enabled;
	}
	public void setEnabled(short enabled) {
		this.enabled = enabled;
	}

	public String toString() {
		return "id: " + getId() + ", username: " + getUsername() + ", password: "
				+ getPassword() + ", role: " + getRole() + ", enabled: " + getEnabled();
	}
}
