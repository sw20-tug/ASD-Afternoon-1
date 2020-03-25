package at.tugraz.sw20asd.lang.server;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class VocabularyController {

    @RequestMapping("/")
    public String index() {
        return "Greeting from LANG/VocabularyController";
    }
}
