package team3.boogie.question;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

import team3.boogie.User.User;
import team3.boogie.User.UserService;
import team3.boogie.answer.AnswerForm;
import jakarta.servlet.http.HttpSession;

@RequestMapping("/question")
@RequiredArgsConstructor
@Controller
public class QuestionController {
    private final QuestionService questionService;
    private final UserService userService;
    private final HttpSession httpSession;

    @GetMapping("/list")
    public String list(Model model) {
        List<Question> questionList = this.questionService.getList();
        model.addAttribute("questionList",questionList);
        return "question_list";
    }

    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm) {
        Question question = this.questionService.getQuestion(id);
        model.addAttribute("question", question);
        return "question_detail";
    }

    @GetMapping("/create")
    public String questionCreate(QuestionForm questionForm) {
        return "question_form";
    }

    @PostMapping("/create")
    public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }

        // HttpSession에서 'loggedInUser' 속성을 가져옵니다. 여기서는 User 객체가 저장되어 있어야 합니다.
        User loggedInUser = (User) httpSession.getAttribute("loggedInUser");

        // 세션에서 User 객체가 없으면 로그인 페이지로 리다이렉트합니다.
        if (loggedInUser == null) {
            return "redirect:/user/login";
        }
        // User 객체에서 loginId를 직접 가져옵니다.
        String loginId = loggedInUser.getLoginId();

        User author = userService.getUserByLoginId(loginId);

        this.questionService.create(questionForm.getSubject(), questionForm.getContent(), author);
        return "redirect:/question/list";
    }
}
