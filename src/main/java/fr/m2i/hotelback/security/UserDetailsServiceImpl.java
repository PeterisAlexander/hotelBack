package fr.m2i.hotelback.security;

import fr.m2i.hotelback.entities.UserEntity;
import fr.m2i.hotelback.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Check si le user existe en bd
        UserEntity user = userRepository.findByUsername(username);

        if(user == null) {
            throw new UsernameNotFoundException("No user named " + username);
        } else {
            return new UserDetailsImpl(user);
        }
    }
}
