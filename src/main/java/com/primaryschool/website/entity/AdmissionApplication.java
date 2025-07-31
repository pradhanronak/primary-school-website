package com.primaryschool.website.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "admission_applications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdmissionApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Student Information
    @NotBlank(message = "Student name is required")
    @Column(name = "student_name", nullable = false)
    private String studentName;

    @NotNull(message = "Date of birth is required")
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Gender is required")
    @Column(name = "gender")
    private String gender;

    @NotBlank(message = "Grade is required")
    @Column(name = "grade_applying_for")
    private String gradeApplyingFor;

    // Parent/Guardian Information
    @NotBlank(message = "Parent name is required")
    @Column(name = "parent_name", nullable = false)
    private String parentName;

    @NotBlank(message = "Email is required")
    @Email(message = "Valid email is required")
    @Column(name = "parent_email", nullable = false)
    private String parentEmail;

    @NotBlank(message = "Phone number is required")
    @Column(name = "parent_phone", nullable = false)
    private String parentPhone;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    // Academic Information
    @Column(name = "previous_school")
    private String previousSchool;

    @Column(name = "academic_year")
    private String academicYear;

    // Application Status
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ApplicationStatus status = ApplicationStatus.PENDING;

    // Payment Information
    @OneToOne(mappedBy = "admissionApplication", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Payment payment;

    // Additional Information
    @Column(name = "special_requirements", columnDefinition = "TEXT")
    private String specialRequirements;

    @Column(name = "how_did_you_hear")
    private String howDidYouHear;

    // Timestamps
    @Column(name = "application_date")
    private LocalDateTime applicationDate;

    @Column(name = "reviewed_date")
    private LocalDateTime reviewedDate;

    @Column(name = "reviewed_by")
    private String reviewedBy;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @PrePersist
    protected void onCreate() {
        applicationDate = LocalDateTime.now();
    }

    public enum ApplicationStatus {
        PENDING,
        PAYMENT_PENDING,
        UNDER_REVIEW,
        APPROVED,
        REJECTED,
        WAITLISTED
    }
}
