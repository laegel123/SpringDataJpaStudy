package jpabook.jpashop.dto;

import jpabook.jpashop.entity.Member;
import lombok.Data;

@Data
public class MemberDto {

    private Long id;
    private String username;
    private int age;

    public MemberDto(Long id, String username, int age) {
        this.id = id;
        this.username = username;
        this.age = age;
    }

    public MemberDto(Member member) {
        this.id = member.getId();
        this.username = member.getUsername();
        this.age = member.getAge();
    }
}
