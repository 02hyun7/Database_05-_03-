package org.example.model;

import java.time.LocalDateTime;

public class Member {

    private Long memberId;
    private String email;
    private String name;
    private Long tierId;
    private LocalDateTime createdAt;

    public Member() {
    }

    public Member(Long memberId, String email, String name, Long tierId, LocalDateTime createdAt) {
        this.memberId = memberId;
        this.email = email;
        this.name = name;
        this.tierId = tierId;
        this.createdAt = createdAt;
    }

    // INSERT용 생성자 (id, createdAt 없이)
    public Member(String email, String name, Long tierId) {
        this.email = email;
        this.name = name;
        this.tierId = tierId;
    }

    // getter / setter
    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTierId() {
        return tierId;
    }

    public void setTierId(Long tierId) {
        this.tierId = tierId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Member{" +
                "memberId=" + memberId +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", tierId=" + tierId +
                ", createdAt=" + createdAt +
                '}';
    }
}

