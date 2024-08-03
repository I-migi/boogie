package team3.boogie;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import lombok.RequiredArgsConstructor;

@Controller
public class HomeController {

    @GetMapping("/aaa")
    public String home(){
        return "html/login";
    }
}

