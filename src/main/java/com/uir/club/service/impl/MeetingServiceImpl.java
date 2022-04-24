package com.uir.club.service.impl;

import com.uir.club.domain.Meeting;
import com.uir.club.repository.MeetingRepository;
import com.uir.club.service.MeetingService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Meeting}.
 */
@Service
@Transactional
public class MeetingServiceImpl implements MeetingService {

    private final Logger log = LoggerFactory.getLogger(MeetingServiceImpl.class);

    private final MeetingRepository meetingRepository;

    public MeetingServiceImpl(MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;
    }

    @Override
    public Meeting save(Meeting meeting) {
        log.debug("Request to save Meeting : {}", meeting);
        return meetingRepository.save(meeting);
    }

    @Override
    public Optional<Meeting> partialUpdate(Meeting meeting) {
        log.debug("Request to partially update Meeting : {}", meeting);

        return meetingRepository
            .findById(meeting.getId())
            .map(existingMeeting -> {
                if (meeting.getTitle() != null) {
                    existingMeeting.setTitle(meeting.getTitle());
                }
                if (meeting.getMeetingDate() != null) {
                    existingMeeting.setMeetingDate(meeting.getMeetingDate());
                }
                if (meeting.getMeetingPlace() != null) {
                    existingMeeting.setMeetingPlace(meeting.getMeetingPlace());
                }
                if (meeting.getContent() != null) {
                    existingMeeting.setContent(meeting.getContent());
                }
                if (meeting.getStatut() != null) {
                    existingMeeting.setStatut(meeting.getStatut());
                }

                return existingMeeting;
            })
            .map(meetingRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Meeting> findAll(Pageable pageable) {
        log.debug("Request to get all Meetings");
        return meetingRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Meeting> findOne(Long id) {
        log.debug("Request to get Meeting : {}", id);
        return meetingRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Meeting : {}", id);
        meetingRepository.deleteById(id);
    }
}
