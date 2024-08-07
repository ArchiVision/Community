package com.archivision.community.listener;

import com.archivision.community.entity.User;
import com.archivision.community.event.PaymentEvent;
import com.archivision.community.service.NotificationService;
import com.archivision.community.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventListener {
    private final NotificationService notificationService;
    private final RedisTemplate<String, String> redisTemplate;
    private final UserService userService;

    @RabbitListener(queues = "payment-events")
    public void handleSuccessPayment(PaymentEvent event) {
        final String chatId = redisTemplate.opsForValue().getAndDelete(event.paymentId());
        userService.changeSubscription(chatId, User.Subscription.VIP);
        notificationService.notifyUserAboutSuccessfulPayment(chatId, "Успішний платіж!");
    }
}
