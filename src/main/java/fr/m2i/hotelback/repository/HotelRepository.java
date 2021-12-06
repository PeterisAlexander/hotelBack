package fr.m2i.hotelback.repository;

import fr.m2i.hotelback.entities.HotelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<HotelEntity, Integer> {
}
