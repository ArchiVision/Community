package com.archivision.community.test.framework;

import com.archivision.community.TestBeansOverride;
import com.archivision.community.bot.CommunityBot;
import com.archivision.community.repo.UserRepository;
import com.archivision.community.test.framework.verification.UserPresenceInDbVerification;
import com.archivision.community.test.framework.verification.UserPresenceInDbVerificationHandler;
import com.archivision.community.test.framework.verification.Verification;
import com.archivision.community.test.framework.verification.VerificationHandler;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Slf4j
public class Scenario {
    private final List<Verification> verifications = new ArrayList<>();
    private final Map<Class<?>, VerificationHandler> verificationHandlers = new ConcurrentHashMap<>();

    public static final int POLL_INTERVAL = 50;

    @Getter
    private MeterRegistry meterRegistry;

    @Getter
    private CommunityBot communityBot;

    @Getter
    private UserRepository userRepository;

    public static Scenario scenario(@NonNull MeterRegistry meterRegistry,
                                    CommunityBot communityBot,
                                    UserRepository userRepository) {

        final Scenario scenario = new Scenario();
        scenario.meterRegistry = meterRegistry;
        scenario.communityBot = communityBot;
        scenario.userRepository = userRepository;

        registerVerificationHandlers(scenario);

        return scenario;
    }

    public Scenario sendUpdate(Update update) {
        communityBot.onUpdateReceived(update);
        return this;
    }

    private static void registerVerificationHandlers(Scenario scenario) {
        scenario.verificationHandlers.put(UserPresenceInDbVerification.class, new UserPresenceInDbVerificationHandler(scenario));
    }

    public Scenario verify(List<Verification> verifications) {
        this.verifications.clear();
        this.verifications.addAll(verifications);
        return this;
    }

    public <T extends Verification> Scenario verify(T... verifications) {
        List<Verification> verificationList = new ArrayList<>();
        if (verifications != null && verifications.length > 0) {
            verificationList.addAll(Arrays.asList(verifications));
        }
        return verify(verificationList);
    }

    public <T extends Verification> void registerVerificationHandler(Class<T> verificationClass, VerificationHandler<T> handler) {
        this.verificationHandlers.put(verificationClass, handler);
    }

}
