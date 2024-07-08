package com.archivision.community.test.framework.verification;

import com.archivision.community.test.framework.Scenario;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.core.ConditionTimeoutException;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
@Data
@Slf4j
public class UserPresenceInDbVerificationHandler implements VerificationHandler<UserPresenceInDbVerification> {
    private final Scenario scenario;

    @Override
    public void handle(UserPresenceInDbVerification verification, long timeoutMillis, long waitAtLeast) {
        try {
            log.info("Verifying... {}", verification);
            await("bot")
                    .atMost(timeoutMillis, TimeUnit.MILLISECONDS)
                    .with()
                    .pollInterval(Scenario.POLL_INTERVAL, TimeUnit.MILLISECONDS)
                    .pollDelay(waitAtLeast, TimeUnit.MILLISECONDS)
                    .until(() -> scenario.getUserRepository().findByTelegramUserId(verification.getTelegramUserId()).isPresent());
            log.info("Passed!");
        } catch (ConditionTimeoutException t) {
            log.warn("Failed verification: expected registration of user with id {}", verification.getTelegramUserId());
            throw t;
        }
    }
}