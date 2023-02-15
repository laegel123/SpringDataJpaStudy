package jpabook.jpashop.repository;

import jpabook.jpashop.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void testMember() {
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(savedMember.getId()).get();

        assertEquals(savedMember.getId(), findMember.getId());
        assertEquals(member.getUsername(), findMember.getUsername());
        assertEquals(member, findMember);
    }

    @Test
    public void findByUsernameAndAgeGreaterThen() throws Exception {
        // given
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("AAA", 20);

        memberRepository.save(member1);
        memberRepository.save(member2);

        // when
        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        // then
        assertEquals(result.get(0).getUsername(), "AAA");
        assertEquals(result.get(0).getAge(), 20);
        assertEquals(result.size(), 1);

    }
    
    @Test
    public void testQuery() throws Exception {
        // given
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("AAA", 20);

        memberRepository.save(member1);
        memberRepository.save(member2);
        // when
        List<Member> findMember = memberRepository.findUser("AAA", 10);
        // then
        assertEquals(findMember.get(0), member1);

    }

}
