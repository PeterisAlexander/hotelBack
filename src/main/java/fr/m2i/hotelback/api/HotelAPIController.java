package fr.m2i.hotelback.api;

import fr.m2i.hotelback.entities.HotelEntity;
import fr.m2i.hotelback.entities.HotelEntity;
import fr.m2i.hotelback.service.HotelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.InvalidObjectException;
import java.net.URI;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/hotel")
public class HotelAPIController {
    HotelService hs;

    public HotelAPIController(HotelService hs) { this.hs = hs;}

    //http://localhost:8080/api/hotel
    @GetMapping(value = "", produces = "application/json")
    public Iterable<HotelEntity> getAll() { return hs.findAll();}

    //http://localhost:8080/api/hotel/2
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<HotelEntity> get(@PathVariable int id) {
        try{
            HotelEntity h = hs.findHotel(id);
            return ResponseEntity.ok(h);
        }catch ( Exception e ){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value="" , consumes = "application/json")
    public ResponseEntity<HotelEntity> add( @RequestBody HotelEntity h ){
        System.out.println( h );
        try{
            hs.addHotel( h );

            // création de l'url d'accès au nouvel objet => http://localhost:8080/api/hotel
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand( h.getId() ).toUri();

            return ResponseEntity.created( uri ).body(h);

        }catch ( InvalidObjectException e ){
            //return ResponseEntity.badRequest().build();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST , e.getMessage() );
        }
    }

    @PutMapping(value="/{id}" , consumes = "application/json")
    public void update( @PathVariable int id , @RequestBody HotelEntity h ){
        try{
            hs.editHotel( id , h );

        }catch ( NoSuchElementException e ){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND , "Hotel introuvable" );

        }catch ( InvalidObjectException e ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST , e.getMessage() );
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> delete(@PathVariable int id) {
        // Check sur l'existance de l'hotel, si ko => 404 not found
        try{
            HotelEntity h = hs.findHotel(id);
        }catch( Exception e ){
            return ResponseEntity.notFound().build();
        }

        // si on a un problème à ce niveau => sql exception
        try{
            hs.delete(id);
            return ResponseEntity.ok(null);
        }catch( Exception e ) {
            return ResponseEntity.badRequest().build();
        }
    }
}
