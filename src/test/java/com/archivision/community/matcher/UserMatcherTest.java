package com.archivision.community.matcher;

import com.archivision.community.document.User;
import com.archivision.community.matcher.nlp.LevenshteinAlgorithm;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 *  Tests for verifying probabilities for matching algorithm
 *  Percentage values are theoretical and should be asserted empirical
 * **/
class UserMatcherTest {
    private final double DELTA = 0.001;
    private final UserMatcher userMatcher = new UserMatcher(new LevenshteinAlgorithm());

    @Test
    public void testIdealMatch() {
        // Ideal match - when match is 1.0
        User userA = new User();
        userA.setAge(18L);
        userA.setCity("Lviv");

        User userB = new User();
        userB.setAge(18L);
        userB.setCity("Lviv");

        userMatcher.match(userA, userB);

        assertEquals(1.0, userMatcher.match(userA, userB).getMatchingProbability(), DELTA);
    }

    @Test
    public void testPartialMissMatchForCity() {
        // Mismatch = when match is less then 50%
        // Partial mistmatch = when at least one parameter have match < 50 (But not all)

        User userA = new User();
        userA.setAge(18L);
        userA.setCity("Lviv");

        User userB = new User();
        userB.setAge(18L);
        userB.setCity("Kyiv");

        userMatcher.match(userA, userB);

        assertEquals(0.5, userMatcher.match(userA, userB).getMatchingProbability(), DELTA);
    }

    @Test
    public void testPartialMissMatchForAge() {
        // Slight age difference
        User userA = new User();
        userA.setAge(17L);
        userA.setCity("Lviv");

        User userB = new User();
        userB.setAge(18L);
        userB.setCity("Lviv");

        userMatcher.match(userA, userB);

        assertEquals(0.683, userMatcher.match(userA, userB).getMatchingProbability(), DELTA);

        // Big age difference
        User userC = new User();
        userC.setAge(16L);
        userC.setCity("Lviv");

        User userD = new User();
        userD.setAge(18L);
        userD.setCity("Lviv");

        userMatcher.match(userC, userD);

        assertEquals(0.567, userMatcher.match(userC, userD).getMatchingProbability(), DELTA);
    }

    @Test
    public void testAgeDatingCondition() {
        User userA = new User();
        userA.setAge(10L);
        userA.setCity("Lviv");

        User userB = new User();
        userB.setAge(18L);
        userB.setCity("Lviv");

        userMatcher.match(userA, userB);


        MatchResult match = userMatcher.match(userA, userB);
        assertNull(match.getMatchingProbability());
        assertEquals(MatchStatus.UNACCEPTABLE_AGE_DIFFERENCE,match.getMatchStatus());
    }
}