package me.scpark.springdeveloper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TestController {

    @Autowired
    TestService testService;

    @GetMapping("/test")
    public List<Member> getAllMembers() {
        return testService.getAllMembers();
    }

    @PostMapping("/test")
    public Member addMember(@RequestBody Member member) {
        return testService.addMember(member);
    }
}