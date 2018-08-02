package com.leaflet.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.leaflet.domain.Importance;

import com.leaflet.repository.ImportanceRepository;
import com.leaflet.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Importance.
 */
@RestController
@RequestMapping("/api")
public class ImportanceResource {

    private final Logger log = LoggerFactory.getLogger(ImportanceResource.class);

    private static final String ENTITY_NAME = "importance";

    private final ImportanceRepository importanceRepository;

    public ImportanceResource(ImportanceRepository importanceRepository) {
        this.importanceRepository = importanceRepository;
    }

    /**
     * POST  /importances : Create a new importance.
     *
     * @param importance the importance to create
     * @return the ResponseEntity with status 201 (Created) and with body the new importance, or with status 400 (Bad Request) if the importance has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/importances")
    @Timed
    public ResponseEntity<Importance> createImportance(@RequestBody Importance importance) throws URISyntaxException {
        log.debug("REST request to save Importance : {}", importance);
        if (importance.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new importance cannot already have an ID")).body(null);
        }
        Importance result = importanceRepository.save(importance);
        return ResponseEntity.created(new URI("/api/importances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /importances : Updates an existing importance.
     *
     * @param importance the importance to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated importance,
     * or with status 400 (Bad Request) if the importance is not valid,
     * or with status 500 (Internal Server Error) if the importance couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/importances")
    @Timed
    public ResponseEntity<Importance> updateImportance(@RequestBody Importance importance) throws URISyntaxException {
        log.debug("REST request to update Importance : {}", importance);
        if (importance.getId() == null) {
            return createImportance(importance);
        }
        Importance result = importanceRepository.save(importance);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, importance.getId().toString()))
            .body(result);
    }

    /**
     * GET  /importances : get all the importances.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of importances in body
     */
    @GetMapping("/importances")
    @Timed
    public List<Importance> getAllImportances() {
        log.debug("REST request to get all Importances");
        return importanceRepository.findAll();
    }

    /**
     * GET  /importances/:id : get the "id" importance.
     *
     * @param id the id of the importance to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the importance, or with status 404 (Not Found)
     */
    @GetMapping("/importances/{id}")
    @Timed
    public ResponseEntity<Importance> getImportance(@PathVariable Long id) {
        log.debug("REST request to get Importance : {}", id);
        Importance importance = importanceRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(importance));
    }

    /**
     * DELETE  /importances/:id : delete the "id" importance.
     *
     * @param id the id of the importance to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/importances/{id}")
    @Timed
    public ResponseEntity<Void> deleteImportance(@PathVariable Long id) {
        log.debug("REST request to delete Importance : {}", id);
        importanceRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
