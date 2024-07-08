package com.archivision.community.scenarious;

import com.archivision.community.TestBeansOverrides;
import com.archivision.community.bot.BotRegistrar;
import com.archivision.community.listener.LikesEventListener;
import com.archivision.community.listener.PaymentEventListener;
import com.archivision.community.repo.UserRepository;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.telegram.telegrambots.meta.generics.TelegramBot;

@SpringBootTest
@ActiveProfiles("mocked")
@Slf4j
@DirtiesContext
public class BaseIntegrationTest {
    @MockBean
    private BotRegistrar botRegistrar;
    @MockBean
    private TelegramBot telegramBot;

    @MockBean
    private RabbitTemplate rabbitTemplate;
    @MockBean
    private LikesEventListener likesEventListener;
    @MockBean
    private PaymentEventListener paymentEventListener;

    @Autowired
    @Getter
    protected MeterRegistry meterRegistry;
    @Autowired
    @Getter
    protected TestBeansOverrides.CommunityBotMock communityBotMock;
    @Autowired
    @Getter
    protected UserRepository userRepository;
}
