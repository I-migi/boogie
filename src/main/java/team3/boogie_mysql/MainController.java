package team3.boogie_mysql;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {
    @GetMapping("/sbb")
    @ResponseBody
    public String index() {
        return "안녕하세요 부기 페이지를 가장 잘 만든 것 같습니다!";
    }
    @GetMapping("/")
    public String root(){
        return "redirect:/question/list";
    }
}