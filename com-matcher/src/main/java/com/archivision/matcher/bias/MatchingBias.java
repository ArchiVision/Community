package com.archivision.matcher.bias;

public enum MatchingBias {
    AGE_BIAS(1F),
    CITY_BIAS(1F),
    TOPICS(0.7F);

    private final float bias;

    MatchingBias(float bias) {
        this.bias = bias;
    }

    public float getBias() {
        return bias;
    }
}
