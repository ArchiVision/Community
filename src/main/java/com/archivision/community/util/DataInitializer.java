package com.archivision.community.util;

import com.archivision.community.service.user.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class DataInitializer {
    private final UserService userService;

    @PostConstruct
    public void init() {
//        for (int i = 0; i < 50; i++) {
//            User user = new User();
//            user.setName("User#" + i+1);
//            user.setState(State.MATCH);
//            user.setLookingFor(Gender.ANYONE);
//            user.setCity("Lviv");
//            user.setDescription(null);
//            user.setPhotoId(null);
//            user.setAge((long) (18 + i));
//            user.setTelegramUserId(...);
//            userService.saveUser(user);
//        }
    }
}
