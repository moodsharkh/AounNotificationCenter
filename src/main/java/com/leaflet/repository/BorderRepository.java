package com.leaflet.repository;

import com.leaflet.domain.Border;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Border entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BorderRepository extends JpaRepository<Border,Long> {
    
}
