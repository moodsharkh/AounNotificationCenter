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
 * A Languages.
 */
@Entity
@Table(name = "languages")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Languages implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "language_desc")
    private String languageDesc;

    @OneToMany(mappedBy = "messageToLanguages")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Messages> languagesToMessages = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLanguageDesc() {
        return languageDesc;
    }

    public Languages languageDesc(String languageDesc) {
        this.languageDesc = languageDesc;
        return this;
    }

    public void setLanguageDesc(String languageDesc) {
        this.languageDesc = languageDesc;
    }

    public Set<Messages> getLanguagesToMessages() {
        return languagesToMessages;
    }

    public Languages languagesToMessages(Set<Messages> messages) {
        this.languagesToMessages = messages;
        return this;
    }

    public Languages addLanguagesToMessages(Messages messages) {
        this.languagesToMessages.add(messages);
        messages.setMessageToLanguages(this);
        return this;
    }

    public Languages removeLanguagesToMessages(Messages messages) {
        this.languagesToMessages.remove(messages);
        messages.setMessageToLanguages(null);
        return this;
    }

    public void setLanguagesToMessages(Set<Messages> messages) {
        this.languagesToMessages = messages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Languages languages = (Languages) o;
        if (languages.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), languages.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Languages{" +
            "id=" + getId() +
            ", languageDesc='" + getLanguageDesc() + "'" +
            "}";
    }
}
