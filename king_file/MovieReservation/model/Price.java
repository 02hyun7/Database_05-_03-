package org.example.model;

import java.math.BigDecimal;

public class Price {

    private Long priceId;
    private Long showId;
    private Long seatTypeId;
    private BigDecimal defaultPrice;
    private BigDecimal changedPrice;
    private Long ruleApplyId;

    public Price() {
    }

    public Price(Long priceId, Long showId, Long seatTypeId,
                 BigDecimal defaultPrice, BigDecimal changedPrice,
                 Long ruleApplyId) {
        this.priceId = priceId;
        this.showId = showId;
        this.seatTypeId = seatTypeId;
        this.defaultPrice = defaultPrice;
        this.changedPrice = changedPrice;
        this.ruleApplyId = ruleApplyId;
    }

    public Long getPriceId() {
        return priceId;
    }

    public void setPriceId(Long priceId) {
        this.priceId = priceId;
    }

    public Long getShowId() {
        return showId;
    }

    public void setShowId(Long showId) {
        this.showId = showId;
    }

    public Long getSeatTypeId() {
        return seatTypeId;
    }

    public void setSeatTypeId(Long seatTypeId) {
        this.seatTypeId = seatTypeId;
    }

    public BigDecimal getDefaultPrice() {
        return defaultPrice;
    }

    public void setDefaultPrice(BigDecimal defaultPrice) {
        this.defaultPrice = defaultPrice;
    }

    public BigDecimal getChangedPrice() {
        return changedPrice;
    }

    public void setChangedPrice(BigDecimal changedPrice) {
        this.changedPrice = changedPrice;
    }

    public Long getRuleApplyId() {
        return ruleApplyId;
    }

    public void setRuleApplyId(Long ruleApplyId) {
        this.ruleApplyId = ruleApplyId;
    }

    @Override
    public String toString() {
        return "Price{" +
                "priceId=" + priceId +
                ", showId=" + showId +
                ", seatTypeId=" + seatTypeId +
                ", defaultPrice=" + defaultPrice +
                ", changedPrice=" + changedPrice +
                ", ruleApplyId=" + ruleApplyId +
                '}';
    }
}