package team3.boogie.Member;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    @Autowired
    private HttpSession httpSession;

    @PostMapping("/addMemberForm")
    public String addMember(@ModelAttribute("member")Member member){
        if(memberRepository.findByName(member.getName()).isEmpty()){
            memberRepository.save(member);
            return "redirect:/";
        }else{
            return "redirect:/addMemberForm?error=true";
        }
    }


}
