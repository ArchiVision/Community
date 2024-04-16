package com.archivision.bot.service;


import com.archivision.bot.sender.MessageSender;
import com.archivision.bot.service.user.UserService;
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
        messageSender.sendTextMessage(likedId, "У вас симпатія! @" +
                userService.getUserByTgId(likerId).getUsername());
        profileSender.showUserProfileTo(likerId, likedId);

        messageSender.sendTextMessage(likerId, "У вас симпатія! @" +
                userService.getUserByTgId(likedId).getUsername());
        profileSender.showUserProfileTo(likedId, likerId);
    }

    public void notifyUserAboutSuccessfulPayment(String chatId, String message) {
        messageSender.sendTextMessage(Long.valueOf(chatId), message);
    }
}
