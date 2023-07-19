package com.archivision.community.matcher;

import com.archivision.community.matcher.model.UserWithMatchedProbability;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;

import static java.lang.Math.abs;

@Component
@RequiredArgsConstructor
public class UserComparator implements Comparator<UserWithMatchedProbability> {
    private static final double DOUBLE_TOLERANCE = 1e-3;

    @Override
    public int compare(UserWithMatchedProbability user1, UserWithMatchedProbability user2) {
        return isDoublesEqualWithTolerance(user1.probability(), user2.probability()) ? 0 :
                user1.probability() > user2.probability() ? 1 : -1;
    }

    private boolean isDoublesEqualWithTolerance(double a, double b) {
        return abs(a - b) <= DOUBLE_TOLERANCE;
    }
}
