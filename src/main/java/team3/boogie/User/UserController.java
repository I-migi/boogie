package team3.boogie.User;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;


    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    @PostMapping("/addUser")
    public String addUser(@RequestParam String name, @RequestParam String loginId, @RequestParam String password) {
        if (!userService.isUserExists(loginId)) {
            User user = new User(name, loginId, password);
            userRepository.save(user);
            return "redirect:/";
        } else {
            return "redirect:/register?error";
        }
    }

    @PostMapping("/login")
    public String login(@RequestParam String loginId, @RequestParam String password, HttpSession httpSession) {
        boolean isLogin = userService.loginService(loginId, password);
        if (isLogin) {
            User user = userService.getUserByLoginId(loginId);
            httpSession.setAttribute("loggedInUser", user);
            return "redirect:/home";
        } else {
            return "redirect:/login?error";
        }
    }

    @GetMapping("/home")
    public String showHomePage(HttpSession httpSession, Model model) {
        User loggedInUser = (User) httpSession.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", loggedInUser);
        return "home";
    }

    @GetMapping("/logout")
    public String logout(HttpSession httpSession) {
        httpSession.removeAttribute("loggedInUser");
        return "redirect:/login";
    }
}
