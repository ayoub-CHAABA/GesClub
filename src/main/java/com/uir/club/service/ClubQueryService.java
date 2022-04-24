package com.uir.club.service;

import com.uir.club.domain.*; // for static metamodels
import com.uir.club.domain.Club;
import com.uir.club.repository.ClubRepository;
import com.uir.club.service.criteria.ClubCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Club} entities in the database.
 * The main input is a {@link ClubCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Club} or a {@link Page} of {@link Club} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ClubQueryService extends QueryService<Club> {

    private final Logger log = LoggerFactory.getLogger(ClubQueryService.class);

    private final ClubRepository clubRepository;

    public ClubQueryService(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;
    }

    /**
     * Return a {@link List} of {@link Club} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Club> findByCriteria(ClubCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Club> specification = createSpecification(criteria);
        return clubRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Club} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Club> findByCriteria(ClubCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Club> specification = createSpecification(criteria);
        return clubRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ClubCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Club> specification = createSpecification(criteria);
        return clubRepository.count(specification);
    }

    /**
     * Function to convert {@link ClubCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Club> createSpecification(ClubCriteria criteria) {
        Specification<Club> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Club_.id));
            }
            if (criteria.getClubName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getClubName(), Club_.clubName));
            }
            if (criteria.getCreationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreationDate(), Club_.creationDate));
            }
        }
        return specification;
    }
}
