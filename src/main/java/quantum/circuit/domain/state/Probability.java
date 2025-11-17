package quantum.circuit.domain.state;

import java.util.Objects;

public class Probability {

    private static final String ERROR_INVALID_RANGE = "[ERROR] 확률은 0.0 이상 1.0 이하여야 합니다.";
    private static final double MIN_VALUE = 0.0;
    private static final double MAX_VALUE = 1.0;
    private static final double PERCENTAGE_MULTIPLIER = 100.0;

    private final double value;

    public Probability(double value) {
        validateRange(value);
        this.value = value;
    }

    private void validateRange(double value) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Probability that = (Probability) o;
        return Double.compare(that.value, value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
