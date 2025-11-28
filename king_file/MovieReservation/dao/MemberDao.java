package org.example.dao;

import org.example.model.Member;
import org.example.util.DBUtil;

import java.sql.*;
import java.time.LocalDateTime;

public class MemberDao {

    // -----------------------------
    // 회원 INSERT
    // -----------------------------
    public Member create(Member member) throws SQLException {

        String sql = "INSERT INTO member (email, name, tier_id, created_at) " +
                "VALUES (?, ?, ?, NOW())";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            pstmt.setString(1, member.getEmail());
            pstmt.setString(2, member.getName());
            // tier_id는 지금은 안 쓰니까 null 로 넣어도 됨
            if (member.getTierId() == null) {
                pstmt.setNull(3, Types.BIGINT);
            } else {
                pstmt.setLong(3, member.getTierId());
            }

            pstmt.executeUpdate();

            // 자동 생성된 member_id 가져오기
            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                if (keys.next()) {
                    member.setMemberId(keys.getLong(1));
                }
            }
        }

        return member;
    }

    // -----------------------------
    // 회원 ID로 조회
    // -----------------------------
    public Member findById(Long id) throws SQLException {
        String sql = "SELECT member_id, email, name, tier_id, created_at " +
                "FROM member WHERE member_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
        }
        return null;
    }

    // -----------------------------
    // 이메일로 회원 조회 (로그인용)
    // -----------------------------
    public Member findByEmail(String email) throws SQLException {
        String sql = "SELECT member_id, email, name, tier_id, created_at " +
                "FROM member WHERE email = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
        }
        return null;
    }

    // -----------------------------
    // ResultSet → Member 객체로 변환
    // -----------------------------
    private Member map(ResultSet rs) throws SQLException {
        Long id = rs.getLong("member_id");
        String email = rs.getString("email");
        String name = rs.getString("name");
        Long tierId = rs.getObject("tier_id") == null ? null : rs.getLong("tier_id");
        Timestamp ts = rs.getTimestamp("created_at");
        LocalDateTime createdAt = ts != null ? ts.toLocalDateTime() : null;

        return new Member(id, email, name, tierId, createdAt);
    }
}