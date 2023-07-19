package com.archivision.community.matcher;

import com.archivision.community.document.User;
import com.archivision.community.matcher.model.UserWithMatchedProbability;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MatchedUsersListResolver {
    private final UserMatcher userMatcher;
    public List<UserWithMatchedProbability> getOrderedMatchingList(User user, List<User> userTargets) {
        return userTargets.stream()
                .map(userTarget -> getUserWithMatchedProbabilityOrNull(user, userTarget))
                .filter(Objects::nonNull)
                .sorted(new UserComparator())
                .collect(Collectors.toList());
    }

    private UserWithMatchedProbability getUserWithMatchedProbabilityOrNull(User user, User userTarget) {
        final MatchResult match = userMatcher.match(user, userTarget);
        if (match.isOk()) {
            return new UserWithMatchedProbability(userTarget, match.getMatchingProbability());
        }
        return null;
    }
}


