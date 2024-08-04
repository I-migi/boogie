package team3.boogie.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class LoginRequest {

    @NotBlank
    private String loginId;

    @NotBlank
    @Size(min = 8, max = 30, message = "비밀번호는 8자 이상, 30자 이하로 입력하세요.")
    private String password;


}
