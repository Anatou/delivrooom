package fr.delivrooom.adapter.in.javafxgui.controller;

/**
 * Result of a command creation request from a state.
 * Contains either a command to execute or an error message explaining why the operation is not allowed.
 *
 * <p>This pattern allows states to provide specific, contextual error messages
 * when operations are not allowed in the current state.</p>
 *
 * @param command      The command to execute, or null if operation not allowed
 * @param errorTitle   The error dialog title, or null if operation is allowed
 * @param errorMessage The error message explaining why operation is not allowed, or null if allowed
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
