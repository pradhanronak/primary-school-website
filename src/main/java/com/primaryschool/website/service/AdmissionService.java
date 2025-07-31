package com.primaryschool.website.service;

import com.primaryschool.website.dto.AdmissionApplicationDTO;
import com.primaryschool.website.entity.AdmissionApplication;
import com.primaryschool.website.repository.AdmissionApplicationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AdmissionService {

    private final AdmissionApplicationRepository admissionRepository;

    public AdmissionApplication createApplication(AdmissionApplicationDTO dto) {
        log.info("Creating admission application for student: {}", dto.getStudentName());

        // Check if application already exists for this academic year
        Optional<AdmissionApplication> existingApplication = 
            admissionRepository.findByParentEmailAndAcademicYear(dto.getParentEmail(), dto.getAcademicYear());

        if (existingApplication.isPresent()) {
            throw new IllegalStateException("Application already exists for this academic year");
        }

        AdmissionApplication application = convertToEntity(dto);
        application.setStatus(AdmissionApplication.ApplicationStatus.PAYMENT_PENDING);

        AdmissionApplication savedApplication = admissionRepository.save(application);
        log.info("Admission application created with ID: {}", savedApplication.getId());

        return savedApplication;
    }

    public Optional<AdmissionApplication> findById(Long id) {
        return admissionRepository.findById(id);
    }

    public List<AdmissionApplication> findByStatus(AdmissionApplication.ApplicationStatus status) {
        return admissionRepository.findByStatus(status);
    }

    public Page<AdmissionApplication> findAll(Pageable pageable) {
        return admissionRepository.findAll(pageable);
    }

    public AdmissionApplication updateStatus(Long id, AdmissionApplication.ApplicationStatus status, String reviewedBy) {
        AdmissionApplication application = admissionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Application not found"));

        application.setStatus(status);
        application.setReviewedBy(reviewedBy);
        application.setReviewedDate(java.time.LocalDateTime.now());

        return admissionRepository.save(application);
    }

    public long getApplicationCountByStatus(AdmissionApplication.ApplicationStatus status) {
        return admissionRepository.countByStatus(status);
    }

    private AdmissionApplication convertToEntity(AdmissionApplicationDTO dto) {
        AdmissionApplication application = new AdmissionApplication();

        // Student Information
        application.setStudentName(dto.getStudentName());
        application.setDateOfBirth(dto.getDateOfBirth());
        application.setGender(dto.getGender());
        application.setGradeApplyingFor(dto.getGradeApplyingFor());

        // Parent Information
        application.setParentName(dto.getParentName());
        application.setParentEmail(dto.getParentEmail());
        application.setParentPhone(dto.getParentPhone());
        application.setAddress(dto.getAddress());

        // Academic Information
        application.setPreviousSchool(dto.getPreviousSchool());
        application.setAcademicYear(dto.getAcademicYear());

        // Additional Information
        application.setSpecialRequirements(dto.getSpecialRequirements());
        application.setHowDidYouHear(dto.getHowDidYouHear());

        return application;
    }
}
