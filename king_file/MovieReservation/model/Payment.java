package org.example.model;

import java.time.LocalDateTime;

public class Payment {

    private Long paymentId;
    private Long bookingId;
    private Long methodId;
    private int amount;
    private String status;
    private LocalDateTime approvedAt;

    public Payment() {}

    public Payment(Long bookingId, Long methodId, int amount) {
        this.bookingId = bookingId;
        this.methodId = methodId;
        this.amount = amount;
        this.status = "SUCCESS";
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Long getMethodId() {
        return methodId;
    }

    public void setMethodId(Long methodId) {
        this.methodId = methodId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(LocalDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", bookingId=" + bookingId +
                ", methodId=" + methodId +
                ", amount=" + amount +
                ", status='" + status + '\'' +
                ", approvedAt=" + approvedAt +
                '}';
    }
}