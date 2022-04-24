package com.uir.club.domain;

import com.uir.club.domain.enumeration.Membership;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * not an ignored comment
 */
@Schema(description = "not an ignored comment")
@Entity
@Table(name = "student")
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "firstname", nullable = false)
    private String firstname;

    @NotNull
    @Column(name = "lastname", nullable = false)
    private String lastname;

    @Column(name = "nationality")
    private String nationality;

    @Column(name = "city")
    private String city;

    @NotNull
    @Column(name = "filiere", nullable = false)
    private String filiere;

    @NotNull
    @Column(name = "level", nullable = false)
    private String level;

    @Column(name = "residency")
    private String residency;

    @NotNull
    @Column(name = "tel", nullable = false)
    private String tel;

    @NotNull
    @Column(name = "mail", nullable = false)
    private String mail;

    @Lob
    @Column(name = "picture")
    private byte[] picture;

    @Column(name = "picture_content_type")
    private String pictureContentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Membership role;

    @Column(name = "adhesion")
    private Instant adhesion;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private User user;

    @ManyToOne
    private Club club;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Student id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public Student firstname(String firstname) {
        this.setFirstname(firstname);
        return this;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return this.lastname;
    }

    public Student lastname(String lastname) {
        this.setLastname(lastname);
        return this;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getNationality() {
        return this.nationality;
    }

    public Student nationality(String nationality) {
        this.setNationality(nationality);
        return this;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getCity() {
        return this.city;
    }

    public Student city(String city) {
        this.setCity(city);
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getFiliere() {
        return this.filiere;
    }

    public Student filiere(String filiere) {
        this.setFiliere(filiere);
        return this;
    }

    public void setFiliere(String filiere) {
        this.filiere = filiere;
    }

    public String getLevel() {
        return this.level;
    }

    public Student level(String level) {
        this.setLevel(level);
        return this;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getResidency() {
        return this.residency;
    }

    public Student residency(String residency) {
        this.setResidency(residency);
        return this;
    }

    public void setResidency(String residency) {
        this.residency = residency;
    }

    public String getTel() {
        return this.tel;
    }

    public Student tel(String tel) {
        this.setTel(tel);
        return this;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getMail() {
        return this.mail;
    }

    public Student mail(String mail) {
        this.setMail(mail);
        return this;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public byte[] getPicture() {
        return this.picture;
    }

    public Student picture(byte[] picture) {
        this.setPicture(picture);
        return this;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getPictureContentType() {
        return this.pictureContentType;
    }

    public Student pictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
        return this;
    }

    public void setPictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
    }

    public Membership getRole() {
        return this.role;
    }

    public Student role(Membership role) {
        this.setRole(role);
        return this;
    }

    public void setRole(Membership role) {
        this.role = role;
    }

    public Instant getAdhesion() {
        return this.adhesion;
    }

    public Student adhesion(Instant adhesion) {
        this.setAdhesion(adhesion);
        return this;
    }

    public void setAdhesion(Instant adhesion) {
        this.adhesion = adhesion;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Student user(User user) {
        this.setUser(user);
        return this;
    }

    public Club getClub() {
        return this.club;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    public Student club(Club club) {
        this.setClub(club);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Student)) {
            return false;
        }
        return id != null && id.equals(((Student) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Student{" +
            "id=" + getId() +
            ", firstname='" + getFirstname() + "'" +
            ", lastname='" + getLastname() + "'" +
            ", nationality='" + getNationality() + "'" +
            ", city='" + getCity() + "'" +
            ", filiere='" + getFiliere() + "'" +
            ", level='" + getLevel() + "'" +
            ", residency='" + getResidency() + "'" +
            ", tel='" + getTel() + "'" +
            ", mail='" + getMail() + "'" +
            ", picture='" + getPicture() + "'" +
            ", pictureContentType='" + getPictureContentType() + "'" +
            ", role='" + getRole() + "'" +
            ", adhesion='" + getAdhesion() + "'" +
            "}";
    }
}
