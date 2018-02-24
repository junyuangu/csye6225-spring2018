package neu.csye6225;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({"neu.csye6225.controller", "neu.csye6225.dao", "neu.csye6225.service", "neu.csye6225.entity",
	"neu.csye6225.Util"})
@SpringBootApplication
public class MyApplication {  
	public static void main(String[] args) {
		/*
		String enPassword = BCrypt.hashpw("1234", BCryptUtil.SALT);
		System.out.println( enPassword );
		String enPassword2 = BCrypt.hashpw("root1234", BCryptUtil.SALT);
		System.out.println( enPassword2 );*/

		SpringApplication.run(MyApplication.class, args);
    }       
}            