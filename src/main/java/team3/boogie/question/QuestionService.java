package team3.boogie.question;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import team3.boogie.DataNotFoundException;
import java.time.LocalDateTime;
import team3.boogie.User.User;

@RequiredArgsConstructor
@Service
public class QuestionService {
    private final QuestionRepository questionRepository;

    public List<Question> getList() {
        return this.questionRepository.findAll();
    }

    public Question getQuestion(Integer id) {
        Optional<Question> question = this.questionRepository.findById(id);
        if (question.isPresent()) {
            return question.get();
        } else {
            throw new DataNotFoundException("question not found");
        }
    }

    public void create(String subject, String content, User author) {
        Question q = new Question();
        q.setSubject(subject);
        q.setContent(content);
        q.setCreateDate(LocalDateTime.now());
        q.setAuthor(author);
        this.questionRepository.save(q);
    }

    @Transactional
    public void modify(Question question, String subject, String content) {
        question.setSubject(subject);
        question.setContent(content);
        this.questionRepository.save(question);
    }

    public void vote(Question question, User loggedInUser) {
        question.getVoter().add(loggedInUser);
        this.questionRepository.save(question);
    }

    public void downvote(Question question, User loggedInUser) {
        question.getNonVoter().add(loggedInUser);
        this.questionRepository.save(question);
    }

    public List<Question> getListSortedByDate() {
        return this.questionRepository.findAll().stream()
                .sorted(Comparator.comparing(Question::getCreateDate).reversed())
                .collect(Collectors.toList());
    }

    public List<Question> getListSortedByVote() {
        return this.questionRepository.findAll().stream()
                .sorted((q1, q2) -> q2.getVoter().size() - q1.getVoter().size())
                .collect(Collectors.toList());
    }

    public List<Question> getTop3QuestionsByVotes() {
        return this.questionRepository.findAll().stream()
                .sorted((q1, q2) -> q2.getVoter().size() - q1.getVoter().size())
                .limit(3)
                .collect(Collectors.toList());
    }
}
