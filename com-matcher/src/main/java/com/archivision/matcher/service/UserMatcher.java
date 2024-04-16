package com.archivision.matcher.service;

import com.archivision.common.model.entity.Topic;
import com.archivision.common.model.entity.User;
import com.archivision.matcher.model.MatchResult;
import com.archivision.matcher.model.MatchStatus;
import com.archivision.matcher.nlp.TopicComparator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;

import static com.archivision.matcher.bias.MatchingBias.*;
import static java.lang.Math.abs;
import static java.lang.Math.exp;

/**
 * User A is a user which is sending request to get matches.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class UserMatcher {
    private final TopicComparator topicComparator;
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
                (getMatchedCitiesProbability(userA.getCity(), userB.getCity()) * CITY_BIAS.getBias()) +
                (getMatchedTopicsProbability(userA.getTopics(), userB.getTopics())) * TOPICS.getBias())
                / getNumberOfAvailableParams(userA);

    }

    /**
     * This method is needed for identification of probability divider for general matched
     * probability. We consider only available params for matching probability.
     */
    private int getNumberOfAvailableParams(User user) {
        return (user.getAge() != null ? 1 : 0) +
                (!user.getTopics().isEmpty() ? 1 : 0) +
                (user.getCity() != null ? 1 : 0);
    }

    private double getMatchedTopicsProbability(Set<Topic> topics1, Set<Topic> topics2) {
        return topics1.stream()
                .flatMap(topic1 -> topics2.stream()
                        .map(topic2 -> topicComparator.compare(
                                topic1.getName(),
                                topic2.getName())
                        )
                )
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
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
