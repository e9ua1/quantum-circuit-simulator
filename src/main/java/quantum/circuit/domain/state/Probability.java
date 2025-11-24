package quantum.circuit.domain.state;

public record Probability(double value) {

    private static final String ERROR_INVALID_RANGE = "[ERROR] 확률은 0.0 이상 1.0 이하여야 합니다.";
    private static final double MIN_VALUE = 0.0;
    private static final double MAX_VALUE = 1.0;
    private static final double PERCENTAGE_MULTIPLIER = 100.0;

    public Probability {
        if (value < MIN_VALUE || value > MAX_VALUE) {
            throw new IllegalArgumentException(ERROR_INVALID_RANGE);
        }
    }

    public double getValue() {
        return value;
    }

    public double toPercentage() {
        return value * PERCENTAGE_MULTIPLIER;
    }
}
