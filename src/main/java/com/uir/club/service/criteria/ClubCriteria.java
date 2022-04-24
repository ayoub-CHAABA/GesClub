package com.uir.club.service.criteria;

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
 * Criteria class for the {@link com.uir.club.domain.Club} entity. This class is used
 * in {@link com.uir.club.web.rest.ClubResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /clubs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ClubCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter clubName;

    private InstantFilter creationDate;

    private Boolean distinct;

    public ClubCriteria() {}

    public ClubCriteria(ClubCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.clubName = other.clubName == null ? null : other.clubName.copy();
        this.creationDate = other.creationDate == null ? null : other.creationDate.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ClubCriteria copy() {
        return new ClubCriteria(this);
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

    public StringFilter getClubName() {
        return clubName;
    }

    public StringFilter clubName() {
        if (clubName == null) {
            clubName = new StringFilter();
        }
        return clubName;
    }

    public void setClubName(StringFilter clubName) {
        this.clubName = clubName;
    }

    public InstantFilter getCreationDate() {
        return creationDate;
    }

    public InstantFilter creationDate() {
        if (creationDate == null) {
            creationDate = new InstantFilter();
        }
        return creationDate;
    }

    public void setCreationDate(InstantFilter creationDate) {
        this.creationDate = creationDate;
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
        final ClubCriteria that = (ClubCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(clubName, that.clubName) &&
            Objects.equals(creationDate, that.creationDate) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clubName, creationDate, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClubCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (clubName != null ? "clubName=" + clubName + ", " : "") +
            (creationDate != null ? "creationDate=" + creationDate + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
