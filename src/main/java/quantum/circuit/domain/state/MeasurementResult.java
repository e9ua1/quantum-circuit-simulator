package quantum.circuit.domain.state;

import java.util.Arrays;

public enum MeasurementResult {

    ZERO(0),
    ONE(1);

    private static final String ERROR_INVALID_VALUE = "[ERROR] 측정 결과는 0 또는 1이어야 합니다.";

    private final int value;

    MeasurementResult(int value) {
        this.value = value;
    }

    public static MeasurementResult from(int value) {
        return Arrays.stream(values())
                .filter(result -> result.value == value)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(ERROR_INVALID_VALUE));
    }

    public int toInt() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
