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
    public ResponseEntity<String> addUser(@ModelAttribute("user") User user){
        if(!userService.isUserExists(user.getLoginId())){
            userRepository.save(user);
            return ResponseEntity.ok("User added successfully");
        } else{
            return ResponseEntity.badRequest().body("User already exists");
        }
    }

    @PostMapping("/addUser/checkValidate")
    public ResponseEntity<Boolean> isValidate(@RequestParam String loginId){
        boolean isValid = userService.isUserExists(loginId);
        return ResponseEntity.ok(!isValid);
    }

    @PostMapping("/login")
    public ResponseEntity<Boolean> login(@RequestBody LoginRequest loginRequest,HttpSession httpSession){
        boolean isLogin = userService.loginService(loginRequest.getLoginId(),loginRequest.getPassword());
        if(isLogin){
            User user = userService.getUserByLoginId(loginRequest.getLoginId());
            httpSession.setAttribute("loggedInUser",user);
        }
        return ResponseEntity.ok(isLogin);
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpSession httpSession){
        httpSession.removeAttribute("loggedInUser");
        return ResponseEntity.ok("Logged out succesfully");
    }
}