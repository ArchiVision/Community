package com.archivision.community.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PaymentController {
    @PostMapping("/payment")
    public void processPayment(@RequestParam MultiValueMap<String, String> formData) {
        // testing
        // Content-Type 'application/x-www-form-urlencoded
        log.info(String.valueOf(formData));
    }
}
