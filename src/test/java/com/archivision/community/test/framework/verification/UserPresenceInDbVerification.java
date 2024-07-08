package com.archivision.community.test.framework.verification;

import lombok.Data;

@Data
public class UserPresenceInDbVerification implements Verification {
    private final long telegramUserId;

    public static UserPresenceInDbVerification userPresentInDb(long userId) {
        return new UserPresenceInDbVerification(userId);
    }
}