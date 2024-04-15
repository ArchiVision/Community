package com.archivision.community.controller;

import com.archivision.community.dto.PaymentRequestDto;
import com.archivision.community.event.PaymentEvent;
import com.archivision.community.service.PayPalService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/paypal")
@RequiredArgsConstructor
@Slf4j
public class PayPalController {
    private final PayPalService payPalService;
    private final RabbitTemplate rabbitTemplate;
    @Value("${community.payment-queue}")
    private String paymentEventQueue;

    @PostMapping("/pay")
    public ResponseEntity<?> payment(@RequestBody PaymentRequestDto paymentRequest) {
        try {
            Payment payment = payPalService.createPayment(
                    paymentRequest.getTotal(),
                    paymentRequest.getCurrency(),
                    "paypal",
                    "sale",
                    "Payment description",
                    "https://2bf3-194-44-71-193.ngrok-free.app/paypal/cancel",
                    "https://2bf3-194-44-71-193.ngrok-free.app/paypal/success");

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
    public void successPay(@RequestParam("paymentId") String paymentId,
                           @RequestParam("PayerID") String payerId,
                           HttpServletResponse response) {
        try {
            Payment payment = payPalService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                log.info("Success payment: {}", payment);
                rabbitTemplate.convertAndSend(paymentEventQueue, new PaymentEvent(paymentId, "success", payment.getTransactions().get(0).getAmount().getTotal()));
                response.sendRedirect("https://t.me/CommuLinkBot");
            }
        } catch (PayPalRESTException e) {
            log.error("Payment error: {}", e.getMessage(), e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}


