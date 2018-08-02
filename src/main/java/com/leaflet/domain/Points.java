package com.leaflet.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Points.
 */
@Entity
@Table(name = "points")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Points implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lon")
    private String lon;

    @Column(name = "lat")
    private String lat;

    @Column(name = "raduis")
    private String raduis;

    @ManyToOne
    private Border manytoone;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLon() {
        return lon;
    }

    public Points lon(String lon) {
        this.lon = lon;
        return this;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public Points lat(String lat) {
        this.lat = lat;
        return this;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getRaduis() {
        return raduis;
    }

    public Points raduis(String raduis) {
        this.raduis = raduis;
        return this;
    }

    public void setRaduis(String raduis) {
        this.raduis = raduis;
    }

    public Border getManytoone() {
        return manytoone;
    }

    public Points manytoone(Border border) {
        this.manytoone = border;
        return this;
    }

    public void setManytoone(Border border) {
        this.manytoone = border;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Points points = (Points) o;
        if (points.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), points.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Points{" +
            "id=" + getId() +
            ", lon='" + getLon() + "'" +
            ", lat='" + getLat() + "'" +
            ", raduis='" + getRaduis() + "'" +
            "}";
    }
}
