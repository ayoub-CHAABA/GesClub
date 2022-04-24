package com.uir.club.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.uir.club.IntegrationTest;
import com.uir.club.domain.Club;
import com.uir.club.domain.Event;
import com.uir.club.domain.enumeration.Statut;
import com.uir.club.repository.EventRepository;
import com.uir.club.service.criteria.EventCriteria;
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
 * Integration tests for the {@link EventResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EventResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Instant DEFAULT_EVENT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EVENT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_EVENT_END = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EVENT_END = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_EVENT_PLACE = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_PLACE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final Statut DEFAULT_STATUT = Statut.CREATED;
    private static final Statut UPDATED_STATUT = Statut.VALIDATED;

    private static final String DEFAULT_BUDGET = "AAAAAAAAAA";
    private static final String UPDATED_BUDGET = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/events";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEventMockMvc;

    private Event event;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Event createEntity(EntityManager em) {
        Event event = new Event()
            .title(DEFAULT_TITLE)
            .eventDate(DEFAULT_EVENT_DATE)
            .eventEnd(DEFAULT_EVENT_END)
            .eventPlace(DEFAULT_EVENT_PLACE)
            .content(DEFAULT_CONTENT)
            .statut(DEFAULT_STATUT)
            .budget(DEFAULT_BUDGET);
        // Add required entity
        Club club;
        if (TestUtil.findAll(em, Club.class).isEmpty()) {
            club = ClubResourceIT.createEntity(em);
            em.persist(club);
            em.flush();
        } else {
            club = TestUtil.findAll(em, Club.class).get(0);
        }
        event.setClub(club);
        return event;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Event createUpdatedEntity(EntityManager em) {
        Event event = new Event()
            .title(UPDATED_TITLE)
            .eventDate(UPDATED_EVENT_DATE)
            .eventEnd(UPDATED_EVENT_END)
            .eventPlace(UPDATED_EVENT_PLACE)
            .content(UPDATED_CONTENT)
            .statut(UPDATED_STATUT)
            .budget(UPDATED_BUDGET);
        // Add required entity
        Club club;
        if (TestUtil.findAll(em, Club.class).isEmpty()) {
            club = ClubResourceIT.createUpdatedEntity(em);
            em.persist(club);
            em.flush();
        } else {
            club = TestUtil.findAll(em, Club.class).get(0);
        }
        event.setClub(club);
        return event;
    }

    @BeforeEach
    public void initTest() {
        event = createEntity(em);
    }

    @Test
    @Transactional
    void createEvent() throws Exception {
        int databaseSizeBeforeCreate = eventRepository.findAll().size();
        // Create the Event
        restEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(event)))
            .andExpect(status().isCreated());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeCreate + 1);
        Event testEvent = eventList.get(eventList.size() - 1);
        assertThat(testEvent.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testEvent.getEventDate()).isEqualTo(DEFAULT_EVENT_DATE);
        assertThat(testEvent.getEventEnd()).isEqualTo(DEFAULT_EVENT_END);
        assertThat(testEvent.getEventPlace()).isEqualTo(DEFAULT_EVENT_PLACE);
        assertThat(testEvent.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testEvent.getStatut()).isEqualTo(DEFAULT_STATUT);
        assertThat(testEvent.getBudget()).isEqualTo(DEFAULT_BUDGET);
    }

    @Test
    @Transactional
    void createEventWithExistingId() throws Exception {
        // Create the Event with an existing ID
        event.setId(1L);

        int databaseSizeBeforeCreate = eventRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(event)))
            .andExpect(status().isBadRequest());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventRepository.findAll().size();
        // set the field null
        event.setTitle(null);

        // Create the Event, which fails.

        restEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(event)))
            .andExpect(status().isBadRequest());

        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEventDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventRepository.findAll().size();
        // set the field null
        event.setEventDate(null);

        // Create the Event, which fails.

        restEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(event)))
            .andExpect(status().isBadRequest());

        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEventEndIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventRepository.findAll().size();
        // set the field null
        event.setEventEnd(null);

        // Create the Event, which fails.

        restEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(event)))
            .andExpect(status().isBadRequest());

        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEventPlaceIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventRepository.findAll().size();
        // set the field null
        event.setEventPlace(null);

        // Create the Event, which fails.

        restEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(event)))
            .andExpect(status().isBadRequest());

        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBudgetIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventRepository.findAll().size();
        // set the field null
        event.setBudget(null);

        // Create the Event, which fails.

        restEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(event)))
            .andExpect(status().isBadRequest());

        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEvents() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList
        restEventMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(event.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].eventDate").value(hasItem(DEFAULT_EVENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].eventEnd").value(hasItem(DEFAULT_EVENT_END.toString())))
            .andExpect(jsonPath("$.[*].eventPlace").value(hasItem(DEFAULT_EVENT_PLACE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())))
            .andExpect(jsonPath("$.[*].budget").value(hasItem(DEFAULT_BUDGET)));
    }

    @Test
    @Transactional
    void getEvent() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get the event
        restEventMockMvc
            .perform(get(ENTITY_API_URL_ID, event.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(event.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.eventDate").value(DEFAULT_EVENT_DATE.toString()))
            .andExpect(jsonPath("$.eventEnd").value(DEFAULT_EVENT_END.toString()))
            .andExpect(jsonPath("$.eventPlace").value(DEFAULT_EVENT_PLACE))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT.toString()))
            .andExpect(jsonPath("$.budget").value(DEFAULT_BUDGET));
    }

    @Test
    @Transactional
    void getEventsByIdFiltering() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        Long id = event.getId();

        defaultEventShouldBeFound("id.equals=" + id);
        defaultEventShouldNotBeFound("id.notEquals=" + id);

        defaultEventShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEventShouldNotBeFound("id.greaterThan=" + id);

        defaultEventShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEventShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEventsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where title equals to DEFAULT_TITLE
        defaultEventShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the eventList where title equals to UPDATED_TITLE
        defaultEventShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllEventsByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where title not equals to DEFAULT_TITLE
        defaultEventShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the eventList where title not equals to UPDATED_TITLE
        defaultEventShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllEventsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultEventShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the eventList where title equals to UPDATED_TITLE
        defaultEventShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllEventsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where title is not null
        defaultEventShouldBeFound("title.specified=true");

        // Get all the eventList where title is null
        defaultEventShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByTitleContainsSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where title contains DEFAULT_TITLE
        defaultEventShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the eventList where title contains UPDATED_TITLE
        defaultEventShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllEventsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where title does not contain DEFAULT_TITLE
        defaultEventShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the eventList where title does not contain UPDATED_TITLE
        defaultEventShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllEventsByEventDateIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where eventDate equals to DEFAULT_EVENT_DATE
        defaultEventShouldBeFound("eventDate.equals=" + DEFAULT_EVENT_DATE);

        // Get all the eventList where eventDate equals to UPDATED_EVENT_DATE
        defaultEventShouldNotBeFound("eventDate.equals=" + UPDATED_EVENT_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByEventDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where eventDate not equals to DEFAULT_EVENT_DATE
        defaultEventShouldNotBeFound("eventDate.notEquals=" + DEFAULT_EVENT_DATE);

        // Get all the eventList where eventDate not equals to UPDATED_EVENT_DATE
        defaultEventShouldBeFound("eventDate.notEquals=" + UPDATED_EVENT_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByEventDateIsInShouldWork() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where eventDate in DEFAULT_EVENT_DATE or UPDATED_EVENT_DATE
        defaultEventShouldBeFound("eventDate.in=" + DEFAULT_EVENT_DATE + "," + UPDATED_EVENT_DATE);

        // Get all the eventList where eventDate equals to UPDATED_EVENT_DATE
        defaultEventShouldNotBeFound("eventDate.in=" + UPDATED_EVENT_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByEventDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where eventDate is not null
        defaultEventShouldBeFound("eventDate.specified=true");

        // Get all the eventList where eventDate is null
        defaultEventShouldNotBeFound("eventDate.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByEventEndIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where eventEnd equals to DEFAULT_EVENT_END
        defaultEventShouldBeFound("eventEnd.equals=" + DEFAULT_EVENT_END);

        // Get all the eventList where eventEnd equals to UPDATED_EVENT_END
        defaultEventShouldNotBeFound("eventEnd.equals=" + UPDATED_EVENT_END);
    }

    @Test
    @Transactional
    void getAllEventsByEventEndIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where eventEnd not equals to DEFAULT_EVENT_END
        defaultEventShouldNotBeFound("eventEnd.notEquals=" + DEFAULT_EVENT_END);

        // Get all the eventList where eventEnd not equals to UPDATED_EVENT_END
        defaultEventShouldBeFound("eventEnd.notEquals=" + UPDATED_EVENT_END);
    }

    @Test
    @Transactional
    void getAllEventsByEventEndIsInShouldWork() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where eventEnd in DEFAULT_EVENT_END or UPDATED_EVENT_END
        defaultEventShouldBeFound("eventEnd.in=" + DEFAULT_EVENT_END + "," + UPDATED_EVENT_END);

        // Get all the eventList where eventEnd equals to UPDATED_EVENT_END
        defaultEventShouldNotBeFound("eventEnd.in=" + UPDATED_EVENT_END);
    }

    @Test
    @Transactional
    void getAllEventsByEventEndIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where eventEnd is not null
        defaultEventShouldBeFound("eventEnd.specified=true");

        // Get all the eventList where eventEnd is null
        defaultEventShouldNotBeFound("eventEnd.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByEventPlaceIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where eventPlace equals to DEFAULT_EVENT_PLACE
        defaultEventShouldBeFound("eventPlace.equals=" + DEFAULT_EVENT_PLACE);

        // Get all the eventList where eventPlace equals to UPDATED_EVENT_PLACE
        defaultEventShouldNotBeFound("eventPlace.equals=" + UPDATED_EVENT_PLACE);
    }

    @Test
    @Transactional
    void getAllEventsByEventPlaceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where eventPlace not equals to DEFAULT_EVENT_PLACE
        defaultEventShouldNotBeFound("eventPlace.notEquals=" + DEFAULT_EVENT_PLACE);

        // Get all the eventList where eventPlace not equals to UPDATED_EVENT_PLACE
        defaultEventShouldBeFound("eventPlace.notEquals=" + UPDATED_EVENT_PLACE);
    }

    @Test
    @Transactional
    void getAllEventsByEventPlaceIsInShouldWork() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where eventPlace in DEFAULT_EVENT_PLACE or UPDATED_EVENT_PLACE
        defaultEventShouldBeFound("eventPlace.in=" + DEFAULT_EVENT_PLACE + "," + UPDATED_EVENT_PLACE);

        // Get all the eventList where eventPlace equals to UPDATED_EVENT_PLACE
        defaultEventShouldNotBeFound("eventPlace.in=" + UPDATED_EVENT_PLACE);
    }

    @Test
    @Transactional
    void getAllEventsByEventPlaceIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where eventPlace is not null
        defaultEventShouldBeFound("eventPlace.specified=true");

        // Get all the eventList where eventPlace is null
        defaultEventShouldNotBeFound("eventPlace.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByEventPlaceContainsSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where eventPlace contains DEFAULT_EVENT_PLACE
        defaultEventShouldBeFound("eventPlace.contains=" + DEFAULT_EVENT_PLACE);

        // Get all the eventList where eventPlace contains UPDATED_EVENT_PLACE
        defaultEventShouldNotBeFound("eventPlace.contains=" + UPDATED_EVENT_PLACE);
    }

    @Test
    @Transactional
    void getAllEventsByEventPlaceNotContainsSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where eventPlace does not contain DEFAULT_EVENT_PLACE
        defaultEventShouldNotBeFound("eventPlace.doesNotContain=" + DEFAULT_EVENT_PLACE);

        // Get all the eventList where eventPlace does not contain UPDATED_EVENT_PLACE
        defaultEventShouldBeFound("eventPlace.doesNotContain=" + UPDATED_EVENT_PLACE);
    }

    @Test
    @Transactional
    void getAllEventsByStatutIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where statut equals to DEFAULT_STATUT
        defaultEventShouldBeFound("statut.equals=" + DEFAULT_STATUT);

        // Get all the eventList where statut equals to UPDATED_STATUT
        defaultEventShouldNotBeFound("statut.equals=" + UPDATED_STATUT);
    }

    @Test
    @Transactional
    void getAllEventsByStatutIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where statut not equals to DEFAULT_STATUT
        defaultEventShouldNotBeFound("statut.notEquals=" + DEFAULT_STATUT);

        // Get all the eventList where statut not equals to UPDATED_STATUT
        defaultEventShouldBeFound("statut.notEquals=" + UPDATED_STATUT);
    }

    @Test
    @Transactional
    void getAllEventsByStatutIsInShouldWork() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where statut in DEFAULT_STATUT or UPDATED_STATUT
        defaultEventShouldBeFound("statut.in=" + DEFAULT_STATUT + "," + UPDATED_STATUT);

        // Get all the eventList where statut equals to UPDATED_STATUT
        defaultEventShouldNotBeFound("statut.in=" + UPDATED_STATUT);
    }

    @Test
    @Transactional
    void getAllEventsByStatutIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where statut is not null
        defaultEventShouldBeFound("statut.specified=true");

        // Get all the eventList where statut is null
        defaultEventShouldNotBeFound("statut.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByBudgetIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where budget equals to DEFAULT_BUDGET
        defaultEventShouldBeFound("budget.equals=" + DEFAULT_BUDGET);

        // Get all the eventList where budget equals to UPDATED_BUDGET
        defaultEventShouldNotBeFound("budget.equals=" + UPDATED_BUDGET);
    }

    @Test
    @Transactional
    void getAllEventsByBudgetIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where budget not equals to DEFAULT_BUDGET
        defaultEventShouldNotBeFound("budget.notEquals=" + DEFAULT_BUDGET);

        // Get all the eventList where budget not equals to UPDATED_BUDGET
        defaultEventShouldBeFound("budget.notEquals=" + UPDATED_BUDGET);
    }

    @Test
    @Transactional
    void getAllEventsByBudgetIsInShouldWork() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where budget in DEFAULT_BUDGET or UPDATED_BUDGET
        defaultEventShouldBeFound("budget.in=" + DEFAULT_BUDGET + "," + UPDATED_BUDGET);

        // Get all the eventList where budget equals to UPDATED_BUDGET
        defaultEventShouldNotBeFound("budget.in=" + UPDATED_BUDGET);
    }

    @Test
    @Transactional
    void getAllEventsByBudgetIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where budget is not null
        defaultEventShouldBeFound("budget.specified=true");

        // Get all the eventList where budget is null
        defaultEventShouldNotBeFound("budget.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByBudgetContainsSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where budget contains DEFAULT_BUDGET
        defaultEventShouldBeFound("budget.contains=" + DEFAULT_BUDGET);

        // Get all the eventList where budget contains UPDATED_BUDGET
        defaultEventShouldNotBeFound("budget.contains=" + UPDATED_BUDGET);
    }

    @Test
    @Transactional
    void getAllEventsByBudgetNotContainsSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where budget does not contain DEFAULT_BUDGET
        defaultEventShouldNotBeFound("budget.doesNotContain=" + DEFAULT_BUDGET);

        // Get all the eventList where budget does not contain UPDATED_BUDGET
        defaultEventShouldBeFound("budget.doesNotContain=" + UPDATED_BUDGET);
    }

    @Test
    @Transactional
    void getAllEventsByClubIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);
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
        event.setClub(club);
        eventRepository.saveAndFlush(event);
        Long clubId = club.getId();

        // Get all the eventList where club equals to clubId
        defaultEventShouldBeFound("clubId.equals=" + clubId);

        // Get all the eventList where club equals to (clubId + 1)
        defaultEventShouldNotBeFound("clubId.equals=" + (clubId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEventShouldBeFound(String filter) throws Exception {
        restEventMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(event.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].eventDate").value(hasItem(DEFAULT_EVENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].eventEnd").value(hasItem(DEFAULT_EVENT_END.toString())))
            .andExpect(jsonPath("$.[*].eventPlace").value(hasItem(DEFAULT_EVENT_PLACE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())))
            .andExpect(jsonPath("$.[*].budget").value(hasItem(DEFAULT_BUDGET)));

        // Check, that the count call also returns 1
        restEventMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEventShouldNotBeFound(String filter) throws Exception {
        restEventMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEventMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEvent() throws Exception {
        // Get the event
        restEventMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEvent() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        int databaseSizeBeforeUpdate = eventRepository.findAll().size();

        // Update the event
        Event updatedEvent = eventRepository.findById(event.getId()).get();
        // Disconnect from session so that the updates on updatedEvent are not directly saved in db
        em.detach(updatedEvent);
        updatedEvent
            .title(UPDATED_TITLE)
            .eventDate(UPDATED_EVENT_DATE)
            .eventEnd(UPDATED_EVENT_END)
            .eventPlace(UPDATED_EVENT_PLACE)
            .content(UPDATED_CONTENT)
            .statut(UPDATED_STATUT)
            .budget(UPDATED_BUDGET);

        restEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEvent.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEvent))
            )
            .andExpect(status().isOk());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
        Event testEvent = eventList.get(eventList.size() - 1);
        assertThat(testEvent.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testEvent.getEventDate()).isEqualTo(UPDATED_EVENT_DATE);
        assertThat(testEvent.getEventEnd()).isEqualTo(UPDATED_EVENT_END);
        assertThat(testEvent.getEventPlace()).isEqualTo(UPDATED_EVENT_PLACE);
        assertThat(testEvent.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testEvent.getStatut()).isEqualTo(UPDATED_STATUT);
        assertThat(testEvent.getBudget()).isEqualTo(UPDATED_BUDGET);
    }

    @Test
    @Transactional
    void putNonExistingEvent() throws Exception {
        int databaseSizeBeforeUpdate = eventRepository.findAll().size();
        event.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, event.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(event))
            )
            .andExpect(status().isBadRequest());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEvent() throws Exception {
        int databaseSizeBeforeUpdate = eventRepository.findAll().size();
        event.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(event))
            )
            .andExpect(status().isBadRequest());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEvent() throws Exception {
        int databaseSizeBeforeUpdate = eventRepository.findAll().size();
        event.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(event)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEventWithPatch() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        int databaseSizeBeforeUpdate = eventRepository.findAll().size();

        // Update the event using partial update
        Event partialUpdatedEvent = new Event();
        partialUpdatedEvent.setId(event.getId());

        partialUpdatedEvent.eventDate(UPDATED_EVENT_DATE);

        restEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEvent.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEvent))
            )
            .andExpect(status().isOk());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
        Event testEvent = eventList.get(eventList.size() - 1);
        assertThat(testEvent.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testEvent.getEventDate()).isEqualTo(UPDATED_EVENT_DATE);
        assertThat(testEvent.getEventEnd()).isEqualTo(DEFAULT_EVENT_END);
        assertThat(testEvent.getEventPlace()).isEqualTo(DEFAULT_EVENT_PLACE);
        assertThat(testEvent.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testEvent.getStatut()).isEqualTo(DEFAULT_STATUT);
        assertThat(testEvent.getBudget()).isEqualTo(DEFAULT_BUDGET);
    }

    @Test
    @Transactional
    void fullUpdateEventWithPatch() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        int databaseSizeBeforeUpdate = eventRepository.findAll().size();

        // Update the event using partial update
        Event partialUpdatedEvent = new Event();
        partialUpdatedEvent.setId(event.getId());

        partialUpdatedEvent
            .title(UPDATED_TITLE)
            .eventDate(UPDATED_EVENT_DATE)
            .eventEnd(UPDATED_EVENT_END)
            .eventPlace(UPDATED_EVENT_PLACE)
            .content(UPDATED_CONTENT)
            .statut(UPDATED_STATUT)
            .budget(UPDATED_BUDGET);

        restEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEvent.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEvent))
            )
            .andExpect(status().isOk());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
        Event testEvent = eventList.get(eventList.size() - 1);
        assertThat(testEvent.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testEvent.getEventDate()).isEqualTo(UPDATED_EVENT_DATE);
        assertThat(testEvent.getEventEnd()).isEqualTo(UPDATED_EVENT_END);
        assertThat(testEvent.getEventPlace()).isEqualTo(UPDATED_EVENT_PLACE);
        assertThat(testEvent.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testEvent.getStatut()).isEqualTo(UPDATED_STATUT);
        assertThat(testEvent.getBudget()).isEqualTo(UPDATED_BUDGET);
    }

    @Test
    @Transactional
    void patchNonExistingEvent() throws Exception {
        int databaseSizeBeforeUpdate = eventRepository.findAll().size();
        event.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, event.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(event))
            )
            .andExpect(status().isBadRequest());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEvent() throws Exception {
        int databaseSizeBeforeUpdate = eventRepository.findAll().size();
        event.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(event))
            )
            .andExpect(status().isBadRequest());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEvent() throws Exception {
        int databaseSizeBeforeUpdate = eventRepository.findAll().size();
        event.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(event)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEvent() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        int databaseSizeBeforeDelete = eventRepository.findAll().size();

        // Delete the event
        restEventMockMvc
            .perform(delete(ENTITY_API_URL_ID, event.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
