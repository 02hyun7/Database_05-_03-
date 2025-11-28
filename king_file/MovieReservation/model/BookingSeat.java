package org.example.model;

public class BookingSeat {

    private Long bookingId;
    private Long seatId;
    private Long showId;
    private int price;

    public BookingSeat(Long bookingId, Long seatId, Long showId, int price) {
        this.bookingId = bookingId;
        this.seatId = seatId;
        this.showId = showId;
        this.price = price;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Long getSeatId() {
        return seatId;
    }

    public void setSeatId(Long seatId) {
        this.seatId = seatId;
    }

    public Long getShowId() {
        return showId;
    }

    public void setShowId(Long showId) {
        this.showId = showId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "BookingSeat{" +
                "bookingId=" + bookingId +
                ", seatId=" + seatId +
                ", showId=" + showId +
                ", price=" + price +
                '}';
    }
}