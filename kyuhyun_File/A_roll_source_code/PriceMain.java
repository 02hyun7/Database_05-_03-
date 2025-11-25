package com.example;

import com.example.dao.PriceDao;

public class PriceMain {

    public static void main(String[] args) {
        PriceDao dao = new PriceDao();

        int showId = 1;
        int seatTypeId = 2;

        Integer price = dao.getFinalPrice(showId, seatTypeId);

        System.out.println("최종 가격: " + price);
    }
}
