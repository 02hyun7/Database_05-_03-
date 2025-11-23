package org.example.dao;

import org.example.model.NonMember;
import org.example.util.DBUtil;

import java.sql.*;
import java.time.LocalDateTime;

public class NonMemberDao {

    public NonMember create(NonMember nonMember) throws SQLException {

        String sql = "INSERT INTO non_member (name, phone, created_at) VALUES (?, ?, NOW())";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            pstmt.setString(1, nonMember.getName());
            pstmt.setString(2, nonMember.getPhone());

            pstmt.executeUpdate();

            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                if (keys.next()) {
                    nonMember.setNonMemberId(keys.getLong(1));
                }
            }
        }

        return nonMember;
    }


    public NonMember findById(Long id) throws SQLException {
        String sql = "SELECT non_member_id, name, phone, created_at FROM non_member WHERE non_member_id = ?";

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


    private NonMember map(ResultSet rs) throws SQLException {
        Long id = rs.getLong("non_member_id");
        String name = rs.getString("name");
        String phone = rs.getString("phone");
        LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();

        return new NonMember(id, name, phone, createdAt);
    }
}