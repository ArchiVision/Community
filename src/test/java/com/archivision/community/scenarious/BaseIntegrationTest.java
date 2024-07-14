package com.archivision.community.scenarious;

import com.archivision.community.bot.BotRegistrar;
import com.archivision.community.bot.CommunityBot;
import com.archivision.community.listener.LikesEventListener;
import com.archivision.community.listener.PaymentEventListener;
import com.archivision.community.messagesender.MessageSender;
import com.archivision.community.processor.UpdateProcessor;
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
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@ActiveProfiles("mocked")
@Slf4j
@DirtiesContext
public class BaseIntegrationTest {
    private static final int REDIS_MAPPED_PORT = 6379;

    @MockBean
    private BotRegistrar botRegistrar;
    @MockBean
    private TelegramBot telegramBot;

    @MockBean
    private MessageSender messageSender;

    @Getter
    @Autowired
    private CommunityBot communityBot;

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
    private UpdateProcessor updateProcessor;

    @Autowired
    @Getter
    protected UserRepository userRepository;

    static {
        GenericContainer<?> redis =
                new GenericContainer<>(DockerImageName.parse("redis:6.2.6")).withExposedPorts(REDIS_MAPPED_PORT);
        redis.start();
        System.setProperty("spring.data.redis.url", "redis://" + redis.getHost() + ":" + redis.getMappedPort(REDIS_MAPPED_PORT));
    }
}
