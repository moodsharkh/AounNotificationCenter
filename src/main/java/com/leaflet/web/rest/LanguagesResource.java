package com.leaflet.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.leaflet.domain.Languages;

import com.leaflet.repository.LanguagesRepository;
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
 * REST controller for managing Languages.
 */
@RestController
@RequestMapping("/api")
public class LanguagesResource {

    private final Logger log = LoggerFactory.getLogger(LanguagesResource.class);

    private static final String ENTITY_NAME = "languages";

    private final LanguagesRepository languagesRepository;

    public LanguagesResource(LanguagesRepository languagesRepository) {
        this.languagesRepository = languagesRepository;
    }

    /**
     * POST  /languages : Create a new languages.
     *
     * @param languages the languages to create
     * @return the ResponseEntity with status 201 (Created) and with body the new languages, or with status 400 (Bad Request) if the languages has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/languages")
    @Timed
    public ResponseEntity<Languages> createLanguages(@RequestBody Languages languages) throws URISyntaxException {
        log.debug("REST request to save Languages : {}", languages);
        if (languages.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new languages cannot already have an ID")).body(null);
        }
        Languages result = languagesRepository.save(languages);
        return ResponseEntity.created(new URI("/api/languages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /languages : Updates an existing languages.
     *
     * @param languages the languages to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated languages,
     * or with status 400 (Bad Request) if the languages is not valid,
     * or with status 500 (Internal Server Error) if the languages couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/languages")
    @Timed
    public ResponseEntity<Languages> updateLanguages(@RequestBody Languages languages) throws URISyntaxException {
        log.debug("REST request to update Languages : {}", languages);
        if (languages.getId() == null) {
            return createLanguages(languages);
        }
        Languages result = languagesRepository.save(languages);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, languages.getId().toString()))
            .body(result);
    }

    /**
     * GET  /languages : get all the languages.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of languages in body
     */
    @GetMapping("/languages")
    @Timed
    public List<Languages> getAllLanguages() {
        log.debug("REST request to get all Languages");
        return languagesRepository.findAll();
    }

    /**
     * GET  /languages/:id : get the "id" languages.
     *
     * @param id the id of the languages to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the languages, or with status 404 (Not Found)
     */
    @GetMapping("/languages/{id}")
    @Timed
    public ResponseEntity<Languages> getLanguages(@PathVariable Long id) {
        log.debug("REST request to get Languages : {}", id);
        Languages languages = languagesRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(languages));
    }

    /**
     * DELETE  /languages/:id : delete the "id" languages.
     *
     * @param id the id of the languages to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/languages/{id}")
    @Timed
    public ResponseEntity<Void> deleteLanguages(@PathVariable Long id) {
        log.debug("REST request to delete Languages : {}", id);
        languagesRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
