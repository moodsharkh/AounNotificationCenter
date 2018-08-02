package com.leaflet.repository;

import com.leaflet.domain.Messages;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Messages entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MessagesRepository extends JpaRepository<Messages,Long> {
    
}
