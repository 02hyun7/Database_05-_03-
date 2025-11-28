package org.example.model;

import java.time.LocalDateTime;

public class Showtime {

    private Long showId;
    private Long screenId;
    private Long versionId;
    private LocalDateTime startsAt;
    private LocalDateTime endsAt;
    private Long showStatusId;

    public Showtime() {
    }

    public Showtime(Long showId, Long screenId, Long versionId,
                    LocalDateTime startsAt, LocalDateTime endsAt,
                    Long showStatusId) {
        this.showId = showId;
        this.screenId = screenId;
        this.versionId = versionId;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.showStatusId = showStatusId;
    }

    public Long getShowId() {
        return showId;
    }

    public void setShowId(Long showId) {
        this.showId = showId;
    }

    public Long getScreenId() {
        return screenId;
    }

    public void setScreenId(Long screenId) {
        this.screenId = screenId;
    }

    public Long getVersionId() {
        return versionId;
    }

    public void setVersionId(Long versionId) {
        this.versionId = versionId;
    }

    public LocalDateTime getStartsAt() {
        return startsAt;
    }

    public void setStartsAt(LocalDateTime startsAt) {
        this.startsAt = startsAt;
    }

    public LocalDateTime getEndsAt() {
        return endsAt;
    }

    public void setEndsAt(LocalDateTime endsAt) {
        this.endsAt = endsAt;
    }

    public Long getShowStatusId() {
        return showStatusId;
    }

    public void setShowStatusId(Long showStatusId) {
        this.showStatusId = showStatusId;
    }

    @Override
    public String toString() {
        return "Showtime{" +
                "showId=" + showId +
                ", screenId=" + screenId +
                ", versionId=" + versionId +
                ", startsAt=" + startsAt +
                ", endsAt=" + endsAt +
                ", showStatusId=" + showStatusId +
                '}';
    }
}
