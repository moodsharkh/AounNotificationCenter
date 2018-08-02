package com.leaflet.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.leaflet.domain.Points;

import com.leaflet.repository.PointsRepository;
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
 * REST controller for managing Points.
 */
@RestController
@RequestMapping("/api")
public class PointsResource {

    private final Logger log = LoggerFactory.getLogger(PointsResource.class);

    private static final String ENTITY_NAME = "points";

    private final PointsRepository pointsRepository;

    public PointsResource(PointsRepository pointsRepository) {
        this.pointsRepository = pointsRepository;
    }

    /**
     * POST  /points : Create a new points.
     *
     * @param points the points to create
     * @return the ResponseEntity with status 201 (Created) and with body the new points, or with status 400 (Bad Request) if the points has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/points")
    @Timed
    public ResponseEntity<Points> createPoints(@RequestBody Points points) throws URISyntaxException {
        log.debug("REST request to save Points : {}", points);
        if (points.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new points cannot already have an ID")).body(null);
        }
        Points result = pointsRepository.save(points);
        return ResponseEntity.created(new URI("/api/points/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
    /**
     * PUT  /points : Updates an existing points.
     *
     * @param points the points to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated points,
     * or with status 400 (Bad Request) if the points is not valid,
     * or with status 500 (Internal Server Error) if the points couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/points")
    @Timed
    public ResponseEntity<Points> updatePoints(@RequestBody Points points) throws URISyntaxException {
        log.debug("REST request to update Points : {}", points);
        if (points.getId() == null) {
            return createPoints(points);
        }
        Points result = pointsRepository.save(points);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, points.getId().toString()))
            .body(result);
    }

    /**
     * GET  /points : get all the points.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of points in body
     */
    @GetMapping("/points")
    @Timed
    public List<Points> getAllPoints() {
        log.debug("REST request to get all Points");
        return pointsRepository.findAll();
    }

    /**
     * GET  /points/:id : get the "id" points.
     *
     * @param id the id of the points to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the points, or with status 404 (Not Found)
     */
    @GetMapping("/points/{id}")
    @Timed
    public ResponseEntity<Points> getPoints(@PathVariable Long id) {
        log.debug("REST request to get Points : {}", id);
        Points points = pointsRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(points));
    }

    /**
     * DELETE  /points/:id : delete the "id" points.
     *
     * @param id the id of the points to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/points/{id}")
    @Timed
    public ResponseEntity<Void> deletePoints(@PathVariable Long id) {
        log.debug("REST request to delete Points : {}", id);
        pointsRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
