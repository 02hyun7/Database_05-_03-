package org.example.model;

public class Seat {

    private Long seatId;
    private Long layoutId;
    private String rowLabel;
    private int colNumber;
    private Long seatTypeId;

    public Seat() {
    }

    public Seat(Long seatId, Long layoutId, String rowLabel, int colNumber, Long seatTypeId) {
        this.seatId = seatId;
        this.layoutId = layoutId;
        this.rowLabel = rowLabel;
        this.colNumber = colNumber;
        this.seatTypeId = seatTypeId;
    }

    public Long getSeatId() {
        return seatId;
    }

    public void setSeatId(Long seatId) {
        this.seatId = seatId;
    }

    public Long getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(Long layoutId) {
        this.layoutId = layoutId;
    }

    public String getRowLabel() {
        return rowLabel;
    }

    public void setRowLabel(String rowLabel) {
        this.rowLabel = rowLabel;
    }

    public int getColNumber() {
        return colNumber;
    }

    public void setColNumber(int colNumber) {
        this.colNumber = colNumber;
    }

    public Long getSeatTypeId() {
        return seatTypeId;
    }

    public void setSeatTypeId(Long seatTypeId) {
        this.seatTypeId = seatTypeId;
    }

    @Override
    public String toString() {
        return "Seat{" +
                "seatId=" + seatId +
                ", layoutId=" + layoutId +
                ", rowLabel='" + rowLabel + '\'' +
                ", colNumber=" + colNumber +
                ", seatTypeId=" + seatTypeId +
                '}';
    }
}