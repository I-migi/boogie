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

    @GetMapping("/popularPosts")
    public String popularPosts(Model model, @RequestParam(name = "sort", required = false) String sort) {
        List<Question> questionList;
        if ("vote".equals(sort)) {
            questionList = this.questionService.getListSortedByVote();
        } else {
            questionList = this.questionService.getListSortedByDate();
        }

        List<Question> top3Questions = this.questionService.getTop3QuestionsByVotes();
        model.addAttribute("top3Questions", top3Questions);

        model.addAttribute("questionList", questionList);
        return "html/PopularPost";
    }

    @GetMapping(value = "/OpenPostPage2/{id}")
    public String OpenPostPage2(Model model, @PathVariable("id") Integer id, AnswerForm answerForm, HttpSession session) {
        Question question = this.questionService.getQuestion(id);
        model.addAttribute("question", question);

        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);

        if (loggedInUser != null && loggedInUser.getId().equals(question.getAuthor().getId())) {
            return "html/OpenPostPage";
        } else {
            return "html/OpenPostPage2";
        }
    }

    // 글 수정 페이지로 이동
    @GetMapping("/question/modify/{id}")
    public String questionModify(@PathVariable("id") Integer id, QuestionForm questionForm, HttpSession session, Model model) {
        Question question = this.questionService.getQuestion(id);

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null || !loggedInUser.getLoginId().equals(question.getAuthor().getLoginId())) {
            return "redirect:/OpenPostPage2/" + id;
        }

        questionForm.setSubject(question.getSubject());
        questionForm.setContent(question.getContent());

        model.addAttribute("question", question);
        model.addAttribute("loggedInUser", loggedInUser);
        return "html/WritingPage";
    }

    // 글 수정 처리
    @PostMapping("/question/modify/{id}")
    public String questionModify(@Valid QuestionForm questionForm, BindingResult bindingResult, HttpSession session, @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "html/WritingPage";
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
            return "redirect:/OpenPostPage2/" + id;
        }

        this.questionService.modify(question, questionForm.getSubject(), questionForm.getContent());
        return String.format("redirect:/OpenPostPage2/%s", id);
    }

    @GetMapping("/WritingPage")
    public String WritingPage(QuestionForm questionForm, HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);
        return "html/WritingPage";
    }

    @PostMapping("/WritingPage")
    public String WritingPage(@Valid QuestionForm questionForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "html/WritingPage";
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
        return "redirect:/PopularPost";
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
        return String.format("redirect:/OpenPostPage2/%s", id);
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
        return String.format("redirect:/OpenPostPage2/%s", id);
    }

    @GetMapping("/WriteAdvise")
    public String WriteAdvise() {
        return "html/WriteAdvise";
    }

    @GetMapping("/mainPage")
    public String mainPage(Model model, HttpSession session) {
        // 사용자 정보 확인
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            throw new RuntimeException("잘못된 접근입니다");
        }
        model.addAttribute("loggedInUser", loggedInUser);

        // 실시간 인기글 추가
        List<Question> top3Questions = this.questionService.getTop3QuestionsByVotes();
        model.addAttribute("top3Questions", top3Questions);

        return "html/mainPage";
    }
}
