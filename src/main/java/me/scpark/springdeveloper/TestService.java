package me.scpark.springdeveloper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TestService {

    @Autowired
    TestRepository testRepository;

    public List<Member> getAllMembers() {
        return testRepository.findAll();
    }

    public Member addMember(Member member) {
        return testRepository.save(member);
    }
}