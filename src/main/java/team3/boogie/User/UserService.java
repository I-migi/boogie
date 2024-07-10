package team3.boogie.User;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;


    public boolean isUserExists(String loginId) {
        return userRepository.existsByLoginId(loginId);
    }

    public boolean loginService(String loginId,String password){
        User user = userRepository.findByLoginId(loginId).orElse((null));
        if(user == null){
            return false;
        }
        return password.equals(user.getPassword());
    }


}
