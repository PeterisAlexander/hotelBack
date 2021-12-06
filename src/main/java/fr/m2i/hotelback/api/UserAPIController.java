package fr.m2i.hotelback.api;

import fr.m2i.hotelback.entities.UserEntity;
import fr.m2i.hotelback.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/user")
public class UserAPIController {
    UserService us;

    public UserAPIController(UserService us) { this.us = us;}

    //http://localhost:8080/api/user
    @GetMapping(value = "", produces = "application/json")
    public Iterable<UserEntity> getAll() { return us.findAll();}

    //http://localhost:8080/api/user/2
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<UserEntity> get(@PathVariable int id) {
        try{
            UserEntity u = us.findUser(id);
            return ResponseEntity.ok(u);
        }catch ( Exception e ){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value="" , consumes = "application/json")
    public ResponseEntity<UserEntity> add( @RequestBody UserEntity u ){
        System.out.println( u );
        us.addUser( u );

        // création de l'url d'accès au nouvel objet => http://localhost:8080/api/user
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand( u.getId() ).toUri();

        return ResponseEntity.created( uri ).body(u);

    }

    @PutMapping(value="/{id}" , consumes = "application/json")
    public void update( @PathVariable int id , @RequestBody UserEntity u ){
        try{
            us.editUser( id , u );

        }catch ( NoSuchElementException e ){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND , "User introuvable" );

        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> delete(@PathVariable int id) {
        // Check sur l'existance de l'utilisateur, si ko => 404 not found
        try{
            UserEntity u = us.findUser(id);
        }catch( Exception e ){
            return ResponseEntity.notFound().build();
        }

        // si on a un problème à ce niveau => sql exception
        try{
            us.delete(id);
            return ResponseEntity.ok(null);
        }catch( Exception e ) {
            return ResponseEntity.badRequest().build();
        }
    }
}
