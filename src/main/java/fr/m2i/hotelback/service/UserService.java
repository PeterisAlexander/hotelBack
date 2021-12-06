package fr.m2i.hotelback.service;

import fr.m2i.hotelback.entities.UserEntity;
import fr.m2i.hotelback.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UserService {
    private UserRepository ur;

    public UserService(UserRepository ur) { this.ur = ur;}

    @Autowired
    private PasswordEncoder encode;

    public Iterable<UserEntity> findAll() { return ur.findAll();}

    public void addUser(UserEntity u) {
      u.setPassword(encode.encode(u.getPassword()));

      ur.save(u);
    }

    public UserEntity findUser(int id) {
        return ur.findById(id).get();
    }

    public void editUser( int id , UserEntity u) throws NoSuchElementException {

        try{
            UserEntity uExistant = ur.findById(id).get();

            uExistant.setUsername( u.getUsername() );
            uExistant.setPassword( u.getPassword() );
            uExistant.setRole( u.getRole() );

            if( u.getPassword().length() > 0 ){
                uExistant.setPassword( encode.encode( u.getPassword() ) );
            }

            ur.save( uExistant );

        }catch ( NoSuchElementException e ){
            throw e;
        }
    }

    public void delete(int id) {
        ur.deleteById(id);
    }
}
