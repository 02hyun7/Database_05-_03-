package org.example.model;

import java.time.LocalDateTime;

public class HoldSeat {

    private Long holdId;
    private Long showId;
    private Long seatId;
    private Long memberId;
    private LocalDateTime expiresAt;

    public HoldSeat() {}

    public HoldSeat(Long holdId, Long showId, Long seatId, Long memberId, LocalDateTime expiresAt) {
        this.holdId = holdId;
        this.showId = showId;
        this.seatId = seatId;
        this.memberId = memberId;
        this.expiresAt = expiresAt;
    }

    public Long getHoldId() {
        return holdId;
    }

    public void setHoldId(Long holdId) {
        this.holdId = holdId;
    }

    public Long getShowId() {
        return showId;
    }

    public void setShowId(Long showId) {
        this.showId = showId;
    }

    public Long getSeatId() {
        return seatId;
    }

    public void setSeatId(Long seatId) {
        this.seatId = seatId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    @Override
    public String toString() {
        return "HoldSeat{" +
                "holdId=" + holdId +
                ", showId=" + showId +
                ", seatId=" + seatId +
                ", memberId=" + memberId +
                ", expiresAt=" + expiresAt +
                '}';
    }
}