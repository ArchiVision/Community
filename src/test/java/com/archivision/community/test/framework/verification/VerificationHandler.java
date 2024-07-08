package com.archivision.community.test.framework.verification;

public interface VerificationHandler<T extends Verification> {
    void handle(T verification, long timeout, long waitAtLeast);
    default void handle(T verification, long timeout) {
        handle(verification, timeout, 0);
    }
}
