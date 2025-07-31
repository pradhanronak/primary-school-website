package com.primaryschool.website.service;

import com.primaryschool.website.dto.PaymentDTO;
import com.primaryschool.website.entity.AdmissionApplication;
import com.primaryschool.website.entity.Payment;
import com.primaryschool.website.repository.AdmissionApplicationRepository;
import com.primaryschool.website.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final AdmissionApplicationRepository admissionRepository;

    public Payment createPayment(Long admissionApplicationId, String razorpayOrderId, BigDecimal amount) {
        log.info("Creating payment for admission application ID: {}", admissionApplicationId);

        AdmissionApplication application = admissionRepository.findById(admissionApplicationId)
                .orElseThrow(() -> new IllegalArgumentException("Admission application not found"));

        Payment payment = new Payment();
        payment.setAdmissionApplication(application);
        payment.setRazorpayOrderId(razorpayOrderId);
        payment.setAmount(amount);
        payment.setCurrency("INR");
        payment.setStatus(Payment.PaymentStatus.CREATED);
        payment.setReceiptNumber(generateReceiptNumber());

        Payment savedPayment = paymentRepository.save(payment);
        log.info("Payment created with ID: {}", savedPayment.getId());

        return savedPayment;
    }

    public Payment updatePaymentSuccess(String razorpayPaymentId, String razorpaySignature, String paymentMethod) {
        log.info("Updating payment success for Razorpay payment ID: {}", razorpayPaymentId);

        // Find payment by payment ID in the signature or order context
        // This is a simplified approach - in production, you'd verify the signature first
        Payment payment = paymentRepository.findByRazorpayPaymentId(razorpayPaymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));

        payment.setRazorpayPaymentId(razorpayPaymentId);
        payment.setRazorpaySignature(razorpaySignature);
        payment.setPaymentMethod(paymentMethod);
        payment.setStatus(Payment.PaymentStatus.CAPTURED);
        payment.setPaidAt(LocalDateTime.now());

        // Update admission application status
        AdmissionApplication application = payment.getAdmissionApplication();
        application.setStatus(AdmissionApplication.ApplicationStatus.UNDER_REVIEW);
        admissionRepository.save(application);

        Payment savedPayment = paymentRepository.save(payment);
        log.info("Payment updated successfully for ID: {}", savedPayment.getId());

        return savedPayment;
    }

    public Payment updatePaymentFailure(String razorpayOrderId, String errorReason) {
        log.error("Payment failed for order ID: {} - Reason: {}", razorpayOrderId, errorReason);

        Payment payment = paymentRepository.findByRazorpayOrderId(razorpayOrderId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));

        payment.setStatus(Payment.PaymentStatus.FAILED);

        return paymentRepository.save(payment);
    }

    public Optional<Payment> findByOrderId(String orderId) {
        return paymentRepository.findByRazorpayOrderId(orderId);
    }

    public Optional<Payment> findByAdmissionApplicationId(Long applicationId) {
        return paymentRepository.findByAdmissionApplicationId(applicationId);
    }

    private String generateReceiptNumber() {
        return "RCP-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
