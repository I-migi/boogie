package team3.boogie.question;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

import team3.boogie.User.User;
import team3.boogie.User.UserService;
import team3.boogie.answer.AnswerForm;
import jakarta.servlet.http.HttpSession;


@RequiredArgsConstructor
@Controller
public class QuestionController {
    private final QuestionService questionService;
    private final UserService userService;
    private final HttpSession httpSession;

    @GetMapping("/question/list")
    public String list(Model model, @RequestParam(name = "sort", required = false) String sort) {
        List<Question> questionList;
        if ("vote".equals(sort)) {
            questionList = this.questionService.getListSortedByVote();
        } else {
            questionList = this.questionService.getListSortedByDate();
        }

        List<Question> top3Questions = this.questionService.getTop3QuestionsByVotes();
        model.addAttribute("top3Questions", top3Questions);

        model.addAttribute("questionList", questionList);
        return "question_list";
    }

    @GetMapping(value = "/question/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm, HttpSession session) {
        Question question = this.questionService.getQuestion(id);
        model.addAttribute("question", question);

        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);

        if (loggedInUser != null && loggedInUser.getId().equals(question.getAuthor().getId())) {
            return "question_detail_login";
        } else {
            return "question_detail";
        }
    }

    // 글 수정 페이지로 이동
    @GetMapping("/question/modify/{id}")
    public String questionModify(@PathVariable("id") Integer id, QuestionForm questionForm, HttpSession session) {
        Question question = this.questionService.getQuestion(id);

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null || !loggedInUser.getLoginId().equals(question.getAuthor().getLoginId())) {
            return "redirect:/question/detail/" + id;
        }

        questionForm.setSubject(question.getSubject());
        questionForm.setContent(question.getContent());
        return "question_form";
    }

    // 글 수정 처리
    @PostMapping("/question/modify/{id}")
    public String questionModify(@Valid QuestionForm questionForm, BindingResult bindingResult, HttpSession session, @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            // 로그인되지 않은 경우 로그인 페이지로 리디렉션
            return "redirect:/login";
        }

        Question question = this.questionService.getQuestion(id);

        // 현재 로그인한 사용자가 글쓴이인지 확인합니다.
        if (!question.getAuthor().getId().equals(loggedInUser.getId())) {
            // 글쓴이가 아니면 접근 금지
            return "redirect:/question/detail/" + id;
        }

        this.questionService.modify(question, questionForm.getSubject(), questionForm.getContent());
        return String.format("redirect:/question/detail/%s", id);
    }

    @GetMapping("/question/create")
    public String questionCreate(QuestionForm questionForm) {
        return "question_form";
    }

    @PostMapping("/question/create")
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

    // 추천 처리
    @GetMapping("/question/vote/{id}")
    public String questionVote(HttpSession session, @PathVariable("id") Integer id) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            // 로그인되지 않은 경우 로그인 페이지로 리디렉션
            return "redirect:/login";
        }
        Question question = this.questionService.getQuestion(id);
        this.questionService.vote(question, loggedInUser);
        return String.format("redirect:/question/detail/%s", id);
    }

    // 비추천 처리
    @GetMapping("/question/downvote/{id}")
    public String questionDownVote(HttpSession session, @PathVariable("id") Integer id) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            // 로그인되지 않은 경우 로그인 페이지로 리디렉션
            return "redirect:/login";
        }
        Question question = this.questionService.getQuestion(id);
        this.questionService.downvote(question, loggedInUser);
        return String.format("redirect:/question/detail/%s", id);
    }

    @GetMapping("/question/answer")
    public String questionAnswer() {
        return "question_answer";
    }

    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
        // 실시간 인기글 추가
        List<Question> top3Questions = this.questionService.getTop3QuestionsByVotes();
        model.addAttribute("top3Questions", top3Questions);

        // 사용자 정보 추가
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", loggedInUser);

        return "home";
    }
}
