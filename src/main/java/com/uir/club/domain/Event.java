package com.uir.club.domain;

import com.uir.club.domain.enumeration.Statut;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Type;

/**
 * A Event.
 */
@Entity
@Table(name = "event")
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "event_date", nullable = false)
    private Instant eventDate;

    @NotNull
    @Column(name = "event_end", nullable = false)
    private Instant eventEnd;

    @NotNull
    @Column(name = "event_place", nullable = false)
    private String eventPlace;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "content")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    private Statut statut;

    @NotNull
    @Column(name = "budget", nullable = false)
    private String budget;

    @ManyToOne(optional = false)
    @NotNull
    private Club club;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Event id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Event title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Instant getEventDate() {
        return this.eventDate;
    }

    public Event eventDate(Instant eventDate) {
        this.setEventDate(eventDate);
        return this;
    }

    public void setEventDate(Instant eventDate) {
        this.eventDate = eventDate;
    }

    public Instant getEventEnd() {
        return this.eventEnd;
    }

    public Event eventEnd(Instant eventEnd) {
        this.setEventEnd(eventEnd);
        return this;
    }

    public void setEventEnd(Instant eventEnd) {
        this.eventEnd = eventEnd;
    }

    public String getEventPlace() {
        return this.eventPlace;
    }

    public Event eventPlace(String eventPlace) {
        this.setEventPlace(eventPlace);
        return this;
    }

    public void setEventPlace(String eventPlace) {
        this.eventPlace = eventPlace;
    }

    public String getContent() {
        return this.content;
    }

    public Event content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Statut getStatut() {
        return this.statut;
    }

    public Event statut(Statut statut) {
        this.setStatut(statut);
        return this;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }

    public String getBudget() {
        return this.budget;
    }

    public Event budget(String budget) {
        this.setBudget(budget);
        return this;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public Club getClub() {
        return this.club;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    public Event club(Club club) {
        this.setClub(club);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Event)) {
            return false;
        }
        return id != null && id.equals(((Event) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Event{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", eventDate='" + getEventDate() + "'" +
            ", eventEnd='" + getEventEnd() + "'" +
            ", eventPlace='" + getEventPlace() + "'" +
            ", content='" + getContent() + "'" +
            ", statut='" + getStatut() + "'" +
            ", budget='" + getBudget() + "'" +
            "}";
    }
}
