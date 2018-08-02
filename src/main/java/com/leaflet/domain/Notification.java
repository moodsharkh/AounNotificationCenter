package com.leaflet.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Notification.
 */
@Entity
@Table(name = "notification")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "message")
    private String message;

    @Column(name = "received_by")
    private Integer receivedBy;

    @Column(name = "sending_date")
    private ZonedDateTime sendingDate;

    @Column(name = "expiry_date")
    private ZonedDateTime  expiryDate;

    @ManyToOne
    private Border notimanytoone;

    @ManyToOne
    private Importance imanytoone;

    @OneToMany(mappedBy = "messageToNotification")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Messages> notificationToMessages = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Notification title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public Notification message(String message) {
        this.message = message;
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getReceivedBy() {
        return receivedBy;
    }

    public Notification receivedBy(Integer receivedBy) {
        this.receivedBy = receivedBy;
        return this;
    }

    public void setReceivedBy(Integer receivedBy) {
        this.receivedBy = receivedBy;
    }

    public ZonedDateTime getSendingDate() {
        return sendingDate;
    }

    public Notification sendingDate(ZonedDateTime sendingDate) {
        this.sendingDate = sendingDate;
        return this;
    }

    public void setSendingDate(ZonedDateTime sendingDate) {
        this.sendingDate = sendingDate;
    }

    public ZonedDateTime  getExpiryDate() {
        return expiryDate;
    }

    public Notification expiryDate(ZonedDateTime  expiryDate) {
        this.expiryDate = expiryDate;
        return this;
    }

    public void setExpiryDate(ZonedDateTime  expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Border getNotimanytoone() {
        return notimanytoone;
    }

    public Notification notimanytoone(Border border) {
        this.notimanytoone = border;
        return this;
    }

    public void setNotimanytoone(Border border) {
        this.notimanytoone = border;
    }

    public Importance getImanytoone() {
        return imanytoone;
    }

    public Notification imanytoone(Importance importance) {
        this.imanytoone = importance;
        return this;
    }

    public void setImanytoone(Importance importance) {
        this.imanytoone = importance;
    }

    public Set<Messages> getNotificationToMessages() {
        return notificationToMessages;
    }

    public Notification notificationToMessages(Set<Messages> messages) {
        this.notificationToMessages = messages;
        return this;
    }

    public Notification addNotificationToMessages(Messages messages) {
        this.notificationToMessages.add(messages);
        messages.setMessageToNotification(this);
        return this;
    }

    public Notification removeNotificationToMessages(Messages messages) {
        this.notificationToMessages.remove(messages);
        messages.setMessageToNotification(null);
        return this;
    }

    public void setNotificationToMessages(Set<Messages> messages) {
        this.notificationToMessages = messages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Notification notification = (Notification) o;
        if (notification.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), notification.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Notification{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", message='" + getMessage() + "'" +
            ", receivedBy='" + getReceivedBy() + "'" +
            ", sendingDate='" + getSendingDate() + "'" +
            ", expiryDate='" + getExpiryDate() + "'" +
            "}";
    }
}
