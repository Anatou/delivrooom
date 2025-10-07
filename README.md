# Delivrooom

## Hexagonal architecture
Architecture setup with the help of this [tuto](https://www.happycoders.eu/software-craftsmanship/hexagonal-architecture-java/)

## Conventional Commits
Follow the conventional commits specification listed [here](https://www.conventionalcommits.org/en/v1.0.0/)

## Setup

Clone the config file `adapter/src/main/resources/config.properties.template` to `adapter/src/main/resources/config.properties` and fill it with your
own values.

```bash
cp adapter/src/main/resources/config.properties.template adapter/src/main/resources/config.properties
```

## Run
Run the app with maven running the `javafx:run` goal on the `config` module after installing on the root one.
```bash
./mvnw clean install
cd config
../mvnw javafx:run
```
