package com.archivision.community.scenarious;

import com.archivision.community.test.framework.UpdateFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static com.archivision.community.test.framework.ScenarioFactory.scenario;
import static com.archivision.community.test.framework.verification.UserPresenceInDbVerification.userPresentInDb;

@Slf4j
@RunWith(SpringRunner.class)
@ActiveProfiles("mocked")
public class BasicUpdateIntegrationTest extends BaseIntegrationTest {
    private final long telegramUserId = 1;
    private final String userName = "TestUser";

    @Test
    public void testFullUserRegistrationFlow() {
        scenario(this)
                .sendUpdate(UpdateFactory.of(telegramUserId, userName, "/start"))
                .sendUpdate(UpdateFactory.of(telegramUserId, userName, "Ярослав"))
                .sendUpdate(UpdateFactory.of(telegramUserId, userName, "28"))
                .sendUpdate(UpdateFactory.of(telegramUserId, userName, "Хлопець"))
                .sendUpdate(UpdateFactory.of(telegramUserId, userName, "Дівчат"))
                .sendUpdate(UpdateFactory.of(telegramUserId, userName, "Львів"))
                .sendUpdate(UpdateFactory.of(telegramUserId, userName, "Пропустити"))
                .sendUpdate(UpdateFactory.of(telegramUserId, userName, "Пропустити"))
                .sendUpdate(UpdateFactory.of(telegramUserId, userName, "Пропустити"))
                .sendUpdate(UpdateFactory.of(telegramUserId, userName, "Так"))
                .verify(userPresentInDb(telegramUserId));
    }
}
