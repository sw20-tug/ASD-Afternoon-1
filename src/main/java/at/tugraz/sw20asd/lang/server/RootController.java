package at.tugraz.sw20asd.lang.server;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {
    @RequestMapping("/")
    public String index() {
        return "Greeting from RootController (default)";
    }
}
