# Bowling Alley Simulation

A Java Swing bowling alley management simulation, refactored to remove code smells and extended with MySQL persistence, score queries, pause/resume and scoreboard reactions.

![Java](https://img.shields.io/badge/Java-ED8B00?style=flat-square&logo=openjdk&logoColor=white)
![Swing](https://img.shields.io/badge/GUI-Swing-5382A1?style=flat-square)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=mysql&logoColor=white)

## Overview

The project starts from a legacy bowling alley codebase (originally written around 2003) and treats it as a software engineering exercise: analyse the design, refactor it and add features.

- **Refactoring:** code smells were identified with the Metrics2 plugin for Eclipse and then addressed. Smells targeted included conditional complexity, feature envy, indecent exposure, large class, dead code, and primitive obsession / combinatorial explosion. As part of this, unused classes such as `Alley` and `LaneEventInterface` were removed and score calculation was pulled out of `Lane` into its own `ScoreCount` class.
- **Persistence:** bowler records and score history, previously stored in flat `.DAT` files, are now read from and written to a MySQL database through JDBC.
- **New features:** a score query window, pausing and resuming a lane, a penalty rule for consecutive gutter balls, and animated GIF reactions on the scoreboard.

The simulation is event driven: the control desk, lanes and pinsetters publish events to their views using the observer pattern, and each lane runs on its own thread.

## Features

- **Control desk:** add parties, assign queued parties to free lanes, view lane status and open score queries.
- **New patron registration:** create a bowler (nickname, full name, email), stored in the `bowlers` table.
- **Parties and queueing:** group up to 6 bowlers into a party. If all 3 lanes are busy, the party waits in a queue.
- **Pinsetter view:** shows which pins fall on each throw. The pins are re-racked after two throws.
- **Scoreboard:** standard bowling scoring (a spare adds the next ball, a strike adds the next two), plus a penalty when a bowler throws two consecutive gutter balls in a frame.
- **Scoreboard reactions:** an animated GIF is shown next to a frame's score, picked by how much the cumulative score changed.
- **Pause / resume:** each lane can be paused and resumed from its lane view (for example to simulate maintenance).
- **Score queries:** top and lowest score overall, highest and lowest score for a selected player, and the players holding the highest and lowest scores.
- **End-of-game flow:** prompt to play again, and an end-game report that can be printed.

## Tech stack

- Java (Swing / AWT for the GUI)
- MySQL via JDBC (`mysql-connector-java-8.0.28.jar` is included in the repository)
- Eclipse with the Metrics2 plugin, used for the code-smell analysis

## Project structure

```
Bowling-Alley-Simulation/
├── drive.java                  # entry point: 3 lanes, max 6 patrons per party
├── ControlDesk*.java           # control desk model, events, observer/subscriber
├── ControlDeskView.java        # main window (Add Party, Show Scores, Assign Lanes, ...)
├── Lane.java, LaneView.java, LaneStatusView.java
├── Pinsetter*.java, PinSetterView.java
├── ScoreCount.java             # score calculation, extracted from Lane
├── ShowScores.java             # score query window (MySQL)
├── BowlerFile.java             # bowler persistence (MySQL)
├── ScoreHistoryFile.java       # score history persistence (MySQL)
├── AddPartyView.java, NewPatronView.java, EndGamePrompt.java, EndGameReport.java
├── Bowler.java, Party.java, Queue.java, Score.java
├── images/                     # logo and scoreboard reaction GIFs
├── BOWLERS.DAT, SCOREHISTORY.DAT   # original sample data from the legacy flat-file version
└── mysql-connector-java-8.0.28.jar
```

## Getting started

### Prerequisites

- JDK 8 or later
- A running MySQL server

### 1. Create the database

The code connects to a database named `unit2` on `localhost:3306` and uses two tables. The repository does not include a schema script, so the one below is inferred from the queries in the code: column order matters, and the `nick` and `score` names are used directly. Adjust the types if needed.

```sql
CREATE DATABASE unit2;
USE unit2;

CREATE TABLE bowlers (
  nick      VARCHAR(50) PRIMARY KEY,
  full_name VARCHAR(100),
  email     VARCHAR(100)
);

CREATE TABLE score_history (
  nick  VARCHAR(50),
  date  VARCHAR(50),
  score INT
);
```

You can seed `bowlers` from the sample rows in `BOWLERS.DAT` (tab separated: nickname, full name, email).

### 2. Configure the connection

The JDBC URL, user and password are hard-coded in `BowlerFile.java`, `ScoreHistoryFile.java` and `ShowScores.java`. Update each `DriverManager.getConnection(...)` call to match your local MySQL user and password.

### 3. Build and run

From the repository root:

```bash
# Windows
javac -cp ".;mysql-connector-java-8.0.28.jar" *.java
java  -cp ".;mysql-connector-java-8.0.28.jar" drive

# macOS / Linux
javac -cp ".:mysql-connector-java-8.0.28.jar" *.java
java  -cp ".:mysql-connector-java-8.0.28.jar" drive
```

Run from the repository root so the `images/` folder can be found on the classpath.

## Usage

1. Use **Add Party**, then add existing bowlers or register a **New Patron**, and confirm the party.
2. Click **Assign Lanes** to move queued parties onto free lanes.
3. From the lane status panel, open a lane's **scoreboard** or **pinsetter** view, and pause or resume the lane.
4. When a game ends, choose whether to play again and print the end-game report.
5. Click **Show Scores** to run score queries against the recorded history.

## Context

Built as a team project for a software engineering course at IIIT Hyderabad (2022), with contributions from Mukul Kumar, Siddhant Chandra Kulshrestha, Sai Varun Reddy Bhavanam and [vishal3vp3pawar](https://github.com/vishal3vp3pawar).

---

Made by [Mukul Kumar](https://mkk96.github.io) · [GitHub](https://github.com/mkk96) · [LinkedIn](https://www.linkedin.com/in/mukul-kumar-60322616b/)
