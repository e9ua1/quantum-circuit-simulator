package quantum.circuit.algorithm;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AlgorithmFactory {

    private static final String ERROR_NULL_OR_EMPTY = "알고리즘 이름은 null이거나 비어있을 수 없습니다.";

    public QuantumAlgorithm create(String algorithmName) {
        validateAlgorithmName(algorithmName);
        AlgorithmType type = AlgorithmType.from(algorithmName);
        return type.create();
    }

    private void validateAlgorithmName(String algorithmName) {
        if (algorithmName == null || algorithmName.isBlank()) {
            throw new IllegalArgumentException(ERROR_NULL_OR_EMPTY);
        }
    }

    public List<String> getAvailableAlgorithms() {
        return Arrays.stream(AlgorithmType.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    public List<AlgorithmType> getAlgorithmTypes() {
        return Arrays.asList(AlgorithmType.values());
    }
}
