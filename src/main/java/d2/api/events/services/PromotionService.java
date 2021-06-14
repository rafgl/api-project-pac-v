package d2.api.events.services;

import d2.api.events.models.Promotion;
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
@RequestMapping(path = "promotions")
public class PromotionService {

    private static final Logger LOG = LoggerFactory.getLogger(PromotionService.class);

    @Autowired
    private PromotionsRepository promotionsRepository;

    @GetMapping
    public ResponseEntity<List<Promotion>> index() {
        LOG.info("GET - INDEX");
        return new ResponseEntity<>(promotionsRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Promotion> show(@PathVariable Long id) {
        LOG.info("GET - SHOW - DATA: " + id);
        return promotionsRepository.findById(id).map(promotion ->
                new ResponseEntity<>(promotion, HttpStatus.OK)
        ).orElseThrow(() -> new ResourceNotFoundException("Promotion not found"));
    }

    @PostMapping
    public ResponseEntity<Promotion> store(@Valid @RequestBody Promotion promotion) {
        LOG.info("POST - STORE - DATA: " + promotion.toString());
        return new ResponseEntity<>(promotionsRepository.save(promotion), HttpStatus.OK);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Promotion> update(@PathVariable Long id, @Valid @RequestBody Promotion requestPromotion) {
        return promotionsRepository.findById(id).map(promotion -> {
            promotion.setLabel(requestPromotion.getLabel());
            promotion.setDuration(requestPromotion.getDuration());
            promotion.setPrice(requestPromotion.getPrice());
            promotion.setAvailable_from(requestPromotion.getAvailable_from());
            promotion.setAvailable_to(requestPromotion.getAvailable_to());
            promotion.setDescription(requestPromotion.getDescription());
            return new ResponseEntity<>(promotionsRepository.save(promotion), HttpStatus.OK);
        }).orElseThrow(() -> new ResourceNotFoundException("Promotion not found"));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> destroy(@PathVariable Long id) {
        LOG.info("DELETE - DESTROY - DATA: " + id);
        return promotionsRepository.findById(id).map(promotion -> {
            promotionsRepository.delete(promotion);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Promotion not found"));
    }

}
