package team3.boogie;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import lombok.RequiredArgsConstructor;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(){
        return "html/login";
    }
}

