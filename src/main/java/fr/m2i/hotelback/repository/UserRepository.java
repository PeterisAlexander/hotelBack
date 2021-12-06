package fr.m2i.hotelback.repository;

import fr.m2i.hotelback.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    public UserEntity findByUsername(String username);
}
