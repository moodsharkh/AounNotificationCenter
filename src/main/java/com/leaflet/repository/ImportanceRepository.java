package com.leaflet.repository;

import com.leaflet.domain.Importance;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Importance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ImportanceRepository extends JpaRepository<Importance,Long> {
    
}
