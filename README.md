# Delivrooom

## Hexagonal architecture
Architecture setup with the help of this [tuto](https://www.happycoders.eu/software-craftsmanship/hexagonal-architecture-java/)

## Conventional Commits
Follow the conventional commits specification listed [here](https://www.conventionalcommits.org/en/v1.0.0/)

## Setup

Clone the config file `config/src/main/resources/config.properties.template` to `config/src/main/resources/config.properties` and fill it with your
own values.

```bash
cp config/src/main/resources/config.properties.template config/src/main/resources/config.properties
```

## Run
Run the app with maven running the `javafx:run` goal on the `config` module after installing on the root one.
```bash
./mvnw clean install
cd config
../mvnw javafx:run
```

## GUI Architecture: State-Driven Command Factory Pattern

The application uses a sophisticated combination of **State Pattern** and **Command Pattern** to manage user interactions and application state
transitions.

### Architecture Flow

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

### Key Principles

1. **States as Command Factories**: States don't execute operations; they create commands that encapsulate operations
2. **Controller as Gateway**: Only 3 public methods: `requestCommand()`, `undoCommand()`, `redoCommand()`
3. **Commands Encapsulate Logic**: All business operations are wrapped in undoable commands
4. **Clear Method Naming**:
    - `requestX()` - Public methods for UI to request operations
    - `doX()` - Protected methods for commands to modify data

### Benefits

- ✅ **Full Undo/Redo Support**: All operations are undoable, including state transitions
- ✅ **Strong Encapsulation**: Controller has minimal public API
- ✅ **State Validation**: States control which operations are allowed
- ✅ **Testability**: Clear separation of concerns
- ✅ **Maintainability**: Easy to add new commands and states

## Implementations delivered (so far)

- When the app starts, it loads the intersection points from the xml map files and displays those on a real map.
- Then, deliveries points are loaded from another xml file and are displayed on the map. Blue points are delivery points, red points are pickup points. Tne green point is the warehouse.
- When both maps and delivery points are loaded, the route is computed and displayed on the map (for now only small maps works since we are using a fairly simple Dijkstra).
