package me.scpark.springdeveloper;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Hellowolrdcontroller {
    @GetMapping("/test")
    public String hello() {
        return "Hello World";
    }
    // https://localhost/student?firstName=Saetbyeol&lastName=Lee
    @GetMapping("/student")
    public Student getStudent(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName) {
        return new Student(firstName, lastName);
    }

    //http://localhost/student/
    @GetMapping("/student/{firstName}/{lastName}")
    public Student getStudent2(@PathVariable("firstName") String firstName, @PathVariable("lastName") String lastName){
        return new Student(firstName, lastName);
    }
}