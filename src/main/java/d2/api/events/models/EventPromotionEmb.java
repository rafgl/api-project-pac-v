package d2.api.events.models;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class EventPromotionEmb implements Serializable {

    @Column(name = "event")
    private Long event;

    @Column(name = "promotion")
    private Long promotion;

    public EventPromotionEmb() { }

    public EventPromotionEmb(Long event, Long promotion) {
        this.event = event;
        this.promotion = promotion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventPromotionEmb that = (EventPromotionEmb) o;
        return Objects.equals(event, that.event) &&
                Objects.equals(promotion, that.promotion);
    }

    @Override
    public int hashCode() {

        return Objects.hash(event, promotion);
    }

    public Long getEvent() {
        return event;
    }

    public void setEvent(Long event) {
        this.event = event;
    }

    public Long getPromotion() {
        return promotion;
    }

    public void setPromotion(Long promotion) {
        this.promotion = promotion;
    }
}
