package quantum.circuit.domain.circuit;

public record QubitIndex(int value) {

    private static final String ERROR_NEGATIVE_INDEX = "[ERROR] 큐비트 인덱스는 0 이상이어야 합니다.";

    public QubitIndex {
        if (value < 0) {
            throw new IllegalArgumentException(ERROR_NEGATIVE_INDEX);
        }
    }

    public int getValue() {
        return value;
    }
}
