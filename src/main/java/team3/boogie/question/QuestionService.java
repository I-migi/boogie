package team3.boogie.question;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.Optional;
import team3.boogie.DataNotFoundException;

import java.time.LocalDateTime;
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
}
