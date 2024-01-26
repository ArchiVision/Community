package com.archivision.community.controller;

import com.archivision.community.dto.PaymentRequestDto;
import com.archivision.community.service.PayPalService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/paypal")
@RequiredArgsConstructor
@Slf4j
public class PayPalController {
    private final PayPalService payPalService;

    @PostMapping("/pay")
    public ResponseEntity<?> payment(@RequestBody PaymentRequestDto paymentRequest) {
        try {
            Payment payment = payPalService.createPayment(
                    paymentRequest.getTotal(),
                    paymentRequest.getCurrency(),
                    "paypal",
                    "sale",
                    "Payment description",
                    "http://localhost:8080/paypal/cancel",
                    "http://localhost:8080/paypal/success");

            for (Links link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    return ResponseEntity.ok(link.getHref());
                }
            }
        } catch (PayPalRESTException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
        return ResponseEntity.badRequest().body("Unable to process payment");
    }

    @GetMapping("/cancel")
    public void cancelPay() {
        log.info("Canceling a payment");
    }

    @GetMapping("/success")
    public void successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        try {
            Payment payment = payPalService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                log.info("Success payment: {}", payment);
                // do smth
            }
        } catch (PayPalRESTException e) {
            log.error("Payment error: {}", e.getMessage(), e);
        }
    }
}


