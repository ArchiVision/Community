package com.archivision.community.service;

import com.archivision.community.dto.PaymentRequestDto;
import com.archivision.community.model.Subscription;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionService {
    private final PayPalService payPalService;
    private final RedisTemplate<String, String> redisTemplate;

    // TODO: 26.01.2024 store in DB
    public List<Subscription> getAvailableSubscriptionTypes() {
        List<Subscription> subscriptions = new ArrayList<>();

        subscriptions.add(Subscription.builder()
                        .name("vip")
                        .description("Possibility to return to the previous person")
                        .price(25)
                .build());

        subscriptions.add(Subscription.builder()
                .name("premium")
                .description("Possibility to have additional 15 likes and vip abilities")
                .price(50)
                .build());

        return subscriptions;
    }

    @SneakyThrows
    public String getPaymentUrl(Long chatId, String subscriptionType) {
        // rabbit & redis
        Subscription foundSubscription = getAvailableSubscriptionTypes().stream().filter(subscription -> subscription.getName().equals(subscriptionType)).findAny().orElseThrow();
        int price = foundSubscription.getPrice();
        PaymentRequestDto requestDto = PaymentRequestDto.builder().total(price).currency("EUR").build();
        Payment payment = payPalService.createPaymentUrl(requestDto);
        String paymentId = payment.getId();
        redisTemplate.opsForValue().set(paymentId, String.valueOf(chatId));
        for (Links link : payment.getLinks()) {
            if (link.getRel().equals("approval_url")) {
                return link.getHref();
            }
        }
        // TODO: 26.01.2024 better handling
        return null;
    }
}
