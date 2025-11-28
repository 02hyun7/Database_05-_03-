package org.example.service;

import org.example.dao.MemberDao;
import org.example.model.Member;


public class MemberService {

    private final MemberDao memberDao;

    public MemberService() {
        this.memberDao = new MemberDao();
    }

    // 1) 회원 가입
    public Member registerMember(String email, String name) {
        try {
            Member m = new Member();
            m.setEmail(email);
            m.setName(name);
            m.setTierId(null);

            return memberDao.create(m);

        } catch (Exception e) {
            System.out.println("[ERROR] 회원 등록 실패: " + e.getMessage());
            return null;
        }
    }


    // 2) 회원 로그인 (이메일 기준)
    //    - 존재하면 그대로 반환
    //    - 없으면 null 반환
    public Member login(String email) {
        try {
            return memberDao.findByEmail(email);
        } catch (Exception e) {
            System.out.println("[ERROR] 로그인 실패: " + e.getMessage());
            return null;
        }
    }

    // 3) 회원 로그인 + 자동 회원가입
    //    - 이미 있으면 그 회원 반환
    //    - 없으면 새로 만들고 반환
    public Member loginOrRegister(String email, String name) {
        try {
            Member existing = memberDao.findByEmail(email);
            if (existing != null) {
                return existing;
            }
            // 없으면 회원 생성
            return registerMember(email, name);
        } catch (Exception e) {
            System.out.println("[ERROR] loginOrRegister 실패: " + e.getMessage());
            return null;
        }
    }


    // 4) 회원 단건 조회
    public Member getMemberById(Long id) {
        try {
            return memberDao.findById(id);
        } catch (Exception e) {
            System.out.println("[ERROR] 회원 조회 실패: " + e.getMessage());
            return null;
        }
    }

}