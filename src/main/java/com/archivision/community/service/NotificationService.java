package com.archivision.community.service;

import com.archivision.community.entity.User;
import com.archivision.community.messagesender.MessageSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {
    private final MessageSender messageSender;
    private final UserService userService;
    private final ProfileSender profileSender;
    public void notifyUsersAboutMatch(Long likerId, Long likedId) {
        User likerUser = userService.getUserByTgId(likerId);
        User likedUser = userService.getUserByTgId(likedId);

        messageSender.sendTextMessage(likedId, "У вас симпатія! @"+likerUser.getUsername());
        profileSender.showUserProfileTo(likerId, likedId);

        messageSender.sendTextMessage(likerId, "У вас симпатія! @"+likedUser.getUsername());
        profileSender.showUserProfileTo(likedId, likerId);
    }
}
