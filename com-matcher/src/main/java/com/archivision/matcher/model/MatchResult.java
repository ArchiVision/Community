package com.archivision.matcher.model;

public final class MatchResult {
    private final Double matchingProbability;
    private final MatchStatus matchStatus;

    private MatchResult(Double matchingProbability, MatchStatus matchStatus) {
        this.matchingProbability = matchingProbability;
        this.matchStatus = matchStatus;
    }

    public Double getMatchingProbability() {
        return matchingProbability;
    }

    public MatchStatus getMatchStatus() {
        return matchStatus;
    }

    public static MatchResult ok(Double matchingProbability) {
        return new MatchResult(matchingProbability, MatchStatus.MATCHED);
    }

    public boolean isOk() {
        return this.matchingProbability != null && this.matchStatus == MatchStatus.MATCHED;
    }

    public static MatchResult failed(MatchStatus matchStatus) {
        return new MatchResult(null, matchStatus);
    }
}