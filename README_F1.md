# Formula 1 (F1) Strategy Module — Quick Guide

This short guide explains what each package does, how the strategy representation works, what the model checks, and how the greedy builder and objective function are used. A tiny demo is included at the end.

---

## Packages

- representation.f1
  - Compound: tire compounds (SOFT, MEDIUM, HARD)
  - LapPerformance: per-lap physical data (weather, tireWear, tireAge, fuelLoad)
  - LapState<P extends LapPerformance>: one lap’s decision + performance
  - F1StrategyRepresentation<P extends LapPerformance>: array of LapState forming the full strategy
- problem.f1
  - F1Model: race length and feasibility checks
  - F1TotalTimeOF: objective function (minimize total time)
- alg.greedy.f1
  - F1Evaluator: interface to decide pit stop and compound
  - F1TireAgeEvaluator: simple rule based on tire age
  - F1GreedyHeuristic: builds a strategy lap by lap using an evaluator

---

## Representation

- A strategy is an array of `LapState<P>` with length `totalLaps`.
- `LapState` contains:
  - `compound`: tire compound to use for the lap
  - `pitStop`: whether a pit stop happens on the lap
  - `performance`: arbitrary per-lap data (`P` extends `LapPerformance`)
- `F1StrategyRepresentation<P>` stores the array and provides safe access and deep copy.

---

## Model (What it checks)

`F1Model` validates:
- Chromosome size equals `totalLaps`.
- Second Tire Constraint: at least two different compounds are used in the race.

Note: the method name is `isFeasable` (historical typo) and is kept for API stability.

---

## Objective

`F1TotalTimeOF` minimizes total race time using a very simple formula per lap:
- delta(compound) + degradationRate × tireAge + pitStopTime(if pit stop)
- Defaults: degradationRate = 0.05 s/lapAge, pitStopTime = 28.0 s

---

## Greedy Builder

`F1GreedyHeuristic` constructs a full strategy by asking an `F1Evaluator` each lap.
- Example evaluator `F1TireAgeEvaluator` switches from SOFT to MEDIUM once a tire-age threshold is reached.
- Implementation ensures tire age updates once per lap and fuel load never goes negative.

---

## How to run (Demo)

```java
int TOTAL_LAPS = 58;
F1Model model = new F1Model(TOTAL_LAPS);
F1TotalTimeOF of = new F1TotalTimeOF();
F1GreedyHeuristic heuristic = new F1GreedyHeuristic(new F1TireAgeEvaluator());

F1StrategyRepresentation<LapPerformance> strategy = heuristic.solve(model);
boolean feasible = model.isFeasable(strategy);
double totalTime = of.value(model, strategy);
```

---

## Extensibility

You can extend `LapPerformance` to carry more data and use it everywhere via generics:

```java
class WetLapPerformance extends LapPerformance {
  private final double aquaplaningRisk;
  WetLapPerformance(double w, double tw, int ta, double fl, double risk) {
    super(w, tw, ta, fl);
    this.aquaplaningRisk = risk;
  }
  public double getAquaplaningRisk() { return aquaplaningRisk; }
  public WetLapPerformance copy() {
    return new WetLapPerformance(getWeather(), getTireWear(), getTireAge(), getFuelLoad(), aquaplaningRisk);
  }
}
```

---

## Notes

- Method name `isFeasable` is intentional for compatibility.
- `F1TireAgeEvaluator` uses a single pit stop policy (compound-based) as a simple example.
- `F1GreedyHeuristic` clamps fuel load to non-negative and avoids duplicate tire-age changes.
