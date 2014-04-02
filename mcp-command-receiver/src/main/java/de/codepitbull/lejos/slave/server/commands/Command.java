package de.codepitbull.lejos.slave.server.commands;

/**
 * @param <S>
 */
public interface Command<S> {
    String execute(String amount, String commandId);
}
