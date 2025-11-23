package org.example;

import org.example.model.Member;
import org.example.service.MemberService;

public class Main {
    public static void main(String[] args) {

        MemberService memberService = new MemberService();

        // 1) 회원가입
        Member registered = memberService.registerMember("test1@example.com", "테스트1");
        System.out.println("회원가입 결과: " + registered);

        // 2) 로그인 (이메일로 조회)
        Member login1 = memberService.login("test1@example.com");
        System.out.println("로그인 결과: " + login1);

        // 3) 로그인 + 자동가입
        Member login2 = memberService.loginOrRegister("test2@example.com", "테스트2");
        System.out.println("loginOrRegister 결과: " + login2);
    }
}
