package com.example.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbTest {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/movie?serverTimezone=Asia/Seoul&useSSL=false&characterEncoding=UTF-8";
        String user = "root";
        String password = "sense??3?88160";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("✅ DB 연결 성공!");
        } catch (Exception e) {
            System.out.println("❌ DB 연결 실패");
            e.printStackTrace();
        }
    }
}
