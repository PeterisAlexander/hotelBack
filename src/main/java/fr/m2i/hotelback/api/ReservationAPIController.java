package fr.m2i.hotelback.api;

import fr.m2i.hotelback.entities.ReservationEntity;
import fr.m2i.hotelback.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.InvalidObjectException;
import java.net.URI;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/resa")
public class ReservationAPIController {
    ReservationService rs;

    public ReservationAPIController(ReservationService rs) { this.rs = rs;}

    //http://localhost:8080/api/resa
    @GetMapping(value = "", produces = "application/json")
    public Iterable<ReservationEntity> getAll() { return rs.findAll();}

    //http://localhost:8080/api/resa/2
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<ReservationEntity> get(@PathVariable int id) {
        try{
            ReservationEntity r = rs.findClient(id);
            return ResponseEntity.ok(r);
        }catch ( Exception e ){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value="" , consumes = "application/json")
    public ResponseEntity<ReservationEntity> add( @RequestBody ReservationEntity r ){
        System.out.println( r );
        try{
            rs.addResa( r );

            // création de l'url d'accès au nouvel objet => http://localhost:8080/api/resa
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand( r.getId() ).toUri();

            return ResponseEntity.created( uri ).body(r);

        }catch ( InvalidObjectException e ){
            //return ResponseEntity.badRequest().build();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST , e.getMessage() );
        }
    }

    @PutMapping(value="/{id}" , consumes = "application/json")
    public void update( @PathVariable int id , @RequestBody ReservationEntity r ){
        try{
            rs.editResa( id , r );

        }catch ( NoSuchElementException e ){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND , "Reservation introuvable" );

        }catch ( InvalidObjectException e ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST , e.getMessage() );
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> delete(@PathVariable int id) {
        // Check sur l'existance de la réseravation, si ko => 404 not found
        try{
            ReservationEntity r = rs.findClient(id);
        }catch( Exception e ){
            return ResponseEntity.notFound().build();
        }

        // si on a un problème à ce niveau => sql exception
        try{
            rs.delete(id);
            return ResponseEntity.ok(null);
        }catch( Exception e ) {
            return ResponseEntity.badRequest().build();
        }
    }
}
