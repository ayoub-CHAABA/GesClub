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
 * Criteria class for the {@link com.uir.club.domain.Meeting} entity. This class is used
 * in {@link com.uir.club.web.rest.MeetingResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /meetings?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MeetingCriteria implements Serializable, Criteria {

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

    private InstantFilter meetingDate;

    private StringFilter meetingPlace;

    private StatutFilter statut;

    private LongFilter clubId;

    private Boolean distinct;

    public MeetingCriteria() {}

    public MeetingCriteria(MeetingCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.meetingDate = other.meetingDate == null ? null : other.meetingDate.copy();
        this.meetingPlace = other.meetingPlace == null ? null : other.meetingPlace.copy();
        this.statut = other.statut == null ? null : other.statut.copy();
        this.clubId = other.clubId == null ? null : other.clubId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public MeetingCriteria copy() {
        return new MeetingCriteria(this);
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

    public InstantFilter getMeetingDate() {
        return meetingDate;
    }

    public InstantFilter meetingDate() {
        if (meetingDate == null) {
            meetingDate = new InstantFilter();
        }
        return meetingDate;
    }

    public void setMeetingDate(InstantFilter meetingDate) {
        this.meetingDate = meetingDate;
    }

    public StringFilter getMeetingPlace() {
        return meetingPlace;
    }

    public StringFilter meetingPlace() {
        if (meetingPlace == null) {
            meetingPlace = new StringFilter();
        }
        return meetingPlace;
    }

    public void setMeetingPlace(StringFilter meetingPlace) {
        this.meetingPlace = meetingPlace;
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
        final MeetingCriteria that = (MeetingCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(meetingDate, that.meetingDate) &&
            Objects.equals(meetingPlace, that.meetingPlace) &&
            Objects.equals(statut, that.statut) &&
            Objects.equals(clubId, that.clubId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, meetingDate, meetingPlace, statut, clubId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MeetingCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (title != null ? "title=" + title + ", " : "") +
            (meetingDate != null ? "meetingDate=" + meetingDate + ", " : "") +
            (meetingPlace != null ? "meetingPlace=" + meetingPlace + ", " : "") +
            (statut != null ? "statut=" + statut + ", " : "") +
            (clubId != null ? "clubId=" + clubId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
