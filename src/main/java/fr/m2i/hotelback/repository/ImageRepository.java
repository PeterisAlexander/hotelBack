package fr.m2i.hotelback.repository;

import fr.m2i.hotelback.entities.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<ImageEntity, Integer> {
}
