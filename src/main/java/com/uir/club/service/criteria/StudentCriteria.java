package com.uir.club.service.criteria;

import com.uir.club.domain.enumeration.Membership;
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
 * Criteria class for the {@link com.uir.club.domain.Student} entity. This class is used
 * in {@link com.uir.club.web.rest.StudentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /students?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class StudentCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Membership
     */
    public static class MembershipFilter extends Filter<Membership> {

        public MembershipFilter() {}

        public MembershipFilter(MembershipFilter filter) {
            super(filter);
        }

        @Override
        public MembershipFilter copy() {
            return new MembershipFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter firstname;

    private StringFilter lastname;

    private StringFilter nationality;

    private StringFilter city;

    private StringFilter filiere;

    private StringFilter level;

    private StringFilter residency;

    private StringFilter tel;

    private StringFilter mail;

    private MembershipFilter role;

    private InstantFilter adhesion;

    private LongFilter userId;

    private LongFilter clubId;

    private Boolean distinct;

    public StudentCriteria() {}

    public StudentCriteria(StudentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.firstname = other.firstname == null ? null : other.firstname.copy();
        this.lastname = other.lastname == null ? null : other.lastname.copy();
        this.nationality = other.nationality == null ? null : other.nationality.copy();
        this.city = other.city == null ? null : other.city.copy();
        this.filiere = other.filiere == null ? null : other.filiere.copy();
        this.level = other.level == null ? null : other.level.copy();
        this.residency = other.residency == null ? null : other.residency.copy();
        this.tel = other.tel == null ? null : other.tel.copy();
        this.mail = other.mail == null ? null : other.mail.copy();
        this.role = other.role == null ? null : other.role.copy();
        this.adhesion = other.adhesion == null ? null : other.adhesion.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.clubId = other.clubId == null ? null : other.clubId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public StudentCriteria copy() {
        return new StudentCriteria(this);
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

    public StringFilter getFirstname() {
        return firstname;
    }

    public StringFilter firstname() {
        if (firstname == null) {
            firstname = new StringFilter();
        }
        return firstname;
    }

    public void setFirstname(StringFilter firstname) {
        this.firstname = firstname;
    }

    public StringFilter getLastname() {
        return lastname;
    }

    public StringFilter lastname() {
        if (lastname == null) {
            lastname = new StringFilter();
        }
        return lastname;
    }

    public void setLastname(StringFilter lastname) {
        this.lastname = lastname;
    }

    public StringFilter getNationality() {
        return nationality;
    }

    public StringFilter nationality() {
        if (nationality == null) {
            nationality = new StringFilter();
        }
        return nationality;
    }

    public void setNationality(StringFilter nationality) {
        this.nationality = nationality;
    }

    public StringFilter getCity() {
        return city;
    }

    public StringFilter city() {
        if (city == null) {
            city = new StringFilter();
        }
        return city;
    }

    public void setCity(StringFilter city) {
        this.city = city;
    }

    public StringFilter getFiliere() {
        return filiere;
    }

    public StringFilter filiere() {
        if (filiere == null) {
            filiere = new StringFilter();
        }
        return filiere;
    }

    public void setFiliere(StringFilter filiere) {
        this.filiere = filiere;
    }

    public StringFilter getLevel() {
        return level;
    }

    public StringFilter level() {
        if (level == null) {
            level = new StringFilter();
        }
        return level;
    }

    public void setLevel(StringFilter level) {
        this.level = level;
    }

    public StringFilter getResidency() {
        return residency;
    }

    public StringFilter residency() {
        if (residency == null) {
            residency = new StringFilter();
        }
        return residency;
    }

    public void setResidency(StringFilter residency) {
        this.residency = residency;
    }

    public StringFilter getTel() {
        return tel;
    }

    public StringFilter tel() {
        if (tel == null) {
            tel = new StringFilter();
        }
        return tel;
    }

    public void setTel(StringFilter tel) {
        this.tel = tel;
    }

    public StringFilter getMail() {
        return mail;
    }

    public StringFilter mail() {
        if (mail == null) {
            mail = new StringFilter();
        }
        return mail;
    }

    public void setMail(StringFilter mail) {
        this.mail = mail;
    }

    public MembershipFilter getRole() {
        return role;
    }

    public MembershipFilter role() {
        if (role == null) {
            role = new MembershipFilter();
        }
        return role;
    }

    public void setRole(MembershipFilter role) {
        this.role = role;
    }

    public InstantFilter getAdhesion() {
        return adhesion;
    }

    public InstantFilter adhesion() {
        if (adhesion == null) {
            adhesion = new InstantFilter();
        }
        return adhesion;
    }

    public void setAdhesion(InstantFilter adhesion) {
        this.adhesion = adhesion;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
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
        final StudentCriteria that = (StudentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(firstname, that.firstname) &&
            Objects.equals(lastname, that.lastname) &&
            Objects.equals(nationality, that.nationality) &&
            Objects.equals(city, that.city) &&
            Objects.equals(filiere, that.filiere) &&
            Objects.equals(level, that.level) &&
            Objects.equals(residency, that.residency) &&
            Objects.equals(tel, that.tel) &&
            Objects.equals(mail, that.mail) &&
            Objects.equals(role, that.role) &&
            Objects.equals(adhesion, that.adhesion) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(clubId, that.clubId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            firstname,
            lastname,
            nationality,
            city,
            filiere,
            level,
            residency,
            tel,
            mail,
            role,
            adhesion,
            userId,
            clubId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudentCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (firstname != null ? "firstname=" + firstname + ", " : "") +
            (lastname != null ? "lastname=" + lastname + ", " : "") +
            (nationality != null ? "nationality=" + nationality + ", " : "") +
            (city != null ? "city=" + city + ", " : "") +
            (filiere != null ? "filiere=" + filiere + ", " : "") +
            (level != null ? "level=" + level + ", " : "") +
            (residency != null ? "residency=" + residency + ", " : "") +
            (tel != null ? "tel=" + tel + ", " : "") +
            (mail != null ? "mail=" + mail + ", " : "") +
            (role != null ? "role=" + role + ", " : "") +
            (adhesion != null ? "adhesion=" + adhesion + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (clubId != null ? "clubId=" + clubId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
