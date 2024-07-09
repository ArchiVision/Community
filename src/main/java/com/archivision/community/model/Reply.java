package com.archivision.community.model;

public enum Reply {
    YES("Так"),
    NO("Ні"),
    CHANGE("Змінити"),
    SKIP("Пропустити"),
    MAN("Хлопець"),
    GIRL("Дівчина"),
    OTHER("Інше"),
    LIKE("+"),
    DISLIKE("-"),
    UNIT("Юніт"),
    PERSON("Особа"),
    SETTINGS("settings");

    private final String replyOption;
    Reply(String replyOption) {
        this.replyOption = replyOption;
    }

    public static Reply fromString(String text) {
        for (Reply reply : Reply.values()) {
            if (reply.replyOption.equalsIgnoreCase(text)) {
                return reply;
            }
        }
        throw new IllegalArgumentException("No constant with text " + text + " found");
    }

    @Override
    public String toString() {
        return replyOption;
    }
}
