package com.archivision.community.test.framework;

public final class ScenarioFactory {
    public static Scenario scenario(BaseIntegrationTest baseIntegrationTest) {
        return Scenario.scenario(
                baseIntegrationTest.getMeterRegistry(),
                baseIntegrationTest.getCommunityBot(),
                baseIntegrationTest.getUserRepository()
        );
    }
}
