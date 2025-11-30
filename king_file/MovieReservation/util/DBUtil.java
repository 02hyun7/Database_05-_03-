package org.example.util;

import java.sql.Connection;
import java.sql.DriverManager; // 연결 방식
import java.sql.SQLException;

public class DBUtil {


    private static final String URL = System.getenv("DB_URL");
    private static final String USERNAME = System.getenv("DB_USERNAME");
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD); // SQLException은 서비스 계층에서 처리
    }
}