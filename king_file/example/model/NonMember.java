package org.example.model;

import java.time.LocalDateTime;

public class NonMember {

    private Long nonMemberId;
    private String name;
    private String phone;
    private LocalDateTime createdAt;

    public NonMember() {}

    public NonMember(Long nonMemberId, String name, String phone, LocalDateTime createdAt) {
        this.nonMemberId = nonMemberId;
        this.name = name;
        this.phone = phone;
        this.createdAt = createdAt;
    }

    public NonMember(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public Long getNonMemberId() {
        return nonMemberId;
    }

    public void setNonMemberId(Long nonMemberId) {
        this.nonMemberId = nonMemberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "NonMember{" +
                "nonMemberId=" + nonMemberId +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}