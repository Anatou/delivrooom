# Delivrooom Architecture: State-Driven Command Factory Pattern

## Overview

The Delivrooom application implements a sophisticated **State-Driven Command Factory** pattern that combines the State and Command design patterns to
manage user interactions and application state transitions with full undo/redo support.

## Architecture Flow

```
┌─────────────┐
│UI Components│
└──────┬──────┘
       │ 1. User Action
       ▼
┌─────────────────┐
│  AppController  │
│  requestX()     │
└──────┬──────────┘
       │ 2. Ask State
       ▼
┌─────────────────┐
│  Current State  │
│  (Factory)      │
└──────┬──────────┘
       │ 3. Create Command
       ▼
┌─────────────────┐
│    Command      │
└──────┬──────────┘
       │ 4. Execute via Manager
       ▼
┌─────────────────┐
│ CommandManager  │
│ (Undo/Redo)     │
└──────┬──────────┘
       │ 5. Execute
       ▼
┌─────────────────┐
│    Command      │
│  execute()      │
└──────┬──────────┘
       │ 6. Modify Data
       ▼
┌─────────────────┐
│  AppController  │
│  doX()          │
└──────┬──────────┘
       │ 7. May Trigger
       ▼
┌─────────────────┐
│State Transition │
└─────────────────┘
```

## Core Components

### 1. AppController

The central controller implementing the Singleton pattern with three distinct API levels:

#### Public API (for UI components)

- `requestX()` methods - Request operations from UI
- `requestCommand(Command)` - Execute a command via the manager
- `undoCommand()` - Undo the last command
- `redoCommand()` - Redo the last undone command

#### Protected API (for commands)

- `doX()` methods - Perform actual data modifications
- `doRestoreX()` methods - Restore previous states for undo

#### Package-Private API (for states/commands)

- `transitionToState(State)` - Change application state
- Helper methods for state queries

### 2. State Interface (Command Factory)

States act as command factories, creating commands for operations that are valid in their context:

```java
public interface State {
    Command createOpenMapCommand(URL url);

    Command createOpenDeliveriesCommand(URL url);

    Command createAddDeliveryCommand(Delivery delivery);

    Command createRemoveDeliveryCommand(Delivery delivery);

    Command createSelectIntersectionCommand(Intersection intersection);

    Command createRequestIntersectionSelectionCommand();

    Command createCalculateTourCommand();

    Command createCalculateCourierTourCommand(Courier courier);

    Command createAssignCourierCommand(Delivery delivery, Courier courier);

    String getStateName();
}
```

**Key Principle**: States return `null` for operations not allowed in their context.

### 3. State Implementations

#### StateInitial

- **Allows**: Opening map files
- **Transitions to**: StateMapLoaded

#### StateMapLoaded

- **Allows**: Opening map files, opening deliveries files
- **Transitions to**: StateMapLoaded (new map), StateDeliveriesLoaded (deliveries loaded)

#### StateDeliveriesLoaded

- **Allows**: All file operations, add/remove deliveries, calculate tours, request intersection selection, assign couriers
- **Transitions to**: StateMapLoaded, StateDeliveriesLoaded, StateSelectIntersection

#### StateSelectIntersection

- **Allows**: All operations from DeliveriesLoaded + selecting intersections
- **Transitions to**: StateDeliveriesLoaded (after selection)

### 4. Command Pattern

All commands implement the `Command` interface:

```java
public interface Command {
    void execute();

    void undo();
}
```

#### Implemented Commands

| Command                               | Purpose                    | Undo Strategy                      |
|---------------------------------------|----------------------------|------------------------------------|
| `CommandLoadMap`                      | Load city map              | Restore previous map               |
| `CommandLoadDeliveries`               | Load deliveries            | Restore previous deliveries        |
| `CommandAddDelivery`                  | Add delivery               | Remove delivery                    |
| `CommandRemoveDelivery`               | Remove delivery            | Re-add delivery                    |
| `CommandSelectIntersection`           | Select intersection        | Restore previous selection + state |
| `CommandRequestIntersectionSelection` | Enter selection mode       | Return to previous state           |
| `CommandCalculateTour`                | Calculate tour             | Restore previous tour              |
| `CommandCalculateCourierTour`         | Calculate courier tour     | Restore previous tour              |
| `CommandAssignCourier`                | Assign courier to delivery | Restore previous assignment        |

### 5. CommandManager

Manages command execution with undo/redo stacks:

```java
public class CommandManager {
    private final Stack<Command> undoStack;
    private final Stack<Command> redoStack;

    public void executeCommand(Command command);

    public void undo();

    public void redo();
}
```

## Method Naming Conventions

### Public Methods (UI → Controller)

- `requestX()` - Request an operation
- Example: `requestOpenMapFile(File file)`

### Protected Methods (Commands → Controller)

- `doX()` - Perform data modification
- `doRestoreX()` - Restore previous state
- Examples: `doLoadMapFile(URL url)`, `doRestoreCityMap(CityMap map)`

### Package-Private Methods (States/Commands → Controller)

- `transitionToState(State newState)` - Change state
- `getXForY()` - Query helper methods

## Benefits

### ✅ Full Undo/Redo Support

All operations are undoable, including state transitions, providing a complete history of user actions.

### ✅ Strong Encapsulation

The controller has a minimal public API (only `requestX()`, `undoCommand()`, `redoCommand()`), hiding implementation details.

### ✅ State Validation

States control which operations are allowed, preventing invalid actions and providing clear error messages.

### ✅ Testability

Clear separation of concerns makes each component easy to test in isolation.

### ✅ Maintainability

Adding new commands or states is straightforward without modifying existing code.

### ✅ Flexibility

Commands can be composed, queued, or logged for advanced features like macros or audit trails.

## Usage Example

```java
// UI Component requests to open a map file
File mapFile = fileChooser.showOpenDialog(stage);
controller.requestOpenMapFile(mapFile);

// Flow:
// 1. Controller calls state.createOpenMapCommand(url)
// 2. State returns CommandLoadMap or null
// 3. Controller executes command via CommandManager
// 4. Command calls controller.doLoadMapFile(url)
// 5. Command triggers state transition
// 6. User can undo with controller.undoCommand()
```

## State Transition Diagram

```
┌─────────────┐
│   Initial   │
└──────┬──────┘
       │ Load Map
       ▼
┌─────────────┐
│ Map Loaded  │
└──────┬──────┘
       │ Load Deliveries
       ▼
┌──────────────────┐      Request Selection      ┌────────────────────┐
│Deliveries Loaded │◄──────────────────────────────│Select Intersection │
└──────────────────┘      Select Intersection     └────────────────────┘
```

## Future Enhancements

1. **Command Composition**: Combine multiple commands into macro commands
2. **Command Serialization**: Save/load command history for session recovery
3. **Command Logging**: Audit trail of all user actions
4. **Async Commands**: Support for long-running operations with progress tracking
5. **Command Validation**: Pre-execution validation before adding to undo stack

## Migration Notes

### Breaking Changes

- Old `handleX()` methods replaced with `requestX()` methods
- States no longer execute operations directly
- UI components must use new `requestX()` API

### Backward Compatibility

- All existing functionality preserved
- Undo/redo now works for all operations
- Error handling improved with state validation

## References

- [State Pattern](https://refactoring.guru/design-patterns/state)
- [Command Pattern](https://refactoring.guru/design-patterns/command)
- [Hexagonal Architecture](https://www.happycoders.eu/software-craftsmanship/hexagonal-architecture-java/)
