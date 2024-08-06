package team3.boogie.User;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;

    @GetMapping("/login")
    public String showLoginPage(Model model, @RequestParam(required = false) String error) {
        if (error != null) {
            model.addAttribute("error", "아이디나 비밀번호가 다릅니다.");
        }
        return "html/login";
    }

//    @GetMapping("/mainPage")
//    public String mainPage(Model model, HttpSession session) {
//        User loggedInUser = (User) session.getAttribute("loggedInUser");
//        if(loggedInUser == null) {
//            throw new RuntimeException("잘못된 접근입니다");
//        }
//        model.addAttribute("loggedInUser",loggedInUser);
//        return "html/mainPage";
//    }

    @GetMapping("/register")
    public String showRegisterPage(Model model, @RequestParam(required = false) String error) {
        if (error != null) {
            model.addAttribute("error", "이미 존재하는 아이디입니다.");
        }
        return "html/register";
    }

    @GetMapping("/EndRegister")
    public String successAdd(){
        return "html/EndRegister";
    }



    @PostMapping("/addUser")
    public String addUser(@RequestParam String name, @RequestParam String loginId, @RequestParam String password, Model model) {
        if (!userService.isUserExists(loginId)) {
            User user = new User(name, loginId, password);
            userRepository.save(user);
            return "redirect:/EndRegister";
        } else {
            model.addAttribute("error", "이미 존재하는 아이디입니다.");
            return "/html/register";
        }
    }

    @PostMapping("/login")
    public String login(@RequestParam String loginId, @RequestParam String password, HttpSession httpSession, Model model) {
        boolean isLogin = userService.loginService(loginId, password);
        if (isLogin) {
            User user = userService.getUserByLoginId(loginId);
            httpSession.setAttribute("loggedInUser", user);
            return "redirect:/mainPage";
        } else {
            model.addAttribute("error", "아이디나 비밀번호가 다릅니다.");
            return "html/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession httpSession) {
        httpSession.removeAttribute("loggedInUser");
        return "redirect:/login";
    }

    @GetMapping("/checkDuplicate")
    @ResponseBody
    public Map<String, Boolean> checkDuplicate(@RequestParam String loginId) {
        boolean exists = userService.isUserExists(loginId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return response;
    }

    @GetMapping("/myPage")
    public String myPageForm() {
        return "html/MyPage";
    }
}
