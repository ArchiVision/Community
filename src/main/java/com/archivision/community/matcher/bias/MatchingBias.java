package com.archivision.community.matcher.bias;

public enum MatchingBias {
    AGE_BIAS(0.9F),
    CITY_BIAS(0.9F);

    private final float bias;

    MatchingBias(float bias) {
        this.bias = bias;
    }

    public float getBias() {
        return bias;
    }
}
