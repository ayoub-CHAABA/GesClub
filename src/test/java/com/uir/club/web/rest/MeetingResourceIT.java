package com.uir.club.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.uir.club.IntegrationTest;
import com.uir.club.domain.Club;
import com.uir.club.domain.Meeting;
import com.uir.club.domain.enumeration.Statut;
import com.uir.club.repository.MeetingRepository;
import com.uir.club.service.criteria.MeetingCriteria;
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
 * Integration tests for the {@link MeetingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MeetingResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Instant DEFAULT_MEETING_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MEETING_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_MEETING_PLACE = "AAAAAAAAAA";
    private static final String UPDATED_MEETING_PLACE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final Statut DEFAULT_STATUT = Statut.CREATED;
    private static final Statut UPDATED_STATUT = Statut.VALIDATED;

    private static final String ENTITY_API_URL = "/api/meetings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMeetingMockMvc;

    private Meeting meeting;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Meeting createEntity(EntityManager em) {
        Meeting meeting = new Meeting()
            .title(DEFAULT_TITLE)
            .meetingDate(DEFAULT_MEETING_DATE)
            .meetingPlace(DEFAULT_MEETING_PLACE)
            .content(DEFAULT_CONTENT)
            .statut(DEFAULT_STATUT);
        // Add required entity
        Club club;
        if (TestUtil.findAll(em, Club.class).isEmpty()) {
            club = ClubResourceIT.createEntity(em);
            em.persist(club);
            em.flush();
        } else {
            club = TestUtil.findAll(em, Club.class).get(0);
        }
        meeting.setClub(club);
        return meeting;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Meeting createUpdatedEntity(EntityManager em) {
        Meeting meeting = new Meeting()
            .title(UPDATED_TITLE)
            .meetingDate(UPDATED_MEETING_DATE)
            .meetingPlace(UPDATED_MEETING_PLACE)
            .content(UPDATED_CONTENT)
            .statut(UPDATED_STATUT);
        // Add required entity
        Club club;
        if (TestUtil.findAll(em, Club.class).isEmpty()) {
            club = ClubResourceIT.createUpdatedEntity(em);
            em.persist(club);
            em.flush();
        } else {
            club = TestUtil.findAll(em, Club.class).get(0);
        }
        meeting.setClub(club);
        return meeting;
    }

    @BeforeEach
    public void initTest() {
        meeting = createEntity(em);
    }

    @Test
    @Transactional
    void createMeeting() throws Exception {
        int databaseSizeBeforeCreate = meetingRepository.findAll().size();
        // Create the Meeting
        restMeetingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(meeting)))
            .andExpect(status().isCreated());

        // Validate the Meeting in the database
        List<Meeting> meetingList = meetingRepository.findAll();
        assertThat(meetingList).hasSize(databaseSizeBeforeCreate + 1);
        Meeting testMeeting = meetingList.get(meetingList.size() - 1);
        assertThat(testMeeting.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testMeeting.getMeetingDate()).isEqualTo(DEFAULT_MEETING_DATE);
        assertThat(testMeeting.getMeetingPlace()).isEqualTo(DEFAULT_MEETING_PLACE);
        assertThat(testMeeting.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testMeeting.getStatut()).isEqualTo(DEFAULT_STATUT);
    }

    @Test
    @Transactional
    void createMeetingWithExistingId() throws Exception {
        // Create the Meeting with an existing ID
        meeting.setId(1L);

        int databaseSizeBeforeCreate = meetingRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMeetingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(meeting)))
            .andExpect(status().isBadRequest());

        // Validate the Meeting in the database
        List<Meeting> meetingList = meetingRepository.findAll();
        assertThat(meetingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = meetingRepository.findAll().size();
        // set the field null
        meeting.setTitle(null);

        // Create the Meeting, which fails.

        restMeetingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(meeting)))
            .andExpect(status().isBadRequest());

        List<Meeting> meetingList = meetingRepository.findAll();
        assertThat(meetingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMeetingDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = meetingRepository.findAll().size();
        // set the field null
        meeting.setMeetingDate(null);

        // Create the Meeting, which fails.

        restMeetingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(meeting)))
            .andExpect(status().isBadRequest());

        List<Meeting> meetingList = meetingRepository.findAll();
        assertThat(meetingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMeetingPlaceIsRequired() throws Exception {
        int databaseSizeBeforeTest = meetingRepository.findAll().size();
        // set the field null
        meeting.setMeetingPlace(null);

        // Create the Meeting, which fails.

        restMeetingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(meeting)))
            .andExpect(status().isBadRequest());

        List<Meeting> meetingList = meetingRepository.findAll();
        assertThat(meetingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMeetings() throws Exception {
        // Initialize the database
        meetingRepository.saveAndFlush(meeting);

        // Get all the meetingList
        restMeetingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(meeting.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].meetingDate").value(hasItem(DEFAULT_MEETING_DATE.toString())))
            .andExpect(jsonPath("$.[*].meetingPlace").value(hasItem(DEFAULT_MEETING_PLACE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())));
    }

    @Test
    @Transactional
    void getMeeting() throws Exception {
        // Initialize the database
        meetingRepository.saveAndFlush(meeting);

        // Get the meeting
        restMeetingMockMvc
            .perform(get(ENTITY_API_URL_ID, meeting.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(meeting.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.meetingDate").value(DEFAULT_MEETING_DATE.toString()))
            .andExpect(jsonPath("$.meetingPlace").value(DEFAULT_MEETING_PLACE))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT.toString()));
    }

    @Test
    @Transactional
    void getMeetingsByIdFiltering() throws Exception {
        // Initialize the database
        meetingRepository.saveAndFlush(meeting);

        Long id = meeting.getId();

        defaultMeetingShouldBeFound("id.equals=" + id);
        defaultMeetingShouldNotBeFound("id.notEquals=" + id);

        defaultMeetingShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMeetingShouldNotBeFound("id.greaterThan=" + id);

        defaultMeetingShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMeetingShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMeetingsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        meetingRepository.saveAndFlush(meeting);

        // Get all the meetingList where title equals to DEFAULT_TITLE
        defaultMeetingShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the meetingList where title equals to UPDATED_TITLE
        defaultMeetingShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllMeetingsByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        meetingRepository.saveAndFlush(meeting);

        // Get all the meetingList where title not equals to DEFAULT_TITLE
        defaultMeetingShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the meetingList where title not equals to UPDATED_TITLE
        defaultMeetingShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllMeetingsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        meetingRepository.saveAndFlush(meeting);

        // Get all the meetingList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultMeetingShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the meetingList where title equals to UPDATED_TITLE
        defaultMeetingShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllMeetingsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        meetingRepository.saveAndFlush(meeting);

        // Get all the meetingList where title is not null
        defaultMeetingShouldBeFound("title.specified=true");

        // Get all the meetingList where title is null
        defaultMeetingShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllMeetingsByTitleContainsSomething() throws Exception {
        // Initialize the database
        meetingRepository.saveAndFlush(meeting);

        // Get all the meetingList where title contains DEFAULT_TITLE
        defaultMeetingShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the meetingList where title contains UPDATED_TITLE
        defaultMeetingShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllMeetingsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        meetingRepository.saveAndFlush(meeting);

        // Get all the meetingList where title does not contain DEFAULT_TITLE
        defaultMeetingShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the meetingList where title does not contain UPDATED_TITLE
        defaultMeetingShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllMeetingsByMeetingDateIsEqualToSomething() throws Exception {
        // Initialize the database
        meetingRepository.saveAndFlush(meeting);

        // Get all the meetingList where meetingDate equals to DEFAULT_MEETING_DATE
        defaultMeetingShouldBeFound("meetingDate.equals=" + DEFAULT_MEETING_DATE);

        // Get all the meetingList where meetingDate equals to UPDATED_MEETING_DATE
        defaultMeetingShouldNotBeFound("meetingDate.equals=" + UPDATED_MEETING_DATE);
    }

    @Test
    @Transactional
    void getAllMeetingsByMeetingDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        meetingRepository.saveAndFlush(meeting);

        // Get all the meetingList where meetingDate not equals to DEFAULT_MEETING_DATE
        defaultMeetingShouldNotBeFound("meetingDate.notEquals=" + DEFAULT_MEETING_DATE);

        // Get all the meetingList where meetingDate not equals to UPDATED_MEETING_DATE
        defaultMeetingShouldBeFound("meetingDate.notEquals=" + UPDATED_MEETING_DATE);
    }

    @Test
    @Transactional
    void getAllMeetingsByMeetingDateIsInShouldWork() throws Exception {
        // Initialize the database
        meetingRepository.saveAndFlush(meeting);

        // Get all the meetingList where meetingDate in DEFAULT_MEETING_DATE or UPDATED_MEETING_DATE
        defaultMeetingShouldBeFound("meetingDate.in=" + DEFAULT_MEETING_DATE + "," + UPDATED_MEETING_DATE);

        // Get all the meetingList where meetingDate equals to UPDATED_MEETING_DATE
        defaultMeetingShouldNotBeFound("meetingDate.in=" + UPDATED_MEETING_DATE);
    }

    @Test
    @Transactional
    void getAllMeetingsByMeetingDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        meetingRepository.saveAndFlush(meeting);

        // Get all the meetingList where meetingDate is not null
        defaultMeetingShouldBeFound("meetingDate.specified=true");

        // Get all the meetingList where meetingDate is null
        defaultMeetingShouldNotBeFound("meetingDate.specified=false");
    }

    @Test
    @Transactional
    void getAllMeetingsByMeetingPlaceIsEqualToSomething() throws Exception {
        // Initialize the database
        meetingRepository.saveAndFlush(meeting);

        // Get all the meetingList where meetingPlace equals to DEFAULT_MEETING_PLACE
        defaultMeetingShouldBeFound("meetingPlace.equals=" + DEFAULT_MEETING_PLACE);

        // Get all the meetingList where meetingPlace equals to UPDATED_MEETING_PLACE
        defaultMeetingShouldNotBeFound("meetingPlace.equals=" + UPDATED_MEETING_PLACE);
    }

    @Test
    @Transactional
    void getAllMeetingsByMeetingPlaceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        meetingRepository.saveAndFlush(meeting);

        // Get all the meetingList where meetingPlace not equals to DEFAULT_MEETING_PLACE
        defaultMeetingShouldNotBeFound("meetingPlace.notEquals=" + DEFAULT_MEETING_PLACE);

        // Get all the meetingList where meetingPlace not equals to UPDATED_MEETING_PLACE
        defaultMeetingShouldBeFound("meetingPlace.notEquals=" + UPDATED_MEETING_PLACE);
    }

    @Test
    @Transactional
    void getAllMeetingsByMeetingPlaceIsInShouldWork() throws Exception {
        // Initialize the database
        meetingRepository.saveAndFlush(meeting);

        // Get all the meetingList where meetingPlace in DEFAULT_MEETING_PLACE or UPDATED_MEETING_PLACE
        defaultMeetingShouldBeFound("meetingPlace.in=" + DEFAULT_MEETING_PLACE + "," + UPDATED_MEETING_PLACE);

        // Get all the meetingList where meetingPlace equals to UPDATED_MEETING_PLACE
        defaultMeetingShouldNotBeFound("meetingPlace.in=" + UPDATED_MEETING_PLACE);
    }

    @Test
    @Transactional
    void getAllMeetingsByMeetingPlaceIsNullOrNotNull() throws Exception {
        // Initialize the database
        meetingRepository.saveAndFlush(meeting);

        // Get all the meetingList where meetingPlace is not null
        defaultMeetingShouldBeFound("meetingPlace.specified=true");

        // Get all the meetingList where meetingPlace is null
        defaultMeetingShouldNotBeFound("meetingPlace.specified=false");
    }

    @Test
    @Transactional
    void getAllMeetingsByMeetingPlaceContainsSomething() throws Exception {
        // Initialize the database
        meetingRepository.saveAndFlush(meeting);

        // Get all the meetingList where meetingPlace contains DEFAULT_MEETING_PLACE
        defaultMeetingShouldBeFound("meetingPlace.contains=" + DEFAULT_MEETING_PLACE);

        // Get all the meetingList where meetingPlace contains UPDATED_MEETING_PLACE
        defaultMeetingShouldNotBeFound("meetingPlace.contains=" + UPDATED_MEETING_PLACE);
    }

    @Test
    @Transactional
    void getAllMeetingsByMeetingPlaceNotContainsSomething() throws Exception {
        // Initialize the database
        meetingRepository.saveAndFlush(meeting);

        // Get all the meetingList where meetingPlace does not contain DEFAULT_MEETING_PLACE
        defaultMeetingShouldNotBeFound("meetingPlace.doesNotContain=" + DEFAULT_MEETING_PLACE);

        // Get all the meetingList where meetingPlace does not contain UPDATED_MEETING_PLACE
        defaultMeetingShouldBeFound("meetingPlace.doesNotContain=" + UPDATED_MEETING_PLACE);
    }

    @Test
    @Transactional
    void getAllMeetingsByStatutIsEqualToSomething() throws Exception {
        // Initialize the database
        meetingRepository.saveAndFlush(meeting);

        // Get all the meetingList where statut equals to DEFAULT_STATUT
        defaultMeetingShouldBeFound("statut.equals=" + DEFAULT_STATUT);

        // Get all the meetingList where statut equals to UPDATED_STATUT
        defaultMeetingShouldNotBeFound("statut.equals=" + UPDATED_STATUT);
    }

    @Test
    @Transactional
    void getAllMeetingsByStatutIsNotEqualToSomething() throws Exception {
        // Initialize the database
        meetingRepository.saveAndFlush(meeting);

        // Get all the meetingList where statut not equals to DEFAULT_STATUT
        defaultMeetingShouldNotBeFound("statut.notEquals=" + DEFAULT_STATUT);

        // Get all the meetingList where statut not equals to UPDATED_STATUT
        defaultMeetingShouldBeFound("statut.notEquals=" + UPDATED_STATUT);
    }

    @Test
    @Transactional
    void getAllMeetingsByStatutIsInShouldWork() throws Exception {
        // Initialize the database
        meetingRepository.saveAndFlush(meeting);

        // Get all the meetingList where statut in DEFAULT_STATUT or UPDATED_STATUT
        defaultMeetingShouldBeFound("statut.in=" + DEFAULT_STATUT + "," + UPDATED_STATUT);

        // Get all the meetingList where statut equals to UPDATED_STATUT
        defaultMeetingShouldNotBeFound("statut.in=" + UPDATED_STATUT);
    }

    @Test
    @Transactional
    void getAllMeetingsByStatutIsNullOrNotNull() throws Exception {
        // Initialize the database
        meetingRepository.saveAndFlush(meeting);

        // Get all the meetingList where statut is not null
        defaultMeetingShouldBeFound("statut.specified=true");

        // Get all the meetingList where statut is null
        defaultMeetingShouldNotBeFound("statut.specified=false");
    }

    @Test
    @Transactional
    void getAllMeetingsByClubIsEqualToSomething() throws Exception {
        // Initialize the database
        meetingRepository.saveAndFlush(meeting);
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
        meeting.setClub(club);
        meetingRepository.saveAndFlush(meeting);
        Long clubId = club.getId();

        // Get all the meetingList where club equals to clubId
        defaultMeetingShouldBeFound("clubId.equals=" + clubId);

        // Get all the meetingList where club equals to (clubId + 1)
        defaultMeetingShouldNotBeFound("clubId.equals=" + (clubId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMeetingShouldBeFound(String filter) throws Exception {
        restMeetingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(meeting.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].meetingDate").value(hasItem(DEFAULT_MEETING_DATE.toString())))
            .andExpect(jsonPath("$.[*].meetingPlace").value(hasItem(DEFAULT_MEETING_PLACE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())));

        // Check, that the count call also returns 1
        restMeetingMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMeetingShouldNotBeFound(String filter) throws Exception {
        restMeetingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMeetingMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMeeting() throws Exception {
        // Get the meeting
        restMeetingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMeeting() throws Exception {
        // Initialize the database
        meetingRepository.saveAndFlush(meeting);

        int databaseSizeBeforeUpdate = meetingRepository.findAll().size();

        // Update the meeting
        Meeting updatedMeeting = meetingRepository.findById(meeting.getId()).get();
        // Disconnect from session so that the updates on updatedMeeting are not directly saved in db
        em.detach(updatedMeeting);
        updatedMeeting
            .title(UPDATED_TITLE)
            .meetingDate(UPDATED_MEETING_DATE)
            .meetingPlace(UPDATED_MEETING_PLACE)
            .content(UPDATED_CONTENT)
            .statut(UPDATED_STATUT);

        restMeetingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMeeting.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMeeting))
            )
            .andExpect(status().isOk());

        // Validate the Meeting in the database
        List<Meeting> meetingList = meetingRepository.findAll();
        assertThat(meetingList).hasSize(databaseSizeBeforeUpdate);
        Meeting testMeeting = meetingList.get(meetingList.size() - 1);
        assertThat(testMeeting.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testMeeting.getMeetingDate()).isEqualTo(UPDATED_MEETING_DATE);
        assertThat(testMeeting.getMeetingPlace()).isEqualTo(UPDATED_MEETING_PLACE);
        assertThat(testMeeting.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testMeeting.getStatut()).isEqualTo(UPDATED_STATUT);
    }

    @Test
    @Transactional
    void putNonExistingMeeting() throws Exception {
        int databaseSizeBeforeUpdate = meetingRepository.findAll().size();
        meeting.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMeetingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, meeting.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(meeting))
            )
            .andExpect(status().isBadRequest());

        // Validate the Meeting in the database
        List<Meeting> meetingList = meetingRepository.findAll();
        assertThat(meetingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMeeting() throws Exception {
        int databaseSizeBeforeUpdate = meetingRepository.findAll().size();
        meeting.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeetingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(meeting))
            )
            .andExpect(status().isBadRequest());

        // Validate the Meeting in the database
        List<Meeting> meetingList = meetingRepository.findAll();
        assertThat(meetingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMeeting() throws Exception {
        int databaseSizeBeforeUpdate = meetingRepository.findAll().size();
        meeting.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeetingMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(meeting)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Meeting in the database
        List<Meeting> meetingList = meetingRepository.findAll();
        assertThat(meetingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMeetingWithPatch() throws Exception {
        // Initialize the database
        meetingRepository.saveAndFlush(meeting);

        int databaseSizeBeforeUpdate = meetingRepository.findAll().size();

        // Update the meeting using partial update
        Meeting partialUpdatedMeeting = new Meeting();
        partialUpdatedMeeting.setId(meeting.getId());

        restMeetingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMeeting.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMeeting))
            )
            .andExpect(status().isOk());

        // Validate the Meeting in the database
        List<Meeting> meetingList = meetingRepository.findAll();
        assertThat(meetingList).hasSize(databaseSizeBeforeUpdate);
        Meeting testMeeting = meetingList.get(meetingList.size() - 1);
        assertThat(testMeeting.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testMeeting.getMeetingDate()).isEqualTo(DEFAULT_MEETING_DATE);
        assertThat(testMeeting.getMeetingPlace()).isEqualTo(DEFAULT_MEETING_PLACE);
        assertThat(testMeeting.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testMeeting.getStatut()).isEqualTo(DEFAULT_STATUT);
    }

    @Test
    @Transactional
    void fullUpdateMeetingWithPatch() throws Exception {
        // Initialize the database
        meetingRepository.saveAndFlush(meeting);

        int databaseSizeBeforeUpdate = meetingRepository.findAll().size();

        // Update the meeting using partial update
        Meeting partialUpdatedMeeting = new Meeting();
        partialUpdatedMeeting.setId(meeting.getId());

        partialUpdatedMeeting
            .title(UPDATED_TITLE)
            .meetingDate(UPDATED_MEETING_DATE)
            .meetingPlace(UPDATED_MEETING_PLACE)
            .content(UPDATED_CONTENT)
            .statut(UPDATED_STATUT);

        restMeetingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMeeting.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMeeting))
            )
            .andExpect(status().isOk());

        // Validate the Meeting in the database
        List<Meeting> meetingList = meetingRepository.findAll();
        assertThat(meetingList).hasSize(databaseSizeBeforeUpdate);
        Meeting testMeeting = meetingList.get(meetingList.size() - 1);
        assertThat(testMeeting.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testMeeting.getMeetingDate()).isEqualTo(UPDATED_MEETING_DATE);
        assertThat(testMeeting.getMeetingPlace()).isEqualTo(UPDATED_MEETING_PLACE);
        assertThat(testMeeting.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testMeeting.getStatut()).isEqualTo(UPDATED_STATUT);
    }

    @Test
    @Transactional
    void patchNonExistingMeeting() throws Exception {
        int databaseSizeBeforeUpdate = meetingRepository.findAll().size();
        meeting.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMeetingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, meeting.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(meeting))
            )
            .andExpect(status().isBadRequest());

        // Validate the Meeting in the database
        List<Meeting> meetingList = meetingRepository.findAll();
        assertThat(meetingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMeeting() throws Exception {
        int databaseSizeBeforeUpdate = meetingRepository.findAll().size();
        meeting.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeetingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(meeting))
            )
            .andExpect(status().isBadRequest());

        // Validate the Meeting in the database
        List<Meeting> meetingList = meetingRepository.findAll();
        assertThat(meetingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMeeting() throws Exception {
        int databaseSizeBeforeUpdate = meetingRepository.findAll().size();
        meeting.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeetingMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(meeting)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Meeting in the database
        List<Meeting> meetingList = meetingRepository.findAll();
        assertThat(meetingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMeeting() throws Exception {
        // Initialize the database
        meetingRepository.saveAndFlush(meeting);

        int databaseSizeBeforeDelete = meetingRepository.findAll().size();

        // Delete the meeting
        restMeetingMockMvc
            .perform(delete(ENTITY_API_URL_ID, meeting.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Meeting> meetingList = meetingRepository.findAll();
        assertThat(meetingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
