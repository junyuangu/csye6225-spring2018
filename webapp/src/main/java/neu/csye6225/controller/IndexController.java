package neu.csye6225.controller;


import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController implements ErrorController {

    private static final String PATH = "/error";

    @RequestMapping(value = PATH)
    public String error() {
<<<<<<< HEAD
        return "Error handling";
=======
        return "Error Page: you see this page because some error happens.";
>>>>>>> Assignment2
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}
