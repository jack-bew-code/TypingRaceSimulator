# TypingRaceSimulator

Object Oriented Programming Project — ECS414U

## Project Structure

```
TypingRaceSimulator/
├── Part1/    # Textual simulation (Java, command-line)
└── Part2/    # GUI simulation (Java Swing)
```

## Part 1 — Textual Simulation

### How to compile

```bash
cd Part1
javac Typist.java TypingRace.java
```

### How to run

The race is started by calling `startRace()` on a `TypingRace` object.
A simple way to test this is to add a `main` method to `TypingRace`, for example:

```java
public static void main(String[] args) {
    TypingRace race = new TypingRace(40);
    race.addTypist(new Typist('①', "TURBOFINGERS", 0.85), 1);
    race.addTypist(new Typist('②', "QWERTY_QUEEN",  0.60), 2);
    race.addTypist(new Typist('③', "HUNT_N_PECK",   0.30), 3);
    race.startRace();
}
```

Then run:

```bash
java TypingRace
```

Bugs Fixed by Jack Bew:

- fixed mistype display in the lane
- corrected tie handling in race finishing logic
- rounded accuracy values for cleaner output


Features added by Jack Bew:

- typist accuracy changes during the race
- burnout reduces accuracy
- winner/tie outcomes increase accuracy
- mistype and burnout markers are shown on screen
- accuracy is rounded to 2 decimal places
- WPM is estimated during the race


## Part 2 — GUI Simulation

### How to Comple:
```bash
cd Part2
javac *.java
```

### How to Run:
```bash
java RaceConfigGUI
```

GUI Features Implemented by Jack Bew:
- Interactive Configuration: Custom passages, adjustable seat counts, and difficulty modifier(Autocorrect, Caffeine Mode, Night Shift).
- Customisable Typists: Individual settings for Typing Style, Keyboard Type, Lane Color, and Accessories (Wrist Support, Energy Drinks, Headphones) with a built-in Attribute Guide.
- Live Race Screen: Smooth simulation using javax.swing.Timer and dynamic text highlighting using JTextPane and StyledDocument.
- Post-Race Hub (Tabbed Interface):
- Analytics: Calculates actual WPM, True Accuracy %, and burnout tracking.
- Comparison View: Visual bar chart comparing personal bests using JProgressBar.
- Global Leaderboard: Points-based ranking system that persists across races, featuring dynamic titles and badges based on win streaks and clean races.
## Dependencies

- Java Development Kit (JDK) 11 or higher
- No external libraries required for Part 1
- Java Swing (included in standard JDK) is used for Part 2. No external libraries are required.