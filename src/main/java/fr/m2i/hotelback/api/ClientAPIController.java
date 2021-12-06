package fr.m2i.hotelback.api;

import fr.m2i.hotelback.entities.ClientEntity;
import fr.m2i.hotelback.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.InvalidObjectException;
import java.net.URI;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/client")
public class ClientAPIController {
    ClientService cs;

    public ClientAPIController(ClientService cs) {this.cs = cs;}

    //http://localhost:8080/api/client
    @GetMapping(value = "", produces = "application/json")
    public Iterable<ClientEntity> getAll() {return cs.findAll();}

    //http://localhost:8080/api/client/2
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<ClientEntity> get(@PathVariable int id) {
        try{
            ClientEntity c = cs.findClient(id);
            return ResponseEntity.ok(c);
        }catch ( Exception e ){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value="" , consumes = "application/json")
    public ResponseEntity<ClientEntity> add( @RequestBody ClientEntity c ){
        System.out.println( c );
        try{
            cs.addClient( c );

            // création de l'url d'accès au nouvel objet => http://localhost:8080/api/client
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand( c.getId() ).toUri();

            return ResponseEntity.created( uri ).body(c);

        }catch ( InvalidObjectException e ){
            //return ResponseEntity.badRequest().build();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST , e.getMessage() );
        }
    }

    @PutMapping(value="/{id}" , consumes = "application/json")
    public void update( @PathVariable int id , @RequestBody ClientEntity c ){
        try{
            cs.editClient( id , c );

        }catch ( NoSuchElementException e ){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND , "Client introuvable" );

        }catch ( InvalidObjectException e ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST , e.getMessage() );
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> delete(@PathVariable int id) {
        // Check sur l'existance de la client, si ko => 404 not found
        try{
            ClientEntity c = cs.findClient(id);
        }catch( Exception e ){
            return ResponseEntity.notFound().build();
        }

        // si on a un problème à ce niveau => sql exception
        try{
            cs.delete(id);
            return ResponseEntity.ok(null);
        }catch( Exception e ) {
            return ResponseEntity.badRequest().build();
        }
    }
}
