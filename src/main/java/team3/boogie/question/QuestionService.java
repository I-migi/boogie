package team3.boogie.question;

import java.util.Comparator;
import java.util.List;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.Optional;
import team3.boogie.DataNotFoundException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import team3.boogie.User.User;

@RequiredArgsConstructor
@Service
public class QuestionService {
    private final QuestionRepository questionRepository;

    public List<Question> getList(){
        return this.questionRepository.findAll();
    }

    public Question getQuestion(Integer id){
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
    //수정
    @Transactional
    public void modify(Question question, String subject, String content) {
        question.setSubject(subject);
        question.setContent(content);
        this.questionRepository.save(question);
    }

//추천인을 저장
    public void vote(Question question, User loggedInUser) {
        question.getVoter().add(loggedInUser);
        this.questionRepository.save(question);
    }
    //비추천
    public void downvote(Question question, User loggedInUser) {
        question.getNonVoter().add(loggedInUser);
        this.questionRepository.save(question);
    }

    //정렬
    public List<Question> getListSortedByDate() {
        List<Question> questions = this.questionRepository.findAll();
        questions.sort(Comparator.comparing(Question::getCreateDate).reversed());
        return questions;
    }

    public List<Question> getListSortedByVote() {
        List<Question> questions = this.questionRepository.findAll();
        questions.sort((q1, q2) -> q2.getVoter().size() - q1.getVoter().size());
        return questions;
    }
    public List<Question> getTop3QuestionsByVotes() {
        List<Question> questions = this.questionRepository.findAll();
        return questions.stream()
                .sorted((q1, q2) -> q2.getVoter().size() - q1.getVoter().size())
                .limit(3)
                .collect(Collectors.toList());
    }
}
