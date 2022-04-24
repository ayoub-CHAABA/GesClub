package com.uir.club.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.uir.club.IntegrationTest;
import com.uir.club.domain.Club;
import com.uir.club.domain.Student;
import com.uir.club.domain.User;
import com.uir.club.domain.enumeration.Membership;
import com.uir.club.repository.StudentRepository;
import com.uir.club.service.criteria.StudentCriteria;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link StudentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StudentResourceIT {

    private static final String DEFAULT_FIRSTNAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRSTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_LASTNAME = "AAAAAAAAAA";
    private static final String UPDATED_LASTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_NATIONALITY = "AAAAAAAAAA";
    private static final String UPDATED_NATIONALITY = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_FILIERE = "AAAAAAAAAA";
    private static final String UPDATED_FILIERE = "BBBBBBBBBB";

    private static final String DEFAULT_LEVEL = "AAAAAAAAAA";
    private static final String UPDATED_LEVEL = "BBBBBBBBBB";

    private static final String DEFAULT_RESIDENCY = "AAAAAAAAAA";
    private static final String UPDATED_RESIDENCY = "BBBBBBBBBB";

    private static final String DEFAULT_TEL = "AAAAAAAAAA";
    private static final String UPDATED_TEL = "BBBBBBBBBB";

    private static final String DEFAULT_MAIL = "AAAAAAAAAA";
    private static final String UPDATED_MAIL = "BBBBBBBBBB";

    private static final byte[] DEFAULT_PICTURE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PICTURE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PICTURE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PICTURE_CONTENT_TYPE = "image/png";

    private static final Membership DEFAULT_ROLE = Membership.President;
    private static final Membership UPDATED_ROLE = Membership.Tresorie;

    private static final Instant DEFAULT_ADHESION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ADHESION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/students";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStudentMockMvc;

    private Student student;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Student createEntity(EntityManager em) {
        Student student = new Student()
            .firstname(DEFAULT_FIRSTNAME)
            .lastname(DEFAULT_LASTNAME)
            .nationality(DEFAULT_NATIONALITY)
            .city(DEFAULT_CITY)
            .filiere(DEFAULT_FILIERE)
            .level(DEFAULT_LEVEL)
            .residency(DEFAULT_RESIDENCY)
            .tel(DEFAULT_TEL)
            .mail(DEFAULT_MAIL)
            .picture(DEFAULT_PICTURE)
            .pictureContentType(DEFAULT_PICTURE_CONTENT_TYPE)
            .role(DEFAULT_ROLE)
            .adhesion(DEFAULT_ADHESION);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        student.setUser(user);
        return student;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Student createUpdatedEntity(EntityManager em) {
        Student student = new Student()
            .firstname(UPDATED_FIRSTNAME)
            .lastname(UPDATED_LASTNAME)
            .nationality(UPDATED_NATIONALITY)
            .city(UPDATED_CITY)
            .filiere(UPDATED_FILIERE)
            .level(UPDATED_LEVEL)
            .residency(UPDATED_RESIDENCY)
            .tel(UPDATED_TEL)
            .mail(UPDATED_MAIL)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .role(UPDATED_ROLE)
            .adhesion(UPDATED_ADHESION);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        student.setUser(user);
        return student;
    }

    @BeforeEach
    public void initTest() {
        student = createEntity(em);
    }

    @Test
    @Transactional
    void createStudent() throws Exception {
        int databaseSizeBeforeCreate = studentRepository.findAll().size();
        // Create the Student
        restStudentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(student)))
            .andExpect(status().isCreated());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeCreate + 1);
        Student testStudent = studentList.get(studentList.size() - 1);
        assertThat(testStudent.getFirstname()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(testStudent.getLastname()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(testStudent.getNationality()).isEqualTo(DEFAULT_NATIONALITY);
        assertThat(testStudent.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testStudent.getFiliere()).isEqualTo(DEFAULT_FILIERE);
        assertThat(testStudent.getLevel()).isEqualTo(DEFAULT_LEVEL);
        assertThat(testStudent.getResidency()).isEqualTo(DEFAULT_RESIDENCY);
        assertThat(testStudent.getTel()).isEqualTo(DEFAULT_TEL);
        assertThat(testStudent.getMail()).isEqualTo(DEFAULT_MAIL);
        assertThat(testStudent.getPicture()).isEqualTo(DEFAULT_PICTURE);
        assertThat(testStudent.getPictureContentType()).isEqualTo(DEFAULT_PICTURE_CONTENT_TYPE);
        assertThat(testStudent.getRole()).isEqualTo(DEFAULT_ROLE);
        assertThat(testStudent.getAdhesion()).isEqualTo(DEFAULT_ADHESION);
    }

    @Test
    @Transactional
    void createStudentWithExistingId() throws Exception {
        // Create the Student with an existing ID
        student.setId(1L);

        int databaseSizeBeforeCreate = studentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(student)))
            .andExpect(status().isBadRequest());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentRepository.findAll().size();
        // set the field null
        student.setFirstname(null);

        // Create the Student, which fails.

        restStudentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(student)))
            .andExpect(status().isBadRequest());

        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentRepository.findAll().size();
        // set the field null
        student.setLastname(null);

        // Create the Student, which fails.

        restStudentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(student)))
            .andExpect(status().isBadRequest());

        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFiliereIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentRepository.findAll().size();
        // set the field null
        student.setFiliere(null);

        // Create the Student, which fails.

        restStudentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(student)))
            .andExpect(status().isBadRequest());

        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLevelIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentRepository.findAll().size();
        // set the field null
        student.setLevel(null);

        // Create the Student, which fails.

        restStudentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(student)))
            .andExpect(status().isBadRequest());

        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTelIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentRepository.findAll().size();
        // set the field null
        student.setTel(null);

        // Create the Student, which fails.

        restStudentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(student)))
            .andExpect(status().isBadRequest());

        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMailIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentRepository.findAll().size();
        // set the field null
        student.setMail(null);

        // Create the Student, which fails.

        restStudentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(student)))
            .andExpect(status().isBadRequest());

        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllStudents() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList
        restStudentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(student.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstname").value(hasItem(DEFAULT_FIRSTNAME)))
            .andExpect(jsonPath("$.[*].lastname").value(hasItem(DEFAULT_LASTNAME)))
            .andExpect(jsonPath("$.[*].nationality").value(hasItem(DEFAULT_NATIONALITY)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].filiere").value(hasItem(DEFAULT_FILIERE)))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)))
            .andExpect(jsonPath("$.[*].residency").value(hasItem(DEFAULT_RESIDENCY)))
            .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL)))
            .andExpect(jsonPath("$.[*].mail").value(hasItem(DEFAULT_MAIL)))
            .andExpect(jsonPath("$.[*].pictureContentType").value(hasItem(DEFAULT_PICTURE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].picture").value(hasItem(Base64Utils.encodeToString(DEFAULT_PICTURE))))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())))
            .andExpect(jsonPath("$.[*].adhesion").value(hasItem(DEFAULT_ADHESION.toString())));
    }

    @Test
    @Transactional
    void getStudent() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get the student
        restStudentMockMvc
            .perform(get(ENTITY_API_URL_ID, student.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(student.getId().intValue()))
            .andExpect(jsonPath("$.firstname").value(DEFAULT_FIRSTNAME))
            .andExpect(jsonPath("$.lastname").value(DEFAULT_LASTNAME))
            .andExpect(jsonPath("$.nationality").value(DEFAULT_NATIONALITY))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.filiere").value(DEFAULT_FILIERE))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL))
            .andExpect(jsonPath("$.residency").value(DEFAULT_RESIDENCY))
            .andExpect(jsonPath("$.tel").value(DEFAULT_TEL))
            .andExpect(jsonPath("$.mail").value(DEFAULT_MAIL))
            .andExpect(jsonPath("$.pictureContentType").value(DEFAULT_PICTURE_CONTENT_TYPE))
            .andExpect(jsonPath("$.picture").value(Base64Utils.encodeToString(DEFAULT_PICTURE)))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE.toString()))
            .andExpect(jsonPath("$.adhesion").value(DEFAULT_ADHESION.toString()));
    }

    @Test
    @Transactional
    void getStudentsByIdFiltering() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        Long id = student.getId();

        defaultStudentShouldBeFound("id.equals=" + id);
        defaultStudentShouldNotBeFound("id.notEquals=" + id);

        defaultStudentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultStudentShouldNotBeFound("id.greaterThan=" + id);

        defaultStudentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultStudentShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllStudentsByFirstnameIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where firstname equals to DEFAULT_FIRSTNAME
        defaultStudentShouldBeFound("firstname.equals=" + DEFAULT_FIRSTNAME);

        // Get all the studentList where firstname equals to UPDATED_FIRSTNAME
        defaultStudentShouldNotBeFound("firstname.equals=" + UPDATED_FIRSTNAME);
    }

    @Test
    @Transactional
    void getAllStudentsByFirstnameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where firstname not equals to DEFAULT_FIRSTNAME
        defaultStudentShouldNotBeFound("firstname.notEquals=" + DEFAULT_FIRSTNAME);

        // Get all the studentList where firstname not equals to UPDATED_FIRSTNAME
        defaultStudentShouldBeFound("firstname.notEquals=" + UPDATED_FIRSTNAME);
    }

    @Test
    @Transactional
    void getAllStudentsByFirstnameIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where firstname in DEFAULT_FIRSTNAME or UPDATED_FIRSTNAME
        defaultStudentShouldBeFound("firstname.in=" + DEFAULT_FIRSTNAME + "," + UPDATED_FIRSTNAME);

        // Get all the studentList where firstname equals to UPDATED_FIRSTNAME
        defaultStudentShouldNotBeFound("firstname.in=" + UPDATED_FIRSTNAME);
    }

    @Test
    @Transactional
    void getAllStudentsByFirstnameIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where firstname is not null
        defaultStudentShouldBeFound("firstname.specified=true");

        // Get all the studentList where firstname is null
        defaultStudentShouldNotBeFound("firstname.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentsByFirstnameContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where firstname contains DEFAULT_FIRSTNAME
        defaultStudentShouldBeFound("firstname.contains=" + DEFAULT_FIRSTNAME);

        // Get all the studentList where firstname contains UPDATED_FIRSTNAME
        defaultStudentShouldNotBeFound("firstname.contains=" + UPDATED_FIRSTNAME);
    }

    @Test
    @Transactional
    void getAllStudentsByFirstnameNotContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where firstname does not contain DEFAULT_FIRSTNAME
        defaultStudentShouldNotBeFound("firstname.doesNotContain=" + DEFAULT_FIRSTNAME);

        // Get all the studentList where firstname does not contain UPDATED_FIRSTNAME
        defaultStudentShouldBeFound("firstname.doesNotContain=" + UPDATED_FIRSTNAME);
    }

    @Test
    @Transactional
    void getAllStudentsByLastnameIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where lastname equals to DEFAULT_LASTNAME
        defaultStudentShouldBeFound("lastname.equals=" + DEFAULT_LASTNAME);

        // Get all the studentList where lastname equals to UPDATED_LASTNAME
        defaultStudentShouldNotBeFound("lastname.equals=" + UPDATED_LASTNAME);
    }

    @Test
    @Transactional
    void getAllStudentsByLastnameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where lastname not equals to DEFAULT_LASTNAME
        defaultStudentShouldNotBeFound("lastname.notEquals=" + DEFAULT_LASTNAME);

        // Get all the studentList where lastname not equals to UPDATED_LASTNAME
        defaultStudentShouldBeFound("lastname.notEquals=" + UPDATED_LASTNAME);
    }

    @Test
    @Transactional
    void getAllStudentsByLastnameIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where lastname in DEFAULT_LASTNAME or UPDATED_LASTNAME
        defaultStudentShouldBeFound("lastname.in=" + DEFAULT_LASTNAME + "," + UPDATED_LASTNAME);

        // Get all the studentList where lastname equals to UPDATED_LASTNAME
        defaultStudentShouldNotBeFound("lastname.in=" + UPDATED_LASTNAME);
    }

    @Test
    @Transactional
    void getAllStudentsByLastnameIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where lastname is not null
        defaultStudentShouldBeFound("lastname.specified=true");

        // Get all the studentList where lastname is null
        defaultStudentShouldNotBeFound("lastname.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentsByLastnameContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where lastname contains DEFAULT_LASTNAME
        defaultStudentShouldBeFound("lastname.contains=" + DEFAULT_LASTNAME);

        // Get all the studentList where lastname contains UPDATED_LASTNAME
        defaultStudentShouldNotBeFound("lastname.contains=" + UPDATED_LASTNAME);
    }

    @Test
    @Transactional
    void getAllStudentsByLastnameNotContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where lastname does not contain DEFAULT_LASTNAME
        defaultStudentShouldNotBeFound("lastname.doesNotContain=" + DEFAULT_LASTNAME);

        // Get all the studentList where lastname does not contain UPDATED_LASTNAME
        defaultStudentShouldBeFound("lastname.doesNotContain=" + UPDATED_LASTNAME);
    }

    @Test
    @Transactional
    void getAllStudentsByNationalityIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where nationality equals to DEFAULT_NATIONALITY
        defaultStudentShouldBeFound("nationality.equals=" + DEFAULT_NATIONALITY);

        // Get all the studentList where nationality equals to UPDATED_NATIONALITY
        defaultStudentShouldNotBeFound("nationality.equals=" + UPDATED_NATIONALITY);
    }

    @Test
    @Transactional
    void getAllStudentsByNationalityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where nationality not equals to DEFAULT_NATIONALITY
        defaultStudentShouldNotBeFound("nationality.notEquals=" + DEFAULT_NATIONALITY);

        // Get all the studentList where nationality not equals to UPDATED_NATIONALITY
        defaultStudentShouldBeFound("nationality.notEquals=" + UPDATED_NATIONALITY);
    }

    @Test
    @Transactional
    void getAllStudentsByNationalityIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where nationality in DEFAULT_NATIONALITY or UPDATED_NATIONALITY
        defaultStudentShouldBeFound("nationality.in=" + DEFAULT_NATIONALITY + "," + UPDATED_NATIONALITY);

        // Get all the studentList where nationality equals to UPDATED_NATIONALITY
        defaultStudentShouldNotBeFound("nationality.in=" + UPDATED_NATIONALITY);
    }

    @Test
    @Transactional
    void getAllStudentsByNationalityIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where nationality is not null
        defaultStudentShouldBeFound("nationality.specified=true");

        // Get all the studentList where nationality is null
        defaultStudentShouldNotBeFound("nationality.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentsByNationalityContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where nationality contains DEFAULT_NATIONALITY
        defaultStudentShouldBeFound("nationality.contains=" + DEFAULT_NATIONALITY);

        // Get all the studentList where nationality contains UPDATED_NATIONALITY
        defaultStudentShouldNotBeFound("nationality.contains=" + UPDATED_NATIONALITY);
    }

    @Test
    @Transactional
    void getAllStudentsByNationalityNotContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where nationality does not contain DEFAULT_NATIONALITY
        defaultStudentShouldNotBeFound("nationality.doesNotContain=" + DEFAULT_NATIONALITY);

        // Get all the studentList where nationality does not contain UPDATED_NATIONALITY
        defaultStudentShouldBeFound("nationality.doesNotContain=" + UPDATED_NATIONALITY);
    }

    @Test
    @Transactional
    void getAllStudentsByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where city equals to DEFAULT_CITY
        defaultStudentShouldBeFound("city.equals=" + DEFAULT_CITY);

        // Get all the studentList where city equals to UPDATED_CITY
        defaultStudentShouldNotBeFound("city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllStudentsByCityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where city not equals to DEFAULT_CITY
        defaultStudentShouldNotBeFound("city.notEquals=" + DEFAULT_CITY);

        // Get all the studentList where city not equals to UPDATED_CITY
        defaultStudentShouldBeFound("city.notEquals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllStudentsByCityIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where city in DEFAULT_CITY or UPDATED_CITY
        defaultStudentShouldBeFound("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY);

        // Get all the studentList where city equals to UPDATED_CITY
        defaultStudentShouldNotBeFound("city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllStudentsByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where city is not null
        defaultStudentShouldBeFound("city.specified=true");

        // Get all the studentList where city is null
        defaultStudentShouldNotBeFound("city.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentsByCityContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where city contains DEFAULT_CITY
        defaultStudentShouldBeFound("city.contains=" + DEFAULT_CITY);

        // Get all the studentList where city contains UPDATED_CITY
        defaultStudentShouldNotBeFound("city.contains=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllStudentsByCityNotContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where city does not contain DEFAULT_CITY
        defaultStudentShouldNotBeFound("city.doesNotContain=" + DEFAULT_CITY);

        // Get all the studentList where city does not contain UPDATED_CITY
        defaultStudentShouldBeFound("city.doesNotContain=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllStudentsByFiliereIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where filiere equals to DEFAULT_FILIERE
        defaultStudentShouldBeFound("filiere.equals=" + DEFAULT_FILIERE);

        // Get all the studentList where filiere equals to UPDATED_FILIERE
        defaultStudentShouldNotBeFound("filiere.equals=" + UPDATED_FILIERE);
    }

    @Test
    @Transactional
    void getAllStudentsByFiliereIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where filiere not equals to DEFAULT_FILIERE
        defaultStudentShouldNotBeFound("filiere.notEquals=" + DEFAULT_FILIERE);

        // Get all the studentList where filiere not equals to UPDATED_FILIERE
        defaultStudentShouldBeFound("filiere.notEquals=" + UPDATED_FILIERE);
    }

    @Test
    @Transactional
    void getAllStudentsByFiliereIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where filiere in DEFAULT_FILIERE or UPDATED_FILIERE
        defaultStudentShouldBeFound("filiere.in=" + DEFAULT_FILIERE + "," + UPDATED_FILIERE);

        // Get all the studentList where filiere equals to UPDATED_FILIERE
        defaultStudentShouldNotBeFound("filiere.in=" + UPDATED_FILIERE);
    }

    @Test
    @Transactional
    void getAllStudentsByFiliereIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where filiere is not null
        defaultStudentShouldBeFound("filiere.specified=true");

        // Get all the studentList where filiere is null
        defaultStudentShouldNotBeFound("filiere.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentsByFiliereContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where filiere contains DEFAULT_FILIERE
        defaultStudentShouldBeFound("filiere.contains=" + DEFAULT_FILIERE);

        // Get all the studentList where filiere contains UPDATED_FILIERE
        defaultStudentShouldNotBeFound("filiere.contains=" + UPDATED_FILIERE);
    }

    @Test
    @Transactional
    void getAllStudentsByFiliereNotContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where filiere does not contain DEFAULT_FILIERE
        defaultStudentShouldNotBeFound("filiere.doesNotContain=" + DEFAULT_FILIERE);

        // Get all the studentList where filiere does not contain UPDATED_FILIERE
        defaultStudentShouldBeFound("filiere.doesNotContain=" + UPDATED_FILIERE);
    }

    @Test
    @Transactional
    void getAllStudentsByLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where level equals to DEFAULT_LEVEL
        defaultStudentShouldBeFound("level.equals=" + DEFAULT_LEVEL);

        // Get all the studentList where level equals to UPDATED_LEVEL
        defaultStudentShouldNotBeFound("level.equals=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void getAllStudentsByLevelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where level not equals to DEFAULT_LEVEL
        defaultStudentShouldNotBeFound("level.notEquals=" + DEFAULT_LEVEL);

        // Get all the studentList where level not equals to UPDATED_LEVEL
        defaultStudentShouldBeFound("level.notEquals=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void getAllStudentsByLevelIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where level in DEFAULT_LEVEL or UPDATED_LEVEL
        defaultStudentShouldBeFound("level.in=" + DEFAULT_LEVEL + "," + UPDATED_LEVEL);

        // Get all the studentList where level equals to UPDATED_LEVEL
        defaultStudentShouldNotBeFound("level.in=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void getAllStudentsByLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where level is not null
        defaultStudentShouldBeFound("level.specified=true");

        // Get all the studentList where level is null
        defaultStudentShouldNotBeFound("level.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentsByLevelContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where level contains DEFAULT_LEVEL
        defaultStudentShouldBeFound("level.contains=" + DEFAULT_LEVEL);

        // Get all the studentList where level contains UPDATED_LEVEL
        defaultStudentShouldNotBeFound("level.contains=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void getAllStudentsByLevelNotContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where level does not contain DEFAULT_LEVEL
        defaultStudentShouldNotBeFound("level.doesNotContain=" + DEFAULT_LEVEL);

        // Get all the studentList where level does not contain UPDATED_LEVEL
        defaultStudentShouldBeFound("level.doesNotContain=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void getAllStudentsByResidencyIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where residency equals to DEFAULT_RESIDENCY
        defaultStudentShouldBeFound("residency.equals=" + DEFAULT_RESIDENCY);

        // Get all the studentList where residency equals to UPDATED_RESIDENCY
        defaultStudentShouldNotBeFound("residency.equals=" + UPDATED_RESIDENCY);
    }

    @Test
    @Transactional
    void getAllStudentsByResidencyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where residency not equals to DEFAULT_RESIDENCY
        defaultStudentShouldNotBeFound("residency.notEquals=" + DEFAULT_RESIDENCY);

        // Get all the studentList where residency not equals to UPDATED_RESIDENCY
        defaultStudentShouldBeFound("residency.notEquals=" + UPDATED_RESIDENCY);
    }

    @Test
    @Transactional
    void getAllStudentsByResidencyIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where residency in DEFAULT_RESIDENCY or UPDATED_RESIDENCY
        defaultStudentShouldBeFound("residency.in=" + DEFAULT_RESIDENCY + "," + UPDATED_RESIDENCY);

        // Get all the studentList where residency equals to UPDATED_RESIDENCY
        defaultStudentShouldNotBeFound("residency.in=" + UPDATED_RESIDENCY);
    }

    @Test
    @Transactional
    void getAllStudentsByResidencyIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where residency is not null
        defaultStudentShouldBeFound("residency.specified=true");

        // Get all the studentList where residency is null
        defaultStudentShouldNotBeFound("residency.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentsByResidencyContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where residency contains DEFAULT_RESIDENCY
        defaultStudentShouldBeFound("residency.contains=" + DEFAULT_RESIDENCY);

        // Get all the studentList where residency contains UPDATED_RESIDENCY
        defaultStudentShouldNotBeFound("residency.contains=" + UPDATED_RESIDENCY);
    }

    @Test
    @Transactional
    void getAllStudentsByResidencyNotContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where residency does not contain DEFAULT_RESIDENCY
        defaultStudentShouldNotBeFound("residency.doesNotContain=" + DEFAULT_RESIDENCY);

        // Get all the studentList where residency does not contain UPDATED_RESIDENCY
        defaultStudentShouldBeFound("residency.doesNotContain=" + UPDATED_RESIDENCY);
    }

    @Test
    @Transactional
    void getAllStudentsByTelIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where tel equals to DEFAULT_TEL
        defaultStudentShouldBeFound("tel.equals=" + DEFAULT_TEL);

        // Get all the studentList where tel equals to UPDATED_TEL
        defaultStudentShouldNotBeFound("tel.equals=" + UPDATED_TEL);
    }

    @Test
    @Transactional
    void getAllStudentsByTelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where tel not equals to DEFAULT_TEL
        defaultStudentShouldNotBeFound("tel.notEquals=" + DEFAULT_TEL);

        // Get all the studentList where tel not equals to UPDATED_TEL
        defaultStudentShouldBeFound("tel.notEquals=" + UPDATED_TEL);
    }

    @Test
    @Transactional
    void getAllStudentsByTelIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where tel in DEFAULT_TEL or UPDATED_TEL
        defaultStudentShouldBeFound("tel.in=" + DEFAULT_TEL + "," + UPDATED_TEL);

        // Get all the studentList where tel equals to UPDATED_TEL
        defaultStudentShouldNotBeFound("tel.in=" + UPDATED_TEL);
    }

    @Test
    @Transactional
    void getAllStudentsByTelIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where tel is not null
        defaultStudentShouldBeFound("tel.specified=true");

        // Get all the studentList where tel is null
        defaultStudentShouldNotBeFound("tel.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentsByTelContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where tel contains DEFAULT_TEL
        defaultStudentShouldBeFound("tel.contains=" + DEFAULT_TEL);

        // Get all the studentList where tel contains UPDATED_TEL
        defaultStudentShouldNotBeFound("tel.contains=" + UPDATED_TEL);
    }

    @Test
    @Transactional
    void getAllStudentsByTelNotContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where tel does not contain DEFAULT_TEL
        defaultStudentShouldNotBeFound("tel.doesNotContain=" + DEFAULT_TEL);

        // Get all the studentList where tel does not contain UPDATED_TEL
        defaultStudentShouldBeFound("tel.doesNotContain=" + UPDATED_TEL);
    }

    @Test
    @Transactional
    void getAllStudentsByMailIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where mail equals to DEFAULT_MAIL
        defaultStudentShouldBeFound("mail.equals=" + DEFAULT_MAIL);

        // Get all the studentList where mail equals to UPDATED_MAIL
        defaultStudentShouldNotBeFound("mail.equals=" + UPDATED_MAIL);
    }

    @Test
    @Transactional
    void getAllStudentsByMailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where mail not equals to DEFAULT_MAIL
        defaultStudentShouldNotBeFound("mail.notEquals=" + DEFAULT_MAIL);

        // Get all the studentList where mail not equals to UPDATED_MAIL
        defaultStudentShouldBeFound("mail.notEquals=" + UPDATED_MAIL);
    }

    @Test
    @Transactional
    void getAllStudentsByMailIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where mail in DEFAULT_MAIL or UPDATED_MAIL
        defaultStudentShouldBeFound("mail.in=" + DEFAULT_MAIL + "," + UPDATED_MAIL);

        // Get all the studentList where mail equals to UPDATED_MAIL
        defaultStudentShouldNotBeFound("mail.in=" + UPDATED_MAIL);
    }

    @Test
    @Transactional
    void getAllStudentsByMailIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where mail is not null
        defaultStudentShouldBeFound("mail.specified=true");

        // Get all the studentList where mail is null
        defaultStudentShouldNotBeFound("mail.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentsByMailContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where mail contains DEFAULT_MAIL
        defaultStudentShouldBeFound("mail.contains=" + DEFAULT_MAIL);

        // Get all the studentList where mail contains UPDATED_MAIL
        defaultStudentShouldNotBeFound("mail.contains=" + UPDATED_MAIL);
    }

    @Test
    @Transactional
    void getAllStudentsByMailNotContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where mail does not contain DEFAULT_MAIL
        defaultStudentShouldNotBeFound("mail.doesNotContain=" + DEFAULT_MAIL);

        // Get all the studentList where mail does not contain UPDATED_MAIL
        defaultStudentShouldBeFound("mail.doesNotContain=" + UPDATED_MAIL);
    }

    @Test
    @Transactional
    void getAllStudentsByRoleIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where role equals to DEFAULT_ROLE
        defaultStudentShouldBeFound("role.equals=" + DEFAULT_ROLE);

        // Get all the studentList where role equals to UPDATED_ROLE
        defaultStudentShouldNotBeFound("role.equals=" + UPDATED_ROLE);
    }

    @Test
    @Transactional
    void getAllStudentsByRoleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where role not equals to DEFAULT_ROLE
        defaultStudentShouldNotBeFound("role.notEquals=" + DEFAULT_ROLE);

        // Get all the studentList where role not equals to UPDATED_ROLE
        defaultStudentShouldBeFound("role.notEquals=" + UPDATED_ROLE);
    }

    @Test
    @Transactional
    void getAllStudentsByRoleIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where role in DEFAULT_ROLE or UPDATED_ROLE
        defaultStudentShouldBeFound("role.in=" + DEFAULT_ROLE + "," + UPDATED_ROLE);

        // Get all the studentList where role equals to UPDATED_ROLE
        defaultStudentShouldNotBeFound("role.in=" + UPDATED_ROLE);
    }

    @Test
    @Transactional
    void getAllStudentsByRoleIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where role is not null
        defaultStudentShouldBeFound("role.specified=true");

        // Get all the studentList where role is null
        defaultStudentShouldNotBeFound("role.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentsByAdhesionIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where adhesion equals to DEFAULT_ADHESION
        defaultStudentShouldBeFound("adhesion.equals=" + DEFAULT_ADHESION);

        // Get all the studentList where adhesion equals to UPDATED_ADHESION
        defaultStudentShouldNotBeFound("adhesion.equals=" + UPDATED_ADHESION);
    }

    @Test
    @Transactional
    void getAllStudentsByAdhesionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where adhesion not equals to DEFAULT_ADHESION
        defaultStudentShouldNotBeFound("adhesion.notEquals=" + DEFAULT_ADHESION);

        // Get all the studentList where adhesion not equals to UPDATED_ADHESION
        defaultStudentShouldBeFound("adhesion.notEquals=" + UPDATED_ADHESION);
    }

    @Test
    @Transactional
    void getAllStudentsByAdhesionIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where adhesion in DEFAULT_ADHESION or UPDATED_ADHESION
        defaultStudentShouldBeFound("adhesion.in=" + DEFAULT_ADHESION + "," + UPDATED_ADHESION);

        // Get all the studentList where adhesion equals to UPDATED_ADHESION
        defaultStudentShouldNotBeFound("adhesion.in=" + UPDATED_ADHESION);
    }

    @Test
    @Transactional
    void getAllStudentsByAdhesionIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where adhesion is not null
        defaultStudentShouldBeFound("adhesion.specified=true");

        // Get all the studentList where adhesion is null
        defaultStudentShouldNotBeFound("adhesion.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentsByUserIsEqualToSomething() throws Exception {
        // Get already existing entity
        User user = student.getUser();
        studentRepository.saveAndFlush(student);
        Long userId = user.getId();

        // Get all the studentList where user equals to userId
        defaultStudentShouldBeFound("userId.equals=" + userId);

        // Get all the studentList where user equals to (userId + 1)
        defaultStudentShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllStudentsByClubIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);
        Club club;
        if (TestUtil.findAll(em, Club.class).isEmpty()) {
            club = ClubResourceIT.createEntity(em);
            em.persist(club);
            em.flush();
        } else {
            club = TestUtil.findAll(em, Club.class).get(0);
        }
        em.persist(club);
        em.flush();
        student.setClub(club);
        studentRepository.saveAndFlush(student);
        Long clubId = club.getId();

        // Get all the studentList where club equals to clubId
        defaultStudentShouldBeFound("clubId.equals=" + clubId);

        // Get all the studentList where club equals to (clubId + 1)
        defaultStudentShouldNotBeFound("clubId.equals=" + (clubId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultStudentShouldBeFound(String filter) throws Exception {
        restStudentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(student.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstname").value(hasItem(DEFAULT_FIRSTNAME)))
            .andExpect(jsonPath("$.[*].lastname").value(hasItem(DEFAULT_LASTNAME)))
            .andExpect(jsonPath("$.[*].nationality").value(hasItem(DEFAULT_NATIONALITY)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].filiere").value(hasItem(DEFAULT_FILIERE)))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)))
            .andExpect(jsonPath("$.[*].residency").value(hasItem(DEFAULT_RESIDENCY)))
            .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL)))
            .andExpect(jsonPath("$.[*].mail").value(hasItem(DEFAULT_MAIL)))
            .andExpect(jsonPath("$.[*].pictureContentType").value(hasItem(DEFAULT_PICTURE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].picture").value(hasItem(Base64Utils.encodeToString(DEFAULT_PICTURE))))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())))
            .andExpect(jsonPath("$.[*].adhesion").value(hasItem(DEFAULT_ADHESION.toString())));

        // Check, that the count call also returns 1
        restStudentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultStudentShouldNotBeFound(String filter) throws Exception {
        restStudentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStudentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingStudent() throws Exception {
        // Get the student
        restStudentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewStudent() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        int databaseSizeBeforeUpdate = studentRepository.findAll().size();

        // Update the student
        Student updatedStudent = studentRepository.findById(student.getId()).get();
        // Disconnect from session so that the updates on updatedStudent are not directly saved in db
        em.detach(updatedStudent);
        updatedStudent
            .firstname(UPDATED_FIRSTNAME)
            .lastname(UPDATED_LASTNAME)
            .nationality(UPDATED_NATIONALITY)
            .city(UPDATED_CITY)
            .filiere(UPDATED_FILIERE)
            .level(UPDATED_LEVEL)
            .residency(UPDATED_RESIDENCY)
            .tel(UPDATED_TEL)
            .mail(UPDATED_MAIL)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .role(UPDATED_ROLE)
            .adhesion(UPDATED_ADHESION);

        restStudentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedStudent.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedStudent))
            )
            .andExpect(status().isOk());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
        Student testStudent = studentList.get(studentList.size() - 1);
        assertThat(testStudent.getFirstname()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testStudent.getLastname()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testStudent.getNationality()).isEqualTo(UPDATED_NATIONALITY);
        assertThat(testStudent.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testStudent.getFiliere()).isEqualTo(UPDATED_FILIERE);
        assertThat(testStudent.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testStudent.getResidency()).isEqualTo(UPDATED_RESIDENCY);
        assertThat(testStudent.getTel()).isEqualTo(UPDATED_TEL);
        assertThat(testStudent.getMail()).isEqualTo(UPDATED_MAIL);
        assertThat(testStudent.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testStudent.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
        assertThat(testStudent.getRole()).isEqualTo(UPDATED_ROLE);
        assertThat(testStudent.getAdhesion()).isEqualTo(UPDATED_ADHESION);
    }

    @Test
    @Transactional
    void putNonExistingStudent() throws Exception {
        int databaseSizeBeforeUpdate = studentRepository.findAll().size();
        student.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, student.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(student))
            )
            .andExpect(status().isBadRequest());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStudent() throws Exception {
        int databaseSizeBeforeUpdate = studentRepository.findAll().size();
        student.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(student))
            )
            .andExpect(status().isBadRequest());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStudent() throws Exception {
        int databaseSizeBeforeUpdate = studentRepository.findAll().size();
        student.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(student)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStudentWithPatch() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        int databaseSizeBeforeUpdate = studentRepository.findAll().size();

        // Update the student using partial update
        Student partialUpdatedStudent = new Student();
        partialUpdatedStudent.setId(student.getId());

        partialUpdatedStudent
            .nationality(UPDATED_NATIONALITY)
            .city(UPDATED_CITY)
            .filiere(UPDATED_FILIERE)
            .mail(UPDATED_MAIL)
            .adhesion(UPDATED_ADHESION);

        restStudentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudent.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStudent))
            )
            .andExpect(status().isOk());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
        Student testStudent = studentList.get(studentList.size() - 1);
        assertThat(testStudent.getFirstname()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(testStudent.getLastname()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(testStudent.getNationality()).isEqualTo(UPDATED_NATIONALITY);
        assertThat(testStudent.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testStudent.getFiliere()).isEqualTo(UPDATED_FILIERE);
        assertThat(testStudent.getLevel()).isEqualTo(DEFAULT_LEVEL);
        assertThat(testStudent.getResidency()).isEqualTo(DEFAULT_RESIDENCY);
        assertThat(testStudent.getTel()).isEqualTo(DEFAULT_TEL);
        assertThat(testStudent.getMail()).isEqualTo(UPDATED_MAIL);
        assertThat(testStudent.getPicture()).isEqualTo(DEFAULT_PICTURE);
        assertThat(testStudent.getPictureContentType()).isEqualTo(DEFAULT_PICTURE_CONTENT_TYPE);
        assertThat(testStudent.getRole()).isEqualTo(DEFAULT_ROLE);
        assertThat(testStudent.getAdhesion()).isEqualTo(UPDATED_ADHESION);
    }

    @Test
    @Transactional
    void fullUpdateStudentWithPatch() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        int databaseSizeBeforeUpdate = studentRepository.findAll().size();

        // Update the student using partial update
        Student partialUpdatedStudent = new Student();
        partialUpdatedStudent.setId(student.getId());

        partialUpdatedStudent
            .firstname(UPDATED_FIRSTNAME)
            .lastname(UPDATED_LASTNAME)
            .nationality(UPDATED_NATIONALITY)
            .city(UPDATED_CITY)
            .filiere(UPDATED_FILIERE)
            .level(UPDATED_LEVEL)
            .residency(UPDATED_RESIDENCY)
            .tel(UPDATED_TEL)
            .mail(UPDATED_MAIL)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .role(UPDATED_ROLE)
            .adhesion(UPDATED_ADHESION);

        restStudentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudent.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStudent))
            )
            .andExpect(status().isOk());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
        Student testStudent = studentList.get(studentList.size() - 1);
        assertThat(testStudent.getFirstname()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testStudent.getLastname()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testStudent.getNationality()).isEqualTo(UPDATED_NATIONALITY);
        assertThat(testStudent.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testStudent.getFiliere()).isEqualTo(UPDATED_FILIERE);
        assertThat(testStudent.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testStudent.getResidency()).isEqualTo(UPDATED_RESIDENCY);
        assertThat(testStudent.getTel()).isEqualTo(UPDATED_TEL);
        assertThat(testStudent.getMail()).isEqualTo(UPDATED_MAIL);
        assertThat(testStudent.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testStudent.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
        assertThat(testStudent.getRole()).isEqualTo(UPDATED_ROLE);
        assertThat(testStudent.getAdhesion()).isEqualTo(UPDATED_ADHESION);
    }

    @Test
    @Transactional
    void patchNonExistingStudent() throws Exception {
        int databaseSizeBeforeUpdate = studentRepository.findAll().size();
        student.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, student.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(student))
            )
            .andExpect(status().isBadRequest());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStudent() throws Exception {
        int databaseSizeBeforeUpdate = studentRepository.findAll().size();
        student.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(student))
            )
            .andExpect(status().isBadRequest());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStudent() throws Exception {
        int databaseSizeBeforeUpdate = studentRepository.findAll().size();
        student.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(student)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStudent() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        int databaseSizeBeforeDelete = studentRepository.findAll().size();

        // Delete the student
        restStudentMockMvc
            .perform(delete(ENTITY_API_URL_ID, student.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
