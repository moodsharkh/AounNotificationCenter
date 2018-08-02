package com.leaflet.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.leaflet.domain.Border;

import com.leaflet.domain.Messages;
import com.leaflet.domain.Notification;
import com.leaflet.domain.Points;
import com.leaflet.repository.BorderRepository;
import com.leaflet.repository.MessagesRepository;
import com.leaflet.repository.NotificationRepository;
import com.leaflet.repository.PointsRepository;
import com.leaflet.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Border.
 */
@RestController
@RequestMapping("/api")
public class BorderResource {

    private final Logger log = LoggerFactory.getLogger(BorderResource.class);

    private static final String ENTITY_NAME = "border";

    private final BorderRepository borderRepository;
    @Autowired
    private PointsRepository pointsRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private MessagesRepository messagesRepository;
    private List<Messages> listofmessages;
    private List<Notification> listofnotification;
    private List <Points> listofpoints ;
    public BorderResource(BorderRepository borderRepository) {
        this.borderRepository = borderRepository;
    }

    /**
     * POST  /borders : Create a new border.
     *
     * @param border the border to create
     * @return the ResponseEntity with status 201 (Created) and with body the new border, or with status 400 (Bad Request) if the border has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/borders")
    @Timed
    public ResponseEntity<Border> createBorder(@RequestBody Border border) throws URISyntaxException {
        log.debug("REST request to save Border : {}", border);
        if (border.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new border cannot already have an ID")).body(null);
        }
        Border result = borderRepository.save(border);
        return ResponseEntity.created(new URI("/api/borders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /borders : Updates an existing border.
     *
     * @param border the border to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated border,
     * or with status 400 (Bad Request) if the border is not valid,
     * or with status 500 (Internal Server Error) if the border couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/borders")
    @Timed
    public ResponseEntity<Border> updateBorder(@RequestBody Border border) throws URISyntaxException {
        log.debug("REST request to update Border : {}", border);
        if (border.getId() == null) {
            return createBorder(border);
        }
        Border result = borderRepository.save(border);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, border.getId().toString()))
            .body(result);
    }

    /**
     * GET  /borders : get all the borders.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of borders in body
     */
    @GetMapping("/borders")
    @Timed
    public List<Border> getAllBorders() {
        log.debug("REST request to get all Borders");
        return borderRepository.findAll();
    }

    /**
     * GET  /borders/:id : get the "id" border.
     *
     * @param id the id of the border to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the border, or with status 404 (Not Found)
     */
    @GetMapping("/borders/{id}")
    @Timed
    public ResponseEntity<Border> getBorder(@PathVariable Long id) {
        log.debug("REST request to get Border : {}", id);
        Border border = borderRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(border));
    }

    /**
     * DELETE  /borders/:id : delete the "id" border.
     *
     * @param id the id of the border to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/borders/{id}")
    @Timed
    public ResponseEntity<Void> deleteBorder(@PathVariable Long id) {
        log.debug("REST request to delete Border : {}", id);
        if (pointsRepository != null) {
            listofpoints = pointsRepository.findAll();
            for (Iterator<Points> it = listofpoints.iterator(); it.hasNext(); ) {
                Points p = it.next();
                long temp = p.getManytoone().getId();
                if (temp == id) {
                    pointsRepository.delete(p.getId());
                }

            }
        }
        if (messagesRepository != null) {
            listofmessages = messagesRepository.findAll();
            for (Iterator<Messages> it = listofmessages.iterator(); it.hasNext(); ) {
                Messages n = it.next();
                long temp = n.getMessageToNotification().getId();
                if (temp == id) {
                    messagesRepository.delete(n.getId());
                }

            }
        }
        if (notificationRepository != null) {
            listofnotification = notificationRepository.findAll();
            for (Iterator<Notification> it = listofnotification.iterator(); it.hasNext(); ) {
                Notification n = it.next();
                long temp = n.getNotimanytoone().getId();

                if (temp == id) {
                    if (messagesRepository != null) {
                        listofmessages = messagesRepository.findAll();
                        for (Iterator<Messages> po = listofmessages.iterator(); po.hasNext(); ) {
                            Messages k = po.next();
                            long tempo = k.getMessageToNotification().getId();
                            if (tempo == n.getId()) {
                                messagesRepository.delete(k.getId());
                            }

                        }
                    }
                    notificationRepository.delete(n.getId());
                }

            }
        }

        borderRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
