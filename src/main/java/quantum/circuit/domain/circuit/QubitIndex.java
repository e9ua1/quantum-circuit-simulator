package quantum.circuit.domain.circuit;

import java.util.Objects;

public class QubitIndex {

    private static final String ERROR_NEGATIVE_INDEX = "[ERROR] 큐비트 인덱스는 0 이상이어야 합니다.";

    private final int value;

    public QubitIndex(int value) {
        validateNonNegative(value);
        this.value = value;
    }

    private void validateNonNegative(int value) {
        if (value < 0) {
            throw new IllegalArgumentException(ERROR_NEGATIVE_INDEX);
        }
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        QubitIndex that = (QubitIndex) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
