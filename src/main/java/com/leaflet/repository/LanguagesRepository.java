package com.leaflet.repository;

import com.leaflet.domain.Languages;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Languages entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LanguagesRepository extends JpaRepository<Languages,Long> {
    
}
