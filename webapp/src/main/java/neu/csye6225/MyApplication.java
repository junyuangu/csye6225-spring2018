package neu.csye6225;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;

@ComponentScan({"neu.csye6225.controller", "neu.csye6225.dao", "neu.csye6225.service", "neu.csye6225.entity",
        "neu.csye6225.Util"})
@SpringBootApplication
@Profile({"aws","dev","test"})
public class MyApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources( MyApplication.class );
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run( MyApplication.class, args );
    }

}
