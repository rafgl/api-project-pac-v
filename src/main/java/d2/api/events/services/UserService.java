package d2.api.events.services;

import d2.api.events.models.User;
import d2.api.events.repositories.UsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping(path = "/users")
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UsersRepository usersRepository;

    @GetMapping(path = "/{id}")
    public ResponseEntity<?>  show(@PathVariable Long id) {
        LOG.info("GET - SHOW - DATA: " + id);
        return usersRepository.findById(id).map(user ->
                new ResponseEntity<>(user, HttpStatus.OK)
        ).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @PostMapping
    public ResponseEntity<?> store(@Valid @RequestBody User requestUser) {
        if(usersRepository.findByEmail(requestUser.getEmail()).isPresent()) {
            return new ResponseEntity<>("Email already registered.", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(usersRepository.save(requestUser), HttpStatus.OK);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody User requestUser) {

        return usersRepository.findById(id).map(user -> {

            Optional<User> userEmail = usersRepository.findByEmail(requestUser.getEmail());

            if(userEmail.isPresent() && !userEmail.get().getId().equals(requestUser.getId())) {
                return new ResponseEntity<>("Email already in use.", HttpStatus.FORBIDDEN);
            }

            user.setEmail(requestUser.getEmail());
            user.setDisplayName(requestUser.getDisplayName());

            return new ResponseEntity<>(usersRepository.save(user), HttpStatus.OK);

        }).orElseThrow(() -> new ResourceNotFoundException("User not found."));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> destroy(@PathVariable Long id) {
        return usersRepository.findById(id).map(user -> {
            usersRepository.delete(user);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("User not found."));
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
        return usersRepository.findByEmail(email).map(user -> {
            if(user.matchesPassword(password)) {
                return new ResponseEntity<>(user, HttpStatus.OK);
            }
            throw new ResourceNotFoundException("Password does not match.");
        }).orElseThrow(() -> new ResourceNotFoundException("Email not found."));
    }

    @GetMapping(path = "/{id}/events")
    public ResponseEntity<?> userEvents(@PathVariable Long id) {
        return usersRepository.findById(id).map(user ->
                new ResponseEntity<>(user.getEvents(), HttpStatus.OK)
        ).orElseThrow(() -> new ResourceNotFoundException("User not found."));
    }
}
