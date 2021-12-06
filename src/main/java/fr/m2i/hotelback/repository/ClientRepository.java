package fr.m2i.hotelback.repository;

import fr.m2i.hotelback.entities.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<ClientEntity, Integer> {
}
