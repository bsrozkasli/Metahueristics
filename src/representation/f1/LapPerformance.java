package representation.f1;

/**
 * Represents the physical performance data for a single lap in a Formula 1 race.
 * Contains weather conditions, tire wear, tire age, and fuel load information.
 */
public class LapPerformance {
    /** Weather coefficient (0.0 = Dry, 1.0 = Rain) */
    private double weather;
    
    /** Tire wear percentage (0.0 - 1.0) */
    private double tireWear;
    
    /** Number of laps the tire has been used */
    private int tireAge;
    
    /** Fuel load in the car (kg) */
    private double fuelLoad;

    /**
     * Creates a new LapPerformance instance.
     *
     * @param weather weather coefficient (0.0-1.0)
     * @param tireWear tire wear percentage (0.0-1.0)
     * @param tireAge number of laps the tire has been used
     * @param fuelLoad fuel load in kg
     */
    public LapPerformance(double weather, double tireWear, int tireAge, double fuelLoad) {
        this.weather = weather;
        this.tireWear = tireWear;
        this.tireAge = tireAge;
        this.fuelLoad = fuelLoad;
    }

    public double getWeather() {
        return weather;
    }

    public void setWeather(double weather) {
        this.weather = weather;
    }

    public double getTireWear() {
        return tireWear;
    }

    public void setTireWear(double tireWear) {
        this.tireWear = tireWear;
    }

    public int getTireAge() {
        return tireAge;
    }

    public void setTireAge(int tireAge) {
        this.tireAge = tireAge;
    }

    public double getFuelLoad() {
        return fuelLoad;
    }

    public void setFuelLoad(double fuelLoad) {
        this.fuelLoad = fuelLoad;
    }

    /**
     * Creates a deep copy of this LapPerformance object.
     *
     * @return a new LapPerformance instance with copied values
     */
    public LapPerformance copy() {
        return new LapPerformance(weather, tireWear, tireAge, fuelLoad);
    }

    @Override
    public String toString() {
        return String.format("LapPerformance{weather=%.2f, tireWear=%.2f, tireAge=%d, fuelLoad=%.2f}",
                weather, tireWear, tireAge, fuelLoad);
    }
}

