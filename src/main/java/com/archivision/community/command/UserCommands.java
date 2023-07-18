package com.archivision.community.command;

public enum UserCommands {
    START("/start");

    private final String cmd;

    UserCommands(String cmd) {
        this.cmd = cmd;
    }

    public String value() {
        return cmd;
    }
}
