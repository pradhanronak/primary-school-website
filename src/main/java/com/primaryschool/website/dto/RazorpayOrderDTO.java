package com.primaryschool.website.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RazorpayOrderDTO {

    private String orderId;
    private BigDecimal amount;
    private String currency;
    private String key;
    private String name;
    private String description;
    private String image;
    private PrefillDTO prefill;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PrefillDTO {
        private String name;
        private String email;
        private String contact;
    }
}
