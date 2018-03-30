package neu.csye6225;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;

/**
 * @author  Junyuan GU
 * @NUid    001825583
 */
@ComponentScan({"neu.csye6225.controller", "neu.csye6225.dao", "neu.csye6225.service", "neu.csye6225.entity",
	"neu.csye6225.Util"})
@SpringBootApplication
@Profile("test")
public class MyApplicationSpringBoot {
	public static void main(String[] args) {
		/*
		String enPassword = BCrypt.hashpw("1234", BCryptUtil.SALT);
		System.out.println( enPassword );
		String enPassword2 = BCrypt.hashpw("root1234", BCryptUtil.SALT);
		System.out.println( enPassword2 );*/

		SpringApplication.run(MyApplicationSpringBoot.class, args);
    }       
}            