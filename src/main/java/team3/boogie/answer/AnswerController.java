package team3.boogie.answer;

import team3.boogie.question.Question;
import team3.boogie.question.QuestionService;
import team3.boogie.User.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

@RequestMapping("/answer")
@RequiredArgsConstructor
@Controller
public class AnswerController {

    private final QuestionService questionService;
    private final AnswerService answerService;

    @PostMapping("/create/{id}")
    public String createAnswer(Model model, @PathVariable("id") Integer id,
                               @Valid AnswerForm answerForm, BindingResult bindingResult,
                               HttpSession session) {
        // 질문 정보를 가져옵니다.
        Question question = this.questionService.getQuestion(id);
        if (question == null) {
            return "redirect:/PopularPost";
        }

        // 사용자 정보 가져오기
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        // 입력 데이터 검증
        if (bindingResult.hasErrors()) {
            model.addAttribute("question", question);
            model.addAttribute("loggedInUser", loggedInUser);
            // 로그인한 사용자 여부에 따라 다른 뷰를 반환합니다.
            if (loggedInUser != null && loggedInUser.getId().equals(question.getAuthor().getId())) {
                return "html/OpenPostPage";
            } else {
                return "html/OpenPostPage2";
            }
        }

        // 답변 생성
        this.answerService.create(question, answerForm.getContent());

        // 답변이 생성된 후 질문 상세 페이지로 리다이렉트
        return String.format("redirect:/OpenPostPage2/%s", id);
    }
}
