package com.uir.club.service;

import com.uir.club.domain.Meeting;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Meeting}.
 */
public interface MeetingService {
    /**
     * Save a meeting.
     *
     * @param meeting the entity to save.
     * @return the persisted entity.
     */
    Meeting save(Meeting meeting);

    /**
     * Partially updates a meeting.
     *
     * @param meeting the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Meeting> partialUpdate(Meeting meeting);

    /**
     * Get all the meetings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Meeting> findAll(Pageable pageable);

    /**
     * Get the "id" meeting.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Meeting> findOne(Long id);

    /**
     * Delete the "id" meeting.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
