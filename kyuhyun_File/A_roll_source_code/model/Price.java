package com.example.model;

public class Price {
    private int priceId;
    private int showId;
    private int seatTypeId;
    private int defaultPrice;
    private Integer changedPrice;
    private int ruleApplyId;

    public int getPriceId() {
        return priceId;
    }

    public void setPriceId(int priceId) {
        this.priceId = priceId;
    }

    public int getShowId() {
        return showId;
    }

    public void setShowId(int showId) {
        this.showId = showId;
    }

    public int getSeatTypeId() {
        return seatTypeId;
    }

    public void setSeatTypeId(int seatTypeId) {
        this.seatTypeId = seatTypeId;
    }

    public int getDefaultPrice() {
        return defaultPrice;
    }

    public void setDefaultPrice(int defaultPrice) {
        this.defaultPrice = defaultPrice;
    }

    public Integer getChangedPrice() {
        return changedPrice;
    }

    public void setChangedPrice(Integer changedPrice) {
        this.changedPrice = changedPrice;
    }

    public int getRuleApplyId() {
        return ruleApplyId;
    }

    public void setRuleApplyId(int ruleApplyId) {
        this.ruleApplyId = ruleApplyId;
    }
}
