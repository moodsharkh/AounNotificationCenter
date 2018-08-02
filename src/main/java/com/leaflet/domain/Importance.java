package com.leaflet.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Importance.
 */
@Entity
@Table(name = "importance")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Importance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jhi_desc")
    private String desc;

    @OneToMany(mappedBy = "imanytoone")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Notification> ionetomanies = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public Importance desc(String desc) {
        this.desc = desc;
        return this;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Set<Notification> getIonetomanies() {
        return ionetomanies;
    }

    public Importance ionetomanies(Set<Notification> notifications) {
        this.ionetomanies = notifications;
        return this;
    }

    public Importance addIonetomany(Notification notification) {
        this.ionetomanies.add(notification);
        notification.setImanytoone(this);
        return this;
    }

    public Importance removeIonetomany(Notification notification) {
        this.ionetomanies.remove(notification);
        notification.setImanytoone(null);
        return this;
    }

    public void setIonetomanies(Set<Notification> notifications) {
        this.ionetomanies = notifications;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Importance importance = (Importance) o;
        if (importance.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), importance.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Importance{" +
            "id=" + getId() +
            ", desc='" + getDesc() + "'" +
            "}";
    }
}
