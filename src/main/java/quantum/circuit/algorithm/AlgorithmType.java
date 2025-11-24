package quantum.circuit.algorithm;

import java.util.Arrays;
import java.util.function.Supplier;

public enum AlgorithmType {

    BELL_STATE(
            "Bell State",
            "2큐비트 최대 얽힘 상태를 생성합니다. H 게이트로 중첩 상태를 만든 후 CNOT 게이트로 얽힘 상태를 생성합니다.",
            2,
            BellStateAlgorithm::new
    ),
    GHZ_STATE(
            "GHZ State",
            "3큐비트 GHZ(Greenberger-Horne-Zeilinger) 얽힘 상태를 생성합니다. H 게이트로 중첩을 만든 후 연속된 CNOT 게이트로 3큐비트 얽힘을 생성합니다.",
            3,
            GHZStateAlgorithm::new
    ),
    QFT(
            "QFT",
            "양자 푸리에 변환(Quantum Fourier Transform)을 수행합니다. 2큐비트에 대한 간소화된 QFT를 구현합니다.",
            2,
            QFTAlgorithm::new
    ),
    GROVER(
            "Grover's Search",
            "Grover의 검색 알고리즘을 수행합니다. 2큐비트에 대한 간소화된 버전으로, 특정 상태를 증폭합니다.",
            2,
            GroverAlgorithm::new
    ),
    DEUTSCH_JOZSA(
            "Deutsch-Jozsa",
            "Deutsch-Jozsa 알고리즘을 수행합니다. 함수가 상수(constant)인지 균형(balanced)인지를 단 한 번의 질의로 판별합니다.",
            2,
            DeutschJozsaAlgorithm::new
    );

    private final String displayName;
    private final String description;
    private final int requiredQubits;
    private final Supplier<QuantumAlgorithm> supplier;

    AlgorithmType(String displayName, String description, int requiredQubits, Supplier<QuantumAlgorithm> supplier) {
        this.displayName = displayName;
        this.description = description;
        this.requiredQubits = requiredQubits;
        this.supplier = supplier;
    }

    public static AlgorithmType from(String name) {
        String normalizedName = normalizeName(name);
        return Arrays.stream(values())
                .filter(type -> type.name().equals(normalizedName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 알고리즘입니다: " + name));
    }

    private static String normalizeName(String name) {
        String numberToName = convertNumberToName(name.trim());
        if (numberToName != null) {
            return numberToName;
        }

        return name.trim()
                .replace(" ", "_")
                .replace("-", "_")
                .toUpperCase();
    }

    private static String convertNumberToName(String input) {
        return switch (input) {
            case "1" -> "BELL_STATE";
            case "2" -> "GHZ_STATE";
            case "3" -> "QFT";
            case "4" -> "GROVER";
            case "5" -> "DEUTSCH_JOZSA";
            default -> null;
        };
    }

    public QuantumAlgorithm create() {
        return supplier.get();
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public int getRequiredQubits() {
        return requiredQubits;
    }
}
