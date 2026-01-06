# Gatling Performance Tests

![Java](https://img.shields.io/badge/Java-17-007396?logo=java)
![Maven](https://img.shields.io/badge/Maven-Build-C71A36?logo=apachemaven)
![Gatling](https://img.shields.io/badge/Gatling-3.x-FF6F00?logo=scala)
![GitHub Actions](https://img.shields.io/badge/GitHub%20Actions-CI-2088FF?logo=github-actions)

Performance testing project for a Video Games API using Gatling (Java). Includes a GitHub Actions CI workflow, Maven configuration, and a sample scenario that authenticates, creates a game, and queries data.

This project is based on the public VideoGameDB API. Explore the API here:
https://videogamedb.uk/swagger-ui/index.html#/

## Technologies
- Java 17
- Maven with `gatling-maven-plugin`
- Gatling 3.13.5 (Highcharts reports)
- GitHub Actions for CI

## Repository structure
```text
Gatling/
├─ pom.xml
├─ README.md
├─ .gitignore
├─ .github/
│  └─ workflows/
│     └─ ci.yaml
└─ src/
   └─ test/
      ├─ java/
      │  ├─ config/
      │  │  └─ Config.java
      │  ├─ requests/
      │  │  └─ Requests.java
      │  └─ simulations/
      │     └─ UserSimulation.java
      └─ resources/
         ├─ bodies/
         │  └─ newGame.json
         └─ logback.xml
```

## What the scenario does
- requests.Requests
  - authenticate: logs in and saves `jwtToken` in the session.
  - createNewGame: creates a game with body `bodies/newGame.json` and saves `createdGameId`.
  - getAllGames: fetches all games.
  - getSingleGame: fetches a game by `createdGameId` (if used in the scenario).
- simulations.UserSimulation
  - Defines the HTTP protocol (`baseUrl`, headers).
  - Scenario: authenticate → create game → get list.
  - Allows parameterization via `-Dusers` and `-DrampDuration`.

## Requirements
- Java 17
- Maven 3.8+
- Connectivity to the target API (default: `https://videogamedb.uk/api`)

## How to run locally
- Run the default scenario `simulations.UserSimulation`:
```bash
mvn clean gatling:test
```

- Parameterize users, ramp duration and base URL:
```bash
mvn clean gatling:test \
  -Dusers=10 \
  -DrampDuration=20 \
  -DbaseUrl=https://videogamedb.uk/api
```

- Available JVM/system properties (with defaults):
  - `-DbaseUrl`: API base URL. Default: `https://videogamedb.uk/api` (see `config/Config.java`).
  - `-Dusers`: number of concurrent users. Default: `5`.
  - `-DrampDuration`: ramp-up duration in seconds. Default: `10`.
  - You can add more as needed (e.g., `-DgamesToCreate` if you expand the scenario).

## Reports
- After the run, Gatling generates an HTML report in `target/gatling/<simulation-timestamp>/index.html`.
- Open `index.html` in your browser to analyze latency, percentiles, and success rates.

## CI with GitHub Actions
Workflow: `.github/workflows/ci.yaml`
- Triggers on `push`/`pull_request` to `main`, and also manually (`workflow_dispatch`).
- Inputs for manual runs:
  - `users` (default `5`)
  - `rampDuration` (default `10`)
- Artifact: uploads the Gatling report from `target/gatling`.

Run it manually from the "Actions" tab by selecting the workflow, branch, and inputs.

## Key configuration
- `pom.xml`: uses `gatling-maven-plugin` targeting `simulations.UserSimulation` and adds JVM flags for compatibility:
  - `--add-opens` (base module openings)
  - `--enable-native-access=ALL-UNNAMED`
  - `-Dlogback.configurationFile=src/test/resources/logback.xml`
- Logging: configured in `src/test/resources/logback.xml`.
- Creation body: `src/test/resources/bodies/newGame.json` uses the `#{gameName}` session variable.

## Extend the project
- Add more requests under `requests/` and compose scenarios in `simulations/`.
- Use feeders for dynamic data (IDs, payloads, etc.).
- Add global assertions in `setUp(...).assertions(...)` for SLOs.

## Troubleshooting
- If you see module opening errors on Java 17, the `pom.xml` already includes the necessary `--add-opens` flags.
- Ensure the target API is reachable and the JWT token is valid.
- If the report is missing, check the Maven output and `target/gatling` folder.

---
Would you like me to add more scenarios (e.g., update and delete) or a CSV feeder for IDs?
