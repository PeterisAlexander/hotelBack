package fr.m2i.hotelback.api;

import fr.m2i.hotelback.entities.UserEntity;
import fr.m2i.hotelback.repository.UserRepository;
import fr.m2i.hotelback.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Base64;

@RestController
public class LoginAPIController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @PostMapping( value = "/api/login" ,  consumes = "application/json" ,  produces = "application/json")
    public ResponseEntity<UserEntity> get(@RequestBody UserEntity u ) {

        UserEntity user = userRepository.findByUsername( u.getUsername() );

        if(user == null) {
            return ResponseEntity.notFound().build();
        } else {
            System.out.println( "encoded pass : " + u.getPassword() );
            System.out.println( "pass en bd : " + user.getPassword() );

            // user exists
            if( encoder.matches( u.getPassword() , user.getPassword() ) ){
                String encoding = Base64.getEncoder().encodeToString((u.getUsername()+":"+u.getPassword()).getBytes());
                user.setPassword(encoding);

                return ResponseEntity.ok(user);
            }
            return ResponseEntity.badRequest().build();
        }

    }
}
