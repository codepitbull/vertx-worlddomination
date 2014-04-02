package de.codepitbull.lejos.slave.command;

/**
 * @author Jochen Mader
 */
public final class CommandBuilder {
    public static final String separator = ":";
    public static final String terminator = "\n";

    private CommandBuilder() {
    }

    public static String shoot(int nrOfShots, String commandId) {
        StringBuilder builder = new StringBuilder();
        builder.append(CommandEnum.SHOOT).append(separator).append(nrOfShots).append(separator).append(commandId).append(terminator);
        return builder.toString();
    }

    public static String left(int nrOfTurns, String commandId) {
        StringBuilder builder = new StringBuilder();
        builder.append(CommandEnum.LEFT).append(separator).append(nrOfTurns).append(separator).append(commandId).append(terminator);
        return builder.toString();
    }

    public static String right(int nrOfTurns, String commandId) {
        StringBuilder builder = new StringBuilder();
        builder.append(CommandEnum.RIGHT).append(separator).append(nrOfTurns).append(separator).append(commandId).append(terminator);
        return builder.toString();
    }

    public static String travel(float distanceInCm, String commandId) {
        StringBuilder builder = new StringBuilder();
        builder.append(CommandEnum.TRAVEL).append(separator).append(distanceInCm).append(separator).append(commandId).append(terminator);
        return builder.toString();
    }

    public static String measure(String commandId) {
        StringBuilder builder = new StringBuilder();
        builder.append(CommandEnum.MEASURE).append(separator).append(separator).append(commandId).append(terminator);
        return builder.toString();
    }

    public static String answer(String commandId, boolean success, String value) {
        StringBuilder builder = new StringBuilder();
        builder.append(CommandEnum.ANSWER).append(separator).append(commandId).append(separator).append(success ? 1 : 0).append(separator).append(value).append(terminator);
        return builder.toString();
    }
}
