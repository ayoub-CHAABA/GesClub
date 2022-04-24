package com.uir.club.service;

import com.uir.club.domain.Club;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Club}.
 */
public interface ClubService {
    /**
     * Save a club.
     *
     * @param club the entity to save.
     * @return the persisted entity.
     */
    Club save(Club club);

    /**
     * Partially updates a club.
     *
     * @param club the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Club> partialUpdate(Club club);

    /**
     * Get all the clubs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Club> findAll(Pageable pageable);

    /**
     * Get the "id" club.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Club> findOne(Long id);

    /**
     * Delete the "id" club.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
