package neu.csye6225.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author  Junyuan GU
 * @NUid    001825583
 * @date    02/15/2018
 */
@RestController
public class IndexController implements ErrorController {

    private static final String PATH = "/error";

    @RequestMapping(value = PATH)
    public String error() {
        return "Error Page: you see this page because some error happens.";
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }

    @Autowired
    private Environment env;


    @RequestMapping("/testProfile")
    @Profile("test")
    public String testProfile(){
        return env.getProperty("profile");
    }
}
