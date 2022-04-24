package com.uir.club.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.uir.club.IntegrationTest;
import com.uir.club.domain.Club;
import com.uir.club.repository.ClubRepository;
import com.uir.club.service.criteria.ClubCriteria;
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
 * Integration tests for the {@link ClubResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ClubResourceIT {

    private static final String DEFAULT_CLUB_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CLUB_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final byte[] DEFAULT_LOGO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_LOGO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_LOGO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_LOGO_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/clubs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClubMockMvc;

    private Club club;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Club createEntity(EntityManager em) {
        Club club = new Club()
            .clubName(DEFAULT_CLUB_NAME)
            .creationDate(DEFAULT_CREATION_DATE)
            .logo(DEFAULT_LOGO)
            .logoContentType(DEFAULT_LOGO_CONTENT_TYPE);
        return club;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Club createUpdatedEntity(EntityManager em) {
        Club club = new Club()
            .clubName(UPDATED_CLUB_NAME)
            .creationDate(UPDATED_CREATION_DATE)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE);
        return club;
    }

    @BeforeEach
    public void initTest() {
        club = createEntity(em);
    }

    @Test
    @Transactional
    void createClub() throws Exception {
        int databaseSizeBeforeCreate = clubRepository.findAll().size();
        // Create the Club
        restClubMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(club)))
            .andExpect(status().isCreated());

        // Validate the Club in the database
        List<Club> clubList = clubRepository.findAll();
        assertThat(clubList).hasSize(databaseSizeBeforeCreate + 1);
        Club testClub = clubList.get(clubList.size() - 1);
        assertThat(testClub.getClubName()).isEqualTo(DEFAULT_CLUB_NAME);
        assertThat(testClub.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testClub.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testClub.getLogoContentType()).isEqualTo(DEFAULT_LOGO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createClubWithExistingId() throws Exception {
        // Create the Club with an existing ID
        club.setId(1L);

        int databaseSizeBeforeCreate = clubRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClubMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(club)))
            .andExpect(status().isBadRequest());

        // Validate the Club in the database
        List<Club> clubList = clubRepository.findAll();
        assertThat(clubList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkClubNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = clubRepository.findAll().size();
        // set the field null
        club.setClubName(null);

        // Create the Club, which fails.

        restClubMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(club)))
            .andExpect(status().isBadRequest());

        List<Club> clubList = clubRepository.findAll();
        assertThat(clubList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = clubRepository.findAll().size();
        // set the field null
        club.setCreationDate(null);

        // Create the Club, which fails.

        restClubMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(club)))
            .andExpect(status().isBadRequest());

        List<Club> clubList = clubRepository.findAll();
        assertThat(clubList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllClubs() throws Exception {
        // Initialize the database
        clubRepository.saveAndFlush(club);

        // Get all the clubList
        restClubMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(club.getId().intValue())))
            .andExpect(jsonPath("$.[*].clubName").value(hasItem(DEFAULT_CLUB_NAME)))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].logoContentType").value(hasItem(DEFAULT_LOGO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(Base64Utils.encodeToString(DEFAULT_LOGO))));
    }

    @Test
    @Transactional
    void getClub() throws Exception {
        // Initialize the database
        clubRepository.saveAndFlush(club);

        // Get the club
        restClubMockMvc
            .perform(get(ENTITY_API_URL_ID, club.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(club.getId().intValue()))
            .andExpect(jsonPath("$.clubName").value(DEFAULT_CLUB_NAME))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.logoContentType").value(DEFAULT_LOGO_CONTENT_TYPE))
            .andExpect(jsonPath("$.logo").value(Base64Utils.encodeToString(DEFAULT_LOGO)));
    }

    @Test
    @Transactional
    void getClubsByIdFiltering() throws Exception {
        // Initialize the database
        clubRepository.saveAndFlush(club);

        Long id = club.getId();

        defaultClubShouldBeFound("id.equals=" + id);
        defaultClubShouldNotBeFound("id.notEquals=" + id);

        defaultClubShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultClubShouldNotBeFound("id.greaterThan=" + id);

        defaultClubShouldBeFound("id.lessThanOrEqual=" + id);
        defaultClubShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllClubsByClubNameIsEqualToSomething() throws Exception {
        // Initialize the database
        clubRepository.saveAndFlush(club);

        // Get all the clubList where clubName equals to DEFAULT_CLUB_NAME
        defaultClubShouldBeFound("clubName.equals=" + DEFAULT_CLUB_NAME);

        // Get all the clubList where clubName equals to UPDATED_CLUB_NAME
        defaultClubShouldNotBeFound("clubName.equals=" + UPDATED_CLUB_NAME);
    }

    @Test
    @Transactional
    void getAllClubsByClubNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        clubRepository.saveAndFlush(club);

        // Get all the clubList where clubName not equals to DEFAULT_CLUB_NAME
        defaultClubShouldNotBeFound("clubName.notEquals=" + DEFAULT_CLUB_NAME);

        // Get all the clubList where clubName not equals to UPDATED_CLUB_NAME
        defaultClubShouldBeFound("clubName.notEquals=" + UPDATED_CLUB_NAME);
    }

    @Test
    @Transactional
    void getAllClubsByClubNameIsInShouldWork() throws Exception {
        // Initialize the database
        clubRepository.saveAndFlush(club);

        // Get all the clubList where clubName in DEFAULT_CLUB_NAME or UPDATED_CLUB_NAME
        defaultClubShouldBeFound("clubName.in=" + DEFAULT_CLUB_NAME + "," + UPDATED_CLUB_NAME);

        // Get all the clubList where clubName equals to UPDATED_CLUB_NAME
        defaultClubShouldNotBeFound("clubName.in=" + UPDATED_CLUB_NAME);
    }

    @Test
    @Transactional
    void getAllClubsByClubNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        clubRepository.saveAndFlush(club);

        // Get all the clubList where clubName is not null
        defaultClubShouldBeFound("clubName.specified=true");

        // Get all the clubList where clubName is null
        defaultClubShouldNotBeFound("clubName.specified=false");
    }

    @Test
    @Transactional
    void getAllClubsByClubNameContainsSomething() throws Exception {
        // Initialize the database
        clubRepository.saveAndFlush(club);

        // Get all the clubList where clubName contains DEFAULT_CLUB_NAME
        defaultClubShouldBeFound("clubName.contains=" + DEFAULT_CLUB_NAME);

        // Get all the clubList where clubName contains UPDATED_CLUB_NAME
        defaultClubShouldNotBeFound("clubName.contains=" + UPDATED_CLUB_NAME);
    }

    @Test
    @Transactional
    void getAllClubsByClubNameNotContainsSomething() throws Exception {
        // Initialize the database
        clubRepository.saveAndFlush(club);

        // Get all the clubList where clubName does not contain DEFAULT_CLUB_NAME
        defaultClubShouldNotBeFound("clubName.doesNotContain=" + DEFAULT_CLUB_NAME);

        // Get all the clubList where clubName does not contain UPDATED_CLUB_NAME
        defaultClubShouldBeFound("clubName.doesNotContain=" + UPDATED_CLUB_NAME);
    }

    @Test
    @Transactional
    void getAllClubsByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        clubRepository.saveAndFlush(club);

        // Get all the clubList where creationDate equals to DEFAULT_CREATION_DATE
        defaultClubShouldBeFound("creationDate.equals=" + DEFAULT_CREATION_DATE);

        // Get all the clubList where creationDate equals to UPDATED_CREATION_DATE
        defaultClubShouldNotBeFound("creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllClubsByCreationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        clubRepository.saveAndFlush(club);

        // Get all the clubList where creationDate not equals to DEFAULT_CREATION_DATE
        defaultClubShouldNotBeFound("creationDate.notEquals=" + DEFAULT_CREATION_DATE);

        // Get all the clubList where creationDate not equals to UPDATED_CREATION_DATE
        defaultClubShouldBeFound("creationDate.notEquals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllClubsByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        clubRepository.saveAndFlush(club);

        // Get all the clubList where creationDate in DEFAULT_CREATION_DATE or UPDATED_CREATION_DATE
        defaultClubShouldBeFound("creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE);

        // Get all the clubList where creationDate equals to UPDATED_CREATION_DATE
        defaultClubShouldNotBeFound("creationDate.in=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllClubsByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        clubRepository.saveAndFlush(club);

        // Get all the clubList where creationDate is not null
        defaultClubShouldBeFound("creationDate.specified=true");

        // Get all the clubList where creationDate is null
        defaultClubShouldNotBeFound("creationDate.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultClubShouldBeFound(String filter) throws Exception {
        restClubMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(club.getId().intValue())))
            .andExpect(jsonPath("$.[*].clubName").value(hasItem(DEFAULT_CLUB_NAME)))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].logoContentType").value(hasItem(DEFAULT_LOGO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(Base64Utils.encodeToString(DEFAULT_LOGO))));

        // Check, that the count call also returns 1
        restClubMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultClubShouldNotBeFound(String filter) throws Exception {
        restClubMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restClubMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingClub() throws Exception {
        // Get the club
        restClubMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewClub() throws Exception {
        // Initialize the database
        clubRepository.saveAndFlush(club);

        int databaseSizeBeforeUpdate = clubRepository.findAll().size();

        // Update the club
        Club updatedClub = clubRepository.findById(club.getId()).get();
        // Disconnect from session so that the updates on updatedClub are not directly saved in db
        em.detach(updatedClub);
        updatedClub
            .clubName(UPDATED_CLUB_NAME)
            .creationDate(UPDATED_CREATION_DATE)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE);

        restClubMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedClub.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedClub))
            )
            .andExpect(status().isOk());

        // Validate the Club in the database
        List<Club> clubList = clubRepository.findAll();
        assertThat(clubList).hasSize(databaseSizeBeforeUpdate);
        Club testClub = clubList.get(clubList.size() - 1);
        assertThat(testClub.getClubName()).isEqualTo(UPDATED_CLUB_NAME);
        assertThat(testClub.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testClub.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testClub.getLogoContentType()).isEqualTo(UPDATED_LOGO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingClub() throws Exception {
        int databaseSizeBeforeUpdate = clubRepository.findAll().size();
        club.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClubMockMvc
            .perform(
                put(ENTITY_API_URL_ID, club.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(club))
            )
            .andExpect(status().isBadRequest());

        // Validate the Club in the database
        List<Club> clubList = clubRepository.findAll();
        assertThat(clubList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchClub() throws Exception {
        int databaseSizeBeforeUpdate = clubRepository.findAll().size();
        club.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClubMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(club))
            )
            .andExpect(status().isBadRequest());

        // Validate the Club in the database
        List<Club> clubList = clubRepository.findAll();
        assertThat(clubList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamClub() throws Exception {
        int databaseSizeBeforeUpdate = clubRepository.findAll().size();
        club.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClubMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(club)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Club in the database
        List<Club> clubList = clubRepository.findAll();
        assertThat(clubList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClubWithPatch() throws Exception {
        // Initialize the database
        clubRepository.saveAndFlush(club);

        int databaseSizeBeforeUpdate = clubRepository.findAll().size();

        // Update the club using partial update
        Club partialUpdatedClub = new Club();
        partialUpdatedClub.setId(club.getId());

        partialUpdatedClub.creationDate(UPDATED_CREATION_DATE);

        restClubMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClub.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClub))
            )
            .andExpect(status().isOk());

        // Validate the Club in the database
        List<Club> clubList = clubRepository.findAll();
        assertThat(clubList).hasSize(databaseSizeBeforeUpdate);
        Club testClub = clubList.get(clubList.size() - 1);
        assertThat(testClub.getClubName()).isEqualTo(DEFAULT_CLUB_NAME);
        assertThat(testClub.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testClub.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testClub.getLogoContentType()).isEqualTo(DEFAULT_LOGO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateClubWithPatch() throws Exception {
        // Initialize the database
        clubRepository.saveAndFlush(club);

        int databaseSizeBeforeUpdate = clubRepository.findAll().size();

        // Update the club using partial update
        Club partialUpdatedClub = new Club();
        partialUpdatedClub.setId(club.getId());

        partialUpdatedClub
            .clubName(UPDATED_CLUB_NAME)
            .creationDate(UPDATED_CREATION_DATE)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE);

        restClubMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClub.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClub))
            )
            .andExpect(status().isOk());

        // Validate the Club in the database
        List<Club> clubList = clubRepository.findAll();
        assertThat(clubList).hasSize(databaseSizeBeforeUpdate);
        Club testClub = clubList.get(clubList.size() - 1);
        assertThat(testClub.getClubName()).isEqualTo(UPDATED_CLUB_NAME);
        assertThat(testClub.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testClub.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testClub.getLogoContentType()).isEqualTo(UPDATED_LOGO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingClub() throws Exception {
        int databaseSizeBeforeUpdate = clubRepository.findAll().size();
        club.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClubMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, club.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(club))
            )
            .andExpect(status().isBadRequest());

        // Validate the Club in the database
        List<Club> clubList = clubRepository.findAll();
        assertThat(clubList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchClub() throws Exception {
        int databaseSizeBeforeUpdate = clubRepository.findAll().size();
        club.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClubMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(club))
            )
            .andExpect(status().isBadRequest());

        // Validate the Club in the database
        List<Club> clubList = clubRepository.findAll();
        assertThat(clubList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamClub() throws Exception {
        int databaseSizeBeforeUpdate = clubRepository.findAll().size();
        club.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClubMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(club)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Club in the database
        List<Club> clubList = clubRepository.findAll();
        assertThat(clubList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteClub() throws Exception {
        // Initialize the database
        clubRepository.saveAndFlush(club);

        int databaseSizeBeforeDelete = clubRepository.findAll().size();

        // Delete the club
        restClubMockMvc
            .perform(delete(ENTITY_API_URL_ID, club.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Club> clubList = clubRepository.findAll();
        assertThat(clubList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
