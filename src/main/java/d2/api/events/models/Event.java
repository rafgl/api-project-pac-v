package d2.api.events.models;

import com.fasterxml.jackson.annotation.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    private String name;

//    need to set up offset
    @JsonFormat(pattern = "dd/MM/y HH:mm")
    private Date start_date;

//    need to set up offset
    @JsonFormat(pattern = "dd/MM/y HH:mm")
    private Date end_date;

    private String location;
    private String photoUrl;
    private String description;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "created_by")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Cascade(org.hibernate.annotations.CascadeType.DETACH)
    private User created_by;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventPromotion> promotions = new ArrayList<>();

    public Event() { }

    public Event(String name, Date start_date, Date end_date, String location, String photoUrl, String description, User created_by) {
        super();
        this.name = name;
        this.start_date = start_date;
        this.end_date = end_date;
        this.location = location;
        this.photoUrl = photoUrl;
        this.description = description;
        this.created_by = created_by;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", start_date=" + start_date +
                ", end_date=" + end_date +
                ", location='" + location + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", description='" + description + '\'' +
                ", created_by=" + created_by +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getCreated_by() {
        return created_by;
    }

    public void setCreated_by(User created_by) {
        this.created_by = created_by;
    }

    public List<EventPromotion> getPromotions() {
        return promotions;
    }

    public void addPromotion(Promotion promotion) {
        EventPromotion eventPromotion = new EventPromotion(this, promotion);
        promotions.add(eventPromotion);
        promotion.getEvents().add(eventPromotion);
    }

    public void removePromotion(Promotion promotion) {
//        promotions.remove(promotion);
//        promotion.getEvents().remove(this);

        for(Iterator<EventPromotion> iterator = promotions.iterator(); iterator.hasNext();) {
            EventPromotion eventPromotion = iterator.next();

            if(eventPromotion.getEvent().equals(this) && eventPromotion.getPromotion().equals(promotion)) {
                iterator.remove();
                eventPromotion.getPromotion().getEvents().remove(eventPromotion);
                eventPromotion.setEvent(null);
                eventPromotion.setPromotion(null);
            }
        }

    }
}
