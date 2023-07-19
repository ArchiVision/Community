package com.archivision.community.matcher.bias;

public enum MatchingBias {
    AGE_BIAS(1F),
    CITY_BIAS(1F);

    private final float bias;

    MatchingBias(float bias) {
        this.bias = bias;
    }

    public float getBias() {
        return bias;
    }
}
