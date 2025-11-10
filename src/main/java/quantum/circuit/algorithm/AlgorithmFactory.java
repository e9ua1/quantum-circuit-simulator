package quantum.circuit.algorithm;

import java.util.List;

public class AlgorithmFactory {

    private static final String ERROR_NULL_OR_EMPTY = "알고리즘 이름은 null이거나 비어있을 수 없습니다.";
    private static final String ERROR_UNSUPPORTED_ALGORITHM = "지원하지 않는 알고리즘입니다: ";

    public QuantumAlgorithm create(String algorithmName) {
        validateAlgorithmName(algorithmName);

        String upperName = algorithmName.toUpperCase();

        return switch (upperName) {
            case "BELL_STATE" -> new BellStateAlgorithm();
            case "GHZ_STATE" -> new GHZStateAlgorithm();
            case "QFT" -> new QFTAlgorithm();
            case "GROVER" -> new GroverAlgorithm();
            default -> throw new IllegalArgumentException(ERROR_UNSUPPORTED_ALGORITHM + algorithmName);
        };
    }

    private void validateAlgorithmName(String algorithmName) {
        if (algorithmName == null || algorithmName.isBlank()) {
            throw new IllegalArgumentException(ERROR_NULL_OR_EMPTY);
        }
    }

    public List<String> getAvailableAlgorithms() {
        return List.of(
                "BELL_STATE",
                "GHZ_STATE",
                "QFT",
                "GROVER"
        );
    }
}
