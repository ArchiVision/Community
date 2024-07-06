package com.archivision.community;

import com.archivision.community.bot.BotRegistrar;
import com.archivision.community.bot.CommunityBot;
import com.archivision.community.listener.LikesEventListener;
import com.archivision.community.listener.PaymentEventListener;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.telegram.telegrambots.meta.generics.TelegramBot;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("mocked")
@TestPropertySource(locations = "classpath:application-mocked.yaml")
class StartStopTest {
	@Autowired
	private ApplicationContext applicationContext;

	@MockBean
	private BotRegistrar botRegistrar;
	@MockBean
	private TelegramBot telegramBot;
	@MockBean
	private CommunityBot communityBot;

	@MockBean
	private RabbitTemplate rabbitTemplate;
	@MockBean
	private LikesEventListener likesEventListener;
	@MockBean
	private PaymentEventListener paymentEventListener;

	@Test
	void startStop() {
		assertNotNull(applicationContext);
	}
}
