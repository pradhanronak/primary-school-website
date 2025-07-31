package com.primaryschool.website.service;

import com.primaryschool.website.dto.RazorpayOrderDTO;
import com.primaryschool.website.entity.AdmissionApplication;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
@Slf4j
public class RazorpayService {

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    @Value("${razorpay.admission.amount}")
    private BigDecimal admissionAmount;

    private RazorpayClient razorpayClient;

    private RazorpayClient getRazorpayClient() throws RazorpayException {
        if (razorpayClient == null) {
            razorpayClient = new RazorpayClient(keyId, keySecret);
        }
        return razorpayClient;
    }

    public RazorpayOrderDTO createOrder(AdmissionApplication application) throws RazorpayException {
        log.info("Creating Razorpay order for admission application ID: {}", application.getId());

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", admissionAmount.multiply(BigDecimal.valueOf(100)).intValue()); // Amount in paise
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "admission_" + application.getId());

        JSONObject notes = new JSONObject();
        notes.put("application_id", application.getId());
        notes.put("student_name", application.getStudentName());
        notes.put("parent_email", application.getParentEmail());
        orderRequest.put("notes", notes);

        Order order = getRazorpayClient().orders.create(orderRequest);
        log.info("Razorpay order created: {}", order.get("id"));

        RazorpayOrderDTO orderDTO = new RazorpayOrderDTO();
        orderDTO.setOrderId(order.get("id"));
        orderDTO.setAmount(admissionAmount);
        orderDTO.setCurrency("INR");
        orderDTO.setKey(keyId);
        orderDTO.setName("Primary School Admission");
        orderDTO.setDescription("Admission Application Fee for " + application.getStudentName());
        orderDTO.setImage("/images/school-logo.png");

        // Set prefill data
        RazorpayOrderDTO.PrefillDTO prefill = new RazorpayOrderDTO.PrefillDTO();
        prefill.setName(application.getParentName());
        prefill.setEmail(application.getParentEmail());
        prefill.setContact(application.getParentPhone());
        orderDTO.setPrefill(prefill);

        return orderDTO;
    }

    public boolean verifySignature(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature) {
        try {
            String payload = razorpayOrderId + "|" + razorpayPaymentId;

            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(keySecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(secretKey);

            byte[] hash = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            String calculatedSignature = hexString.toString();
            boolean isValid = calculatedSignature.equals(razorpaySignature);

            log.info("Signature verification result: {} for payment ID: {}", isValid, razorpayPaymentId);
            return isValid;

        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            log.error("Error verifying Razorpay signature", e);
            return false;
        }
    }

    public BigDecimal getAdmissionAmount() {
        return admissionAmount;
    }
}
