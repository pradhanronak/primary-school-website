package com.primaryschool.website.controller;

import com.primaryschool.website.service.PaymentService;
import com.primaryschool.website.service.RazorpayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;
    private final RazorpayService razorpayService;

    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(@RequestBody Map<String, String> paymentData) {
        try {
            String razorpayOrderId = paymentData.get("razorpay_order_id");
            String razorpayPaymentId = paymentData.get("razorpay_payment_id");
            String razorpaySignature = paymentData.get("razorpay_signature");
            String paymentMethod = paymentData.get("payment_method");

            log.info("Verifying payment - Order ID: {}, Payment ID: {}", razorpayOrderId, razorpayPaymentId);

            // Verify Razorpay signature
            boolean isValidSignature = razorpayService.verifySignature(
                razorpayOrderId, razorpayPaymentId, razorpaySignature
            );

            if (!isValidSignature) {
                log.error("Invalid payment signature for payment ID: {}", razorpayPaymentId);
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Payment verification failed"
                ));
            }

            // Update payment status
            paymentService.updatePaymentSuccess(razorpayPaymentId, razorpaySignature, paymentMethod);

            log.info("Payment verified successfully - Payment ID: {}", razorpayPaymentId);

            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Payment verified successfully. Your admission application is now under review."
            ));

        } catch (Exception e) {
            log.error("Error verifying payment", e);
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", "An error occurred while verifying payment"
            ));
        }
    }

    @PostMapping("/failure")
    public ResponseEntity<?> handlePaymentFailure(@RequestBody Map<String, String> failureData) {
        try {
            String razorpayOrderId = failureData.get("razorpay_order_id");
            String errorDescription = failureData.get("error_description");

            log.error("Payment failed - Order ID: {}, Error: {}", razorpayOrderId, errorDescription);

            paymentService.updatePaymentFailure(razorpayOrderId, errorDescription);

            return ResponseEntity.ok(Map.of(
                "success", false,
                "message", "Payment failed. Please try again or contact support."
            ));

        } catch (Exception e) {
            log.error("Error handling payment failure", e);
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", "An error occurred while processing payment failure"
            ));
        }
    }

    @GetMapping("/status/{orderId}")
    public ResponseEntity<?> getPaymentStatus(@PathVariable String orderId) {
        return paymentService.findByOrderId(orderId)
                .map(payment -> ResponseEntity.ok(Map.of(
                    "status", payment.getStatus(),
                    "amount", payment.getAmount(),
                    "currency", payment.getCurrency(),
                    "receiptNumber", payment.getReceiptNumber()
                )))
                .orElse(ResponseEntity.notFound().build());
    }
}
