package d2.api.events.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.*;

@Entity
@Table(name = "promotions")
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String label;
    private int duration;

    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private BigDecimal price;

    @JsonFormat(pattern = "dd/MM/y HH:mm")
    private Date available_from;

    @JsonFormat(pattern = "dd/MM/y HH:mm")
    private Date available_to;

    private String description;

    @OneToMany(mappedBy = "promotion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventPromotion> events = new ArrayList<>();

    public Promotion() { }

    public Promotion(String label, int duration, BigDecimal price, Date available_from, Date available_to, String description) {
        this.label = label;
        this.duration = duration;
        this.price = price;
        this.available_from = available_from;
        this.available_to = available_to;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Promotion{" +
                "label='" + label + '\'' +
                ", duration=" + duration +
                ", price=" + price +
                ", available_from=" + available_from +
                ", available_to=" + available_to +
                ", description='" + description + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Date getAvailable_from() {
        return available_from;
    }

    public void setAvailable_from(Date available_from) {
        this.available_from = available_from;
    }

    public Date getAvailable_to() {
        return available_to;
    }

    public void setAvailable_to(Date available_to) {
        this.available_to = available_to;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<EventPromotion> getEvents() {
        return events;
    }

    public void addEvent(Event event) {
        EventPromotion eventPromotion = new EventPromotion(event, this);
        events.add(eventPromotion);
        event.getPromotions().add(eventPromotion);
    }

    public void removeEvent(Event event) {
//        events.remove(event);
//        event.getPromotions().remove(this);

        for(Iterator<EventPromotion> iterator = events.iterator(); iterator.hasNext();) {
            EventPromotion eventPromotion = iterator.next();

            if(eventPromotion.getPromotion().equals(this) && eventPromotion.getEvent().equals(event)) {
                iterator.remove();
                eventPromotion.getEvent().getPromotions().remove(eventPromotion);
                eventPromotion.setPromotion(null);
                eventPromotion.setEvent(null);
            }
        }
    }
}
