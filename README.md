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


## Implementations delivered (so far)

- When the app starts, it loads the intersection points from the xml map files and displays those on a real map.
- Then, deliveries points are loaded from another xml file and are displayed on the map. Blue points are delivery points, red points are pickup points. Tne green point is the warehouse.
- When both maps and delivery points are loaded, the route is computed and displayed on the map (for now only small maps works since we are using a fairly simple Dijkstra).
