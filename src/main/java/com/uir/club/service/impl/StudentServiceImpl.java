package com.uir.club.service.impl;

import com.uir.club.domain.Student;
import com.uir.club.repository.StudentRepository;
import com.uir.club.service.StudentService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Student}.
 */
@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    private final Logger log = LoggerFactory.getLogger(StudentServiceImpl.class);

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student save(Student student) {
        log.debug("Request to save Student : {}", student);
        return studentRepository.save(student);
    }

    @Override
    public Optional<Student> partialUpdate(Student student) {
        log.debug("Request to partially update Student : {}", student);

        return studentRepository
            .findById(student.getId())
            .map(existingStudent -> {
                if (student.getFirstname() != null) {
                    existingStudent.setFirstname(student.getFirstname());
                }
                if (student.getLastname() != null) {
                    existingStudent.setLastname(student.getLastname());
                }
                if (student.getNationality() != null) {
                    existingStudent.setNationality(student.getNationality());
                }
                if (student.getCity() != null) {
                    existingStudent.setCity(student.getCity());
                }
                if (student.getFiliere() != null) {
                    existingStudent.setFiliere(student.getFiliere());
                }
                if (student.getLevel() != null) {
                    existingStudent.setLevel(student.getLevel());
                }
                if (student.getResidency() != null) {
                    existingStudent.setResidency(student.getResidency());
                }
                if (student.getTel() != null) {
                    existingStudent.setTel(student.getTel());
                }
                if (student.getMail() != null) {
                    existingStudent.setMail(student.getMail());
                }
                if (student.getPicture() != null) {
                    existingStudent.setPicture(student.getPicture());
                }
                if (student.getPictureContentType() != null) {
                    existingStudent.setPictureContentType(student.getPictureContentType());
                }
                if (student.getRole() != null) {
                    existingStudent.setRole(student.getRole());
                }
                if (student.getAdhesion() != null) {
                    existingStudent.setAdhesion(student.getAdhesion());
                }

                return existingStudent;
            })
            .map(studentRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Student> findAll(Pageable pageable) {
        log.debug("Request to get all Students");
        return studentRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Student> findOne(Long id) {
        log.debug("Request to get Student : {}", id);
        return studentRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Student : {}", id);
        studentRepository.deleteById(id);
    }
}
