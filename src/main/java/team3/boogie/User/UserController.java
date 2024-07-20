package team3.boogie.User;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;
    private HttpSession httpSession;

    @PostMapping("/addUser")
    public void addUser(@ModelAttribute("user") User user){
        if(!userService.isUserExists(user.getLoginId())){
            userRepository.save(user);
        }
    }

    @PostMapping("/addUser/checkValidate")
    public ResponseEntity<Boolean> isValidate(@RequestParam String loginId){
        boolean isValid = userService.isUserExists(loginId);
        return ResponseEntity.ok(!isValid);
    }

    @PostMapping("/login")
    public ResponseEntity<Boolean> login(@ModelAttribute("user")User user,HttpSession httpSession){
        boolean isLogin = userService.loginService(user.getLoginId(),user.getPassword());
        if(isLogin){
            httpSession.setAttribute("loggedInUser",user.getName());
        }
        return ResponseEntity.ok(isLogin);
    }

    @GetMapping("/logout")
    public void logout(){
        httpSession.removeAttribute("loggedInUser");
    }
}