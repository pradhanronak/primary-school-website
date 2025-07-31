package com.primaryschool.website.repository;

import com.primaryschool.website.entity.AdmissionApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdmissionApplicationRepository extends JpaRepository<AdmissionApplication, Long> {

    List<AdmissionApplication> findByStatus(AdmissionApplication.ApplicationStatus status);
    List<AdmissionApplication> findByGradeApplyingFor(String grade);
    List<AdmissionApplication> findByAcademicYear(String academicYear);
    Optional<AdmissionApplication> findByParentEmailAndAcademicYear(String email, String academicYear);

    @Query("SELECT a FROM AdmissionApplication a WHERE a.parentEmail = :email ORDER BY a.applicationDate DESC")
    List<AdmissionApplication> findByParentEmailOrderByApplicationDateDesc(@Param("email") String email);

    @Query("SELECT COUNT(a) FROM AdmissionApplication a WHERE a.status = :status")
    long countByStatus(@Param("status") AdmissionApplication.ApplicationStatus status);
}
