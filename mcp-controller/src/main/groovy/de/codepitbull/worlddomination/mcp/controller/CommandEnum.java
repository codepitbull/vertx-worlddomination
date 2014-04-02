package de.codepitbull.worlddomination.mcp.controller;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jochen Mader
 */
public enum CommandEnum {
    SHOOT("S"),
    LEFT("L"),
    RIGHT("R"),
    TRAVEL("T"),
    MEASURE("M"),
    ANSWER("A");

    private static final Map<String, CommandEnum> map = new HashMap<>();
    static {
        for(CommandEnum command: CommandEnum.values()) {
            map.put(command.text, command);
        }
    }

    private String text;

    private CommandEnum(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public static CommandEnum commandFromString(String command) {
        return map.get(command);
    }
}
