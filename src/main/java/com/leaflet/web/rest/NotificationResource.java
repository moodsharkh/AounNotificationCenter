package com.leaflet.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.leaflet.domain.Messages;
import com.leaflet.domain.Notification;

import com.leaflet.domain.Points;
import com.leaflet.repository.MessagesRepository;
import com.leaflet.repository.NotificationRepository;
import com.leaflet.repository.PointsRepository;
import com.leaflet.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.time.temporal.TemporalField;
import java.util.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

/**
 * REST controller for managing Notification.
 */
@RestController
@RequestMapping("/api")
public class NotificationResource {

    private final Logger log = LoggerFactory.getLogger(NotificationResource.class);

    private static final String ENTITY_NAME = "notification";
    @Autowired
    private PointsRepository pointsRepository;

    @Autowired
    private MessagesRepository messagesRepository;

    private final NotificationRepository notificationRepository;
    private PointsResource Pointsrep;
    private List<Notification> listofnotification;
    private List<Points> listofpoints;
    private List <Messages> listofmessages;

    public NotificationResource(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    /**
     * POST  /notifications : Create a new notification.
     *
     * @param notification the notification to create
     * @return the ResponseEntity with status 201 (Created) and with body the new notification, or with status 400 (Bad Request) if the notification has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/notifications")
    @Timed
    public ResponseEntity<Notification> createNotification(@RequestBody Notification notification) throws Exception {
        log.debug("REST request to save Notification : {}", notification);
        if (notification.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new notification cannot already have an ID")).body(null);
        }

        Notification result = notificationRepository.save(notification);

//        }
        return ResponseEntity.created(new URI("/api/notifications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /notifications : Updates an existing notification.
     *
     * @param notification the notification to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated notification,
     * or with status 400 (Bad Request) if the notification is not valid,
     * or with status 500 (Internal Server Error) if the notification couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/notifications")
    @Timed
    public ResponseEntity<Notification> updateNotification(@RequestBody Notification notification) throws Exception {
        log.debug("REST request to update Notification : {}", notification);
        if (notification.getId() == null) {
            return createNotification(notification);
        }
        String tempString = notification.getTitle();
        if (tempString.substring(tempString.length()-2).equalsIgnoreCase("##") ){
//            notification.setTitle(notification.getTitle().substring(0,notification.getTitle().length()-2));
            Notification result = notificationRepository.save(notification);

            return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, notification.getId().toString()))
                .body(result);
        }

        String messageEn ="";
        String messageUr = "";
        String titleEn = "";
        String titleUr="";

        List<Points> listofwantedpoints = new ArrayList<Points>();
        List<Messages> listofwantedMessages = new ArrayList<Messages>();
        String jsonBody = "";
        listofpoints = pointsRepository.findAll();
        listofmessages = messagesRepository.findAll();
        ;
        Long notiAreaId = notification.getNotimanytoone().getId();
        String notipriority = String.valueOf(notification.getImanytoone().getId());
        for (Iterator<Points> it = listofpoints.iterator(); it.hasNext(); ) {
            Points p = it.next();
            long temp = p.getManytoone().getId();
            if (temp == notiAreaId) {
                listofwantedpoints.add(p);
            }

        }
        for (Iterator<Messages> it = listofmessages.iterator(); it.hasNext(); ) {
            Messages p = it.next();
            long temp = p.getMessageToNotification().getId();
            if (temp == notification.getId()) {
                if (p.getMessageToLanguages().getId() == 2){
                    messageEn = p.getMessageContent();
                    titleEn = p.getTitle();
                }
                if (p.getMessageToLanguages().getId() == 3){
                    messageUr = p.getMessageContent();
                    titleUr = p.getTitle();
                }

            }

        }

        long expiryMill = notification.getExpiryDate().toInstant().toEpochMilli();

        if (listofwantedpoints.size() == 1) {
            // For Circle, we have to check if it is 1
            Points tempPoint = listofwantedpoints.get(0);
            jsonBody = "{\n" +
                "    \"data\": {\n" +
                "        \"notificationData\": {\n" +
                "        \"notificationId\": \"" + notification.getId() + "\",\n" +
                "        \"notificationTitleAr\": \"" + notification.getTitle() + "\",\n" +
                "        \"notificationMessageAr\": \"" + notification.getMessage() + "\",\n" +
                "        \"notificationTitleEn\": \"" + titleEn + "\",\n" +
                "        \"notificationMessageEn\": \"" + messageEn + "\",\n" +
                "        \"notificationTitleUr\": \"" + titleUr + "\",\n" +
                "        \"notificationMessageUr\": \"" + messageUr + "\",\n" +
                "        \"notificationDateTime\": \"" + notification.getSendingDate() + "\",\n" +
                "        \"notificationPriority\": \"" + notipriority + "\",\n" +
                "        \"notificationExpiryDate\": \"" + expiryMill + "\",\n" +
                "        \"contentAvailable\": \"1\",\n" +
                "        \"notificationArea\": {\n" +
                "                \"areaId\": \"" + notification.getNotimanytoone().getId() + "\",\n" +
                "                \"areaName\": \"" + notification.getNotimanytoone().getDesc() + "\",\n" +
                "                \"radius\": \"" + (String.valueOf(Double.valueOf(tempPoint.getRaduis()) / 1000)) + "\",\n" +
                "                \"notificationCoordinates\": [{\n" +
                "                        \"latitude\": \"" + tempPoint.getLon() + "\",\n" +
                "                        \"longitude\": \"" + tempPoint.getLat() + "\"\n" +
                "                    }" +
                "                ]\n" +
                "            }\n" +
                "    }\n" +
                "    },\n" +
                "    \"to\": \"/topics/notification\"\n" +
                "    \"to\": \"/topics/notification\"\n" +
                "}";
        } else if (listofwantedpoints.size() > 2) {
            // For Polygon, We have to get and send all of the polygon points
            jsonBody = "{\n" +
                "    \"data\": {\n" +
                "        \"notificationData\": {\n" +
                "        \"notificationId\": \"" + notification.getId() + "\",\n" +
                "        \"notificationTitleAr\": \"" + notification.getTitle() + "\",\n" +
                "        \"notificationMessageAr\": \"" + notification.getMessage() + "\",\n" +
                "        \"notificationTitleEn\": \"" + titleEn + "\",\n" +
                "        \"notificationMessageEn\": \"" + messageEn + "\",\n" +
                "        \"notificationTitleUr\": \"" + titleUr + "\",\n" +
                "        \"notificationMessageUr\": \"" + messageUr + "\",\n" +
                "        \"notificationDateTime\": \"" + notification.getSendingDate() + "\",\n" +
                "        \"notificationPriority\": \"" + notipriority + "\",\n" +
                "        \"notificationExpiryDate\": \"" + expiryMill + "\",\n" +
                "        \"contentAvailable\": \"1\",\n" +
                "        \"notificationArea\": {\n" +
                "                \"areaId\": \"" + notification.getNotimanytoone().getId() + "\",\n" +
                "                \"areaName\": \"" + notification.getNotimanytoone().getDesc() + "\",\n" +
                "                \"radius\": \"\",\n" +
                "                \"notificationCoordinates\": [";
            for (Points point : listofwantedpoints) {
                jsonBody = jsonBody + "{\n" +
                    "                        \"latitude\": \"" + point.getLon() + "\",\n" +
                    "                        \"longitude\": \"" + point.getLat() + "\"\n" +
                    "                    },";
            }
            jsonBody = jsonBody.substring(0, jsonBody.length() - 1);
            jsonBody = jsonBody + " ]\n" +
                "            }\n" +
                "    }\n" +
                "    },\n" +
                "    \"to\": \"/topics/notification\"\n" +
                "}";

        }
//
        System.out.println("JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ"+jsonBody);
        sendPost(jsonBody);
        notification.setTitle(notification.getTitle()+"##");
        Notification result = notificationRepository.save(notification);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, notification.getId().toString()))
            .body(result);
    }

    private void sendPost(String dataa) throws Exception {

        String url = "https://fcm.googleapis.com/fcm/send";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");

      //  con.setRequestProperty("Authorization", "key=AIzaSyD7Kb_ES6EgPU_JdK4iWRlUKnut5R5LxnM");
        con.setRequestProperty("Authorization", "key=AIzaSyDF8WLe_Kefd_EAFLJ24lBa6v1O1M7fDeg");
        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.write(dataa.getBytes());
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
//        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
            new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());

    }

    /**
     * GET  /notifications : get all the notifications.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of notifications in body
     */
    @GetMapping("/notifications")
    @Timed
    public List<Notification> getAllNotifications() {
        log.debug("REST request to get all Notifications");
        return notificationRepository.findAll();
    }

    /**
     * GET  /notifications/:id : get the "id" notification.
     *
     * @param id the id of the notification to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the notification, or with status 404 (Not Found)
     */
    @GetMapping("/notifications/{id}")
    @Timed
    public ResponseEntity<Notification> getNotification(@PathVariable Long id) {
        log.debug("REST request to get Notification : {}", id);
        Notification notification = notificationRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(notification));
    }

    /**
     * DELETE  /notifications/:id : delete the "id" notification.
     *
     * @param id the id of the notification to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/notifications/{id}")
    @Timed
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        log.debug("REST request to delete Notification : {}", id);
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
        notificationRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
