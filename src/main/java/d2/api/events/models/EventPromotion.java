package d2.api.events.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "events_promotions")
public class EventPromotion {

    @EmbeddedId
    private EventPromotionEmb id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("event")
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("promotion")
    private Promotion promotion;

    @JsonFormat(pattern = "dd/MM/y HH:mm")
    private Date applied_at = new Date();

    public EventPromotion() { }

    public EventPromotion(Event event, Promotion promotion) {
        this.event = event;
        this.promotion = promotion;
    }

    public EventPromotionEmb getId() {
        return id;
    }

    public void setId(EventPromotionEmb id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }

    public Date getApplied_at() {
        return applied_at;
    }

    public void setApplied_at(Date applied_at) {
        this.applied_at = applied_at;
    }
}
