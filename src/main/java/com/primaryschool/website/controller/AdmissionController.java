package com.primaryschool.website.controller;

import com.primaryschool.website.dto.AdmissionApplicationDTO;
import com.primaryschool.website.dto.RazorpayOrderDTO;
import com.primaryschool.website.entity.AdmissionApplication;
import com.primaryschool.website.service.AdmissionService;
import com.primaryschool.website.service.PaymentService;
import com.primaryschool.website.service.RazorpayService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admission")
@RequiredArgsConstructor
@Slf4j
public class AdmissionController {

    private final AdmissionService admissionService;
    private final PaymentService paymentService;
    private final RazorpayService razorpayService;

    @PostMapping("/apply")
    public ResponseEntity<?> submitApplication(@Valid @RequestBody AdmissionApplicationDTO applicationDTO) {
        try {
            log.info("Received admission application for student: {}", applicationDTO.getStudentName());

            // Create admission application
            AdmissionApplication application = admissionService.createApplication(applicationDTO);

            // Create Razorpay order
            RazorpayOrderDTO orderDTO = razorpayService.createOrder(application);

            // Create payment record
            paymentService.createPayment(application.getId(), orderDTO.getOrderId(), orderDTO.getAmount());

            // Return order details for frontend payment processing
            return ResponseEntity.ok(Map.of(
                "applicationId", application.getId(),
                "razorpayOrder", orderDTO,
                "message", "Application created successfully. Please complete the payment."
            ));

        } catch (IllegalStateException e) {
            log.warn("Duplicate application attempt: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("Error creating admission application", e);
            return ResponseEntity.internalServerError().body(Map.of("error", "An error occurred while processing your application"));
        }
    }

    @GetMapping("/application/{id}")
    public ResponseEntity<?> getApplication(@PathVariable Long id) {
        return admissionService.findById(id)
                .map(application -> ResponseEntity.ok(Map.of(
                    "application", application,
                    "payment", paymentService.findByAdmissionApplicationId(id).orElse(null)
                )))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/grades")
    public ResponseEntity<?> getAvailableGrades() {
        return ResponseEntity.ok(Map.of(
            "grades", new String[]{
                "Pre-K", "Kindergarten", "Grade 1", "Grade 2", "Grade 3", 
                "Grade 4", "Grade 5", "Grade 6", "Grade 7", "Grade 8"
            }
        ));
    }

    @GetMapping("/academic-years")
    public ResponseEntity<?> getAcademicYears() {
        return ResponseEntity.ok(Map.of(
            "academicYears", new String[]{
                "2024-2025", "2025-2026"
            }
        ));
    }
}
