package org.example.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {


    private static final String URL =
            "jdbc:mysql://localhost:3306/movie_reservation?serverTimezone=Asia/Seoul&characterEncoding=UTF-8";
    private static final String USERNAME = "";      // 네 MySQL 계정
    private static final String PASSWORD = "";   // 네 MySQL 비번

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

}
