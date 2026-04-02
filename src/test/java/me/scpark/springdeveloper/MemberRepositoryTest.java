package me.scpark.springdeveloper;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @Sql("/insert-members.sql")
    @Test
    void getAllMembers() {
        //given(준비)
        //when(실행)
        List<Member> members = memberRepository.findAll();
        //then(검증)
        assertThat(members.size()).isEqualTo(3);
    }

    @Sql("/insert-members.sql")
    @Test
    void getMemberById() {

        Member member = memberRepository.findById(2L).get();

        assertThat(member.getName()).isEqualTo("B");
    }

    void findMemberByName(int id) {

        Member member = memberRepository.findByName("C").get();

        assertThat(member.getId()).isEqualTo(3);


    }

    @DisplayName("레코드 삽입 테스트")
    @Test
    void saveMember() {
        Member m = new Member(null, "scpark");

        Member savedMember = memberRepository.save(m);

        assertThat(savedMember.getId()).isNotNull();

       assertThat(memberRepository.findById(savedMember.getId()).get().getName()).isEqualTo("scpark");
    }

    @DisplayName("2개의 레코드를 한 번에 삽입하는 테스트")
    @Test
    void saveMembers() {
        List<Member> members = List.of(new Member("HongGilDong"),
                new Member("Park Munsu"));

        memberRepository.saveAll(members);

        assertThat(memberRepository.findAll().size()).isEqualTo(2);
    }

@Sql("/insert-members.sql")
@DisplayName("레코드 삭제 테스트")
@Test
void deleteAll(){
    memberRepository.deleteAll();

    assertThat(memberRepository.findAll().size()).isEqualTo(0);
}


@Sql("/insert-members.sql")
@DisplayName("Update Test")
@Test
void update() {
    Member member = memberRepository.findById(2L).get();

    member.changeName("scpark");

    assertThat(memberRepository.findById(2L).get().getName()).isEqualTo("scpark");
}
}