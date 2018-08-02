package com.leaflet.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Border.
 */
@Entity
@Table(name = "border")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Border implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jhi_desc")
    private String desc;

    @Column(name = "insertion_date")
    private LocalDate insertionDate;

    @Column(name = "inserted_by")
    private String insertedBy;

    @Column(name = "color_reset")
    private Boolean colorReset;

    @OneToMany(mappedBy = "manytoone")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Points> onetomanies = new HashSet<>();

    @OneToMany(mappedBy = "notimanytoone")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Notification> notionetomanies = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public Border desc(String desc) {
        this.desc = desc;
        return this;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public LocalDate getInsertionDate() {
        return insertionDate;
    }

    public Border insertionDate(LocalDate insertionDate) {
        this.insertionDate = insertionDate;
        return this;
    }

    public void setInsertionDate(LocalDate insertionDate) {
        this.insertionDate = insertionDate;
    }

    public String getInsertedBy() {
        return insertedBy;
    }

    public Border insertedBy(String insertedBy) {
        this.insertedBy = insertedBy;
        return this;
    }

    public void setInsertedBy(String insertedBy) {
        this.insertedBy = insertedBy;
    }

    public Boolean isColorReset() {
        return colorReset;
    }

    public Border colorReset(Boolean colorReset) {
        this.colorReset = colorReset;
        return this;
    }

    public void setColorReset(Boolean colorReset) {
        this.colorReset = colorReset;
    }

    public Set<Points> getOnetomanies() {
        return onetomanies;
    }

    public Border onetomanies(Set<Points> points) {
        this.onetomanies = points;
        return this;
    }

    public Border addOnetomany(Points points) {
        this.onetomanies.add(points);
        points.setManytoone(this);
        return this;
    }

    public Border removeOnetomany(Points points) {
        this.onetomanies.remove(points);
        points.setManytoone(null);
        return this;
    }

    public void setOnetomanies(Set<Points> points) {
        this.onetomanies = points;
    }

    public Set<Notification> getNotionetomanies() {
        return notionetomanies;
    }

    public Border notionetomanies(Set<Notification> notifications) {
        this.notionetomanies = notifications;
        return this;
    }

    public Border addNotionetomany(Notification notification) {
        this.notionetomanies.add(notification);
        notification.setNotimanytoone(this);
        return this;
    }

    public Border removeNotionetomany(Notification notification) {
        this.notionetomanies.remove(notification);
        notification.setNotimanytoone(null);
        return this;
    }

    public void setNotionetomanies(Set<Notification> notifications) {
        this.notionetomanies = notifications;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Border border = (Border) o;
        if (border.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), border.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Border{" +
            "id=" + getId() +
            ", desc='" + getDesc() + "'" +
            ", insertionDate='" + getInsertionDate() + "'" +
            ", insertedBy='" + getInsertedBy() + "'" +
            ", colorReset='" + isColorReset() + "'" +
            "}";
    }
}
