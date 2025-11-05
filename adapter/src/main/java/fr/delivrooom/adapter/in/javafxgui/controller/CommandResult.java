package fr.delivrooom.adapter.in.javafxgui.controller;

/**
 * Represents the result of a command creation attempt from a state.
 * This record can hold either a valid {@link Command} to be executed or error details if the command
 * could not be created (e.g., because the operation is invalid in the current state).
 * This pattern allows states to return specific, contextual error messages.
 *
 * @param command      The command to execute, or {@code null} if the operation is not allowed.
 * @param errorTitle   The title for an error dialog, or {@code null} if the operation is successful.
 * @param errorMessage The message for an error dialog, or {@code null} if the operation is successful.
 */
public record CommandResult(Command command, String errorTitle, String errorMessage) {

    /**
     * Create a successful result with a command to execute.
     *
     * @param command The command to execute
     * @return A successful command result
     */
    public static CommandResult success(Command command) {
        return new CommandResult(command, null, null);
    }

    /**
     * Create an error result with a message explaining why the operation is not allowed.
     *
     * @param errorTitle   The error dialog title
     * @param errorMessage The error message
     * @return An error command result
     */
    public static CommandResult error(String errorTitle, String errorMessage) {
        return new CommandResult(null, errorTitle, errorMessage);
    }

    /**
     * Check if this result represents a successful command creation.
     *
     * @return true if a command is available, false if this is an error
     */
    public boolean isSuccess() {
        return command != null;
    }

    /**
     * Check if this result represents an error.
     *
     * @return true if this is an error, false if a command is available
     */
    public boolean isError() {
        return command == null;
    }
}
