package com.primaryschool.website.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdmissionApplicationDTO {

    // Student Information
    @NotBlank(message = "Student name is required")
    @Size(min = 2, max = 100, message = "Student name must be between 2 and 100 characters")
    private String studentName;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Gender is required")
    @Pattern(regexp = "Male|Female|Other", message = "Gender must be Male, Female, or Other")
    private String gender;

    @NotBlank(message = "Grade applying for is required")
    private String gradeApplyingFor;

    // Parent/Guardian Information
    @NotBlank(message = "Parent name is required")
    @Size(min = 2, max = 100, message = "Parent name must be between 2 and 100 characters")
    private String parentName;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String parentEmail;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Please provide a valid phone number")
    private String parentPhone;

    @NotBlank(message = "Address is required")
    @Size(max = 1000, message = "Address cannot exceed 1000 characters")
    private String address;

    // Academic Information
    @Size(max = 200, message = "Previous school name cannot exceed 200 characters")
    private String previousSchool;

    @NotBlank(message = "Academic year is required")
    private String academicYear;

    // Additional Information
    @Size(max = 1000, message = "Special requirements cannot exceed 1000 characters")
    private String specialRequirements;

    private String howDidYouHear;

    // Terms and Conditions
    @AssertTrue(message = "You must accept the terms and conditions")
    private boolean acceptTerms;
}
