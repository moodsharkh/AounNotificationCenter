package com.leaflet.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Messages.
 */
@Entity
@Table(name = "messages")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Messages implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "message_content")
    private String messageContent;

    @ManyToOne
    private Languages messageToLanguages;

    @ManyToOne
    private Notification messageToNotification;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Messages title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public Messages messageContent(String messageContent) {
        this.messageContent = messageContent;
        return this;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public Languages getMessageToLanguages() {
        return messageToLanguages;
    }

    public Messages messageToLanguages(Languages languages) {
        this.messageToLanguages = languages;
        return this;
    }

    public void setMessageToLanguages(Languages languages) {
        this.messageToLanguages = languages;
    }

    public Notification getMessageToNotification() {
        return messageToNotification;
    }

    public Messages messageToNotification(Notification notification) {
        this.messageToNotification = notification;
        return this;
    }

    public void setMessageToNotification(Notification notification) {
        this.messageToNotification = notification;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Messages messages = (Messages) o;
        if (messages.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), messages.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Messages{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", messageContent='" + getMessageContent() + "'" +
            "}";
    }
}
