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

## GUI Architecture

The application's GUI is built on a **State-Driven Command Factory** pattern, which combines the State and Command design patterns for robust state
management and full undo/redo capabilities.

For a detailed explanation, see [ARCHITECTURE.md](ARCHITECTURE.md).

## Features

- **Map Visualization**: Loads and displays intersections from XML map files onto an interactive map.
- **Delivery Management**: Loads delivery points from XML files and visualizes them on the map.
- **Tour Calculation**: Computes and displays optimized delivery routes.
- **Undo/Redo**: All actions are fully reversible.
