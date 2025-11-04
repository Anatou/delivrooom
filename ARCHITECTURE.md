# GUI Architecture: State-Driven Command Factory

This document outlines the State-Driven Command Factory pattern used in the Delivrooom GUI. It's a combination of the **State** and **Command** design
patterns, providing robust state management and full undo/redo capabilities.

## Core Concepts

1. **`AppController` (Singleton)**: The central hub for all UI interactions. It exposes a limited public API to the UI (`requestX()`, `undo()`,
   `redo()`) and delegates command creation to the current state.

2. **`State` Interface (Command Factory)**: Each state represents a specific application context (e.g., `StateInitial`, `StateMapLoaded`). States act
   as factories, creating `Command` objects for valid operations within that context. If an operation is invalid, the state returns an error result
   instead of a command.

3. **`Command` Interface**: Encapsulates an operation with `execute()` and `undo()` methods. All actions that modify the application's data are
   implemented as commands (e.g., `CommandLoadMap`, `CommandAddDelivery`).

4. **`CommandManager`**: Manages two stacks (`undoStack`, `redoStack`) to track command history. It orchestrates the execution and reversal of
   commands.

## Architectural Flow

```mermaid
sequenceDiagram
    participant UI as UI Components
    participant AC as AppController
    participant S as Current State
    participant C as Command
    participant CM as CommandManager
    UI ->> AC: 1. requestSomething()
    AC ->> S: 2. createSomethingCommand()
    S -->> AC: 3. CommandResult (Command or Error)
    AC ->> CM: 4. executeCommand(Command)
    CM ->> C: 5. execute()
    C ->> AC: 6. doSomething() (modifies data)
    Note over AC: May trigger a state transition
```

## State Transitions

The application moves through states based on user actions, ensuring operations are only available when appropriate.

```mermaid
graph TD
    A[Initial] -->|Load Map| B(Map Loaded)
    B -->|Load Deliveries| C{Deliveries Loaded}
    C -->|Request Selection| D[Select Intersection]
    D -->|Select Intersection| C
    B -->|Load Map| B
    C -->|Load Map| B
    C -->|Load Deliveries| C
```

## Key Benefits

- **Full Undo/Redo**: Every action is reversible.
- **Strong Encapsulation**: The UI is decoupled from business logic.
- **State Validation**: Prevents invalid operations and provides clear, contextual error messages.
- **Testability & Maintainability**: Components are self-contained and easy to manage.
