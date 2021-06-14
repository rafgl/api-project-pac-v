package d2.api.events.services;

import d2.api.events.models.Event;
import d2.api.events.models.Promotion;
import d2.api.events.repositories.EventsRepository;
import d2.api.events.repositories.PromotionsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/events")
public class EventService {

    private static final Logger LOG = LoggerFactory.getLogger(EventService.class);

    @Autowired
    private EventsRepository eventsRepository;

    @Autowired
    private PromotionsRepository promotionsRepository;

    @GetMapping
    public ResponseEntity<List<Event>> index() {
        LOG.info("GET - INDEX");
        return new ResponseEntity<>(eventsRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Event>  show(@PathVariable Long id) {
        LOG.info("GET - SHOW - DATA: " + id);
        return eventsRepository.findById(id).map(event ->
            new ResponseEntity<>(event, HttpStatus.OK)
        ).orElseThrow(() -> new ResourceNotFoundException("Event not found"));
    }

    @PostMapping
    public ResponseEntity<Event> store(@Valid @RequestBody Event event) {
        LOG.info("POST - STORE - DATA: " + event.toString());
        return new ResponseEntity<>(eventsRepository.save(event), HttpStatus.OK);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Event> update(@PathVariable Long id, @Valid @RequestBody Event requestEvent) {
        return eventsRepository.findById(id).map(event -> {
            event.setName(requestEvent.getName());
            event.setStart_date(requestEvent.getStart_date());
            event.setEnd_date(requestEvent.getEnd_date());
            event.setDescription(requestEvent.getDescription());
            event.setLocation(requestEvent.getLocation());
            event.setPhotoUrl(requestEvent.getPhotoUrl());
            return new ResponseEntity<>(eventsRepository.save(event), HttpStatus.OK);
        }).orElseThrow(() -> new ResourceNotFoundException("Event not found"));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> destroy(@PathVariable Long id) {
        LOG.info("DELETE - DESTROY - DATA: " + id);
        return eventsRepository.findById(id).map(event -> {
           eventsRepository.delete(event);
           return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Event not found"));
    }

    @PostMapping(path = "/{eventId}/promote/{promotionId}")
    public ResponseEntity<?> promote(@PathVariable Long eventId, @PathVariable Long promotionId) {
        LOG.info("POST - PROMOTE - DATA: " + eventId + " /// " +promotionId);

        Event event = eventsRepository.findById(eventId).orElseThrow(() -> new ResourceNotFoundException("Event not found"));
        Promotion promotion = promotionsRepository.findById(promotionId).orElseThrow(() -> new ResourceNotFoundException("Promotion not found"));

        event.addPromotion(promotion);

        eventsRepository.save(event);

        return new ResponseEntity<>(event, HttpStatus.OK);
    }
}
