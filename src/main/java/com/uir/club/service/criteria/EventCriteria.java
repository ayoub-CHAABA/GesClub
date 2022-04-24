package com.uir.club.service.criteria;

import com.uir.club.domain.enumeration.Statut;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.uir.club.domain.Event} entity. This class is used
 * in {@link com.uir.club.web.rest.EventResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /events?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EventCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Statut
     */
    public static class StatutFilter extends Filter<Statut> {

        public StatutFilter() {}

        public StatutFilter(StatutFilter filter) {
            super(filter);
        }

        @Override
        public StatutFilter copy() {
            return new StatutFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private InstantFilter eventDate;

    private InstantFilter eventEnd;

    private StringFilter eventPlace;

    private StatutFilter statut;

    private StringFilter budget;

    private LongFilter clubId;

    private Boolean distinct;

    public EventCriteria() {}

    public EventCriteria(EventCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.eventDate = other.eventDate == null ? null : other.eventDate.copy();
        this.eventEnd = other.eventEnd == null ? null : other.eventEnd.copy();
        this.eventPlace = other.eventPlace == null ? null : other.eventPlace.copy();
        this.statut = other.statut == null ? null : other.statut.copy();
        this.budget = other.budget == null ? null : other.budget.copy();
        this.clubId = other.clubId == null ? null : other.clubId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EventCriteria copy() {
        return new EventCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitle() {
        return title;
    }

    public StringFilter title() {
        if (title == null) {
            title = new StringFilter();
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public InstantFilter getEventDate() {
        return eventDate;
    }

    public InstantFilter eventDate() {
        if (eventDate == null) {
            eventDate = new InstantFilter();
        }
        return eventDate;
    }

    public void setEventDate(InstantFilter eventDate) {
        this.eventDate = eventDate;
    }

    public InstantFilter getEventEnd() {
        return eventEnd;
    }

    public InstantFilter eventEnd() {
        if (eventEnd == null) {
            eventEnd = new InstantFilter();
        }
        return eventEnd;
    }

    public void setEventEnd(InstantFilter eventEnd) {
        this.eventEnd = eventEnd;
    }

    public StringFilter getEventPlace() {
        return eventPlace;
    }

    public StringFilter eventPlace() {
        if (eventPlace == null) {
            eventPlace = new StringFilter();
        }
        return eventPlace;
    }

    public void setEventPlace(StringFilter eventPlace) {
        this.eventPlace = eventPlace;
    }

    public StatutFilter getStatut() {
        return statut;
    }

    public StatutFilter statut() {
        if (statut == null) {
            statut = new StatutFilter();
        }
        return statut;
    }

    public void setStatut(StatutFilter statut) {
        this.statut = statut;
    }

    public StringFilter getBudget() {
        return budget;
    }

    public StringFilter budget() {
        if (budget == null) {
            budget = new StringFilter();
        }
        return budget;
    }

    public void setBudget(StringFilter budget) {
        this.budget = budget;
    }

    public LongFilter getClubId() {
        return clubId;
    }

    public LongFilter clubId() {
        if (clubId == null) {
            clubId = new LongFilter();
        }
        return clubId;
    }

    public void setClubId(LongFilter clubId) {
        this.clubId = clubId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EventCriteria that = (EventCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(eventDate, that.eventDate) &&
            Objects.equals(eventEnd, that.eventEnd) &&
            Objects.equals(eventPlace, that.eventPlace) &&
            Objects.equals(statut, that.statut) &&
            Objects.equals(budget, that.budget) &&
            Objects.equals(clubId, that.clubId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, eventDate, eventEnd, eventPlace, statut, budget, clubId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (title != null ? "title=" + title + ", " : "") +
            (eventDate != null ? "eventDate=" + eventDate + ", " : "") +
            (eventEnd != null ? "eventEnd=" + eventEnd + ", " : "") +
            (eventPlace != null ? "eventPlace=" + eventPlace + ", " : "") +
            (statut != null ? "statut=" + statut + ", " : "") +
            (budget != null ? "budget=" + budget + ", " : "") +
            (clubId != null ? "clubId=" + clubId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
