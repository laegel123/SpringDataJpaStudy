package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.jpashop.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
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

    @PersistenceContext
    EntityManager em;

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

    @Test
    public void paging() throws Exception {
        // given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        // when
        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        // then
        List<Member> content = page.getContent();

        assertEquals(content.size(), 3);
        assertEquals(page.getTotalElements(), 5);
        assertEquals(page.getNumber(), 0);
        assertEquals(page.getTotalPages(), 2);
        assertTrue(page.isFirst());
        assertTrue(page.hasNext());

    }

    @Test
    public void slice() throws Exception {
        // given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        // when
        Slice<Member> page = memberRepository.findByAge(age, pageRequest);

        // then
        List<Member> content = page.getContent();

        assertEquals(content.size(), 3);
        assertEquals(page.getNumber(), 0);
        assertTrue(page.isFirst());
        assertTrue(page.hasNext());

    }

    @Test
    public void bulkUpdate() throws Exception {
        // given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 40));

        // when
        int resultCount = memberRepository.bulkAgePlus(20);

        // then
        assertEquals(resultCount, 3);

    }

    @Test
    public void entityGraphTest() throws Exception {
        List<Member> members = memberRepository.findAll();

    }

    @Test
    public void queryHint() throws Exception {
        // given
        Member member = memberRepository.save(new Member("member1", 10));
        em.flush();
        em.clear();

        // when
        Member findMember = memberRepository.findReadOnlyByUsername("member1");
        findMember.changeUsername("member2");

        em.flush();

    }

    @Test
    public void lock() throws Exception {
        // given
        Member member = memberRepository.save(new Member("member1", 10));
        em.flush();
        em.clear();

        // when
        List<Member> members = memberRepository.findLockByUsername("member1");

        em.flush();

    }



}
