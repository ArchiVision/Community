package com.archivision.community.matcher;

import com.archivision.community.document.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.archivision.community.matcher.bias.MatchingBias.AGE_BIAS;
import static com.archivision.community.matcher.bias.MatchingBias.CITY_BIAS;
import static java.lang.Math.abs;
import static java.lang.Math.exp;

/**
 * User A is a user which is sending request to get matches.
 */
@Component
@RequiredArgsConstructor
public class UserMatcher {
    private static final int NUMBER_OF_COMPARISON_PARAMS = 2;

    public MatchResult match(User userA, User userB) {
        if (!isAgeAcceptableForDating(userA.getAge(), userB.getAge())) {
            return MatchResult.failed(MatchStatus.UNACCEPTABLE_AGE_DIFFERENCE);
        }

        try {
            return MatchResult.ok(getMatchingProbability(userA, userB));
        } catch (Exception e) {
            return MatchResult.failed(MatchStatus.UNDEFINED_ERROR);
        }
    }

    /**
    * I use here weighted average.
    *   This method will not kill first value if second is 0 as it would be in multiplication
    *   This method still allow to impact on first value if second is 0 how can't do addition
    *   Biases allow us to stay in 0 to 1 interval as can't do plain addition
    */
    private double getMatchingProbability(User userA, User userB) {
        return ((getMatchedAgeProbability(userA.getAge(), userB.getAge()) * AGE_BIAS.getBias()) +
               (getMatchedCitiesProbability(userA.getCity(), userB.getCity()) * CITY_BIAS.getBias())) / NUMBER_OF_COMPARISON_PARAMS;

    }

    private double getMatchedAgeProbability(Long ageA, Long ageB) {
        return exp(-1 * abs(ageA - ageB));
    }

    private double getMatchedCitiesProbability(String cityA, String cityB) {
        return cityA.equalsIgnoreCase(cityB) ? 1D : 0D;
    }

    private boolean isAgeAcceptableForDating(Long ageA, Long ageB) {
        return ageB <= calculateMaximumDatingAge(ageA) && ageB >= calculateMinimumDatingAge(ageA);
    }

    private Long calculateMinimumDatingAge(Long userAge) {
        return (userAge / 2) + 7;
    }

    private Long calculateMaximumDatingAge(Long userAge) {
        return (userAge - 7) * 2;
    }
}
