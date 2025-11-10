package quantum.circuit.algorithm;

import quantum.circuit.domain.circuit.QuantumCircuit;
import quantum.circuit.domain.circuit.QuantumCircuitBuilder;

public abstract class QuantumAlgorithm {

    private static final String ERROR_INVALID_QUBIT_COUNT = "큐비트 개수가 올바르지 않습니다.";

    /**
     * Template Method: 알고리즘의 공통 흐름을 정의
     */
    public final QuantumCircuit build(int qubitCount) {
        validateQubitCount(qubitCount);

        QuantumCircuitBuilder builder = createBuilder(qubitCount);
        prepareInitialState(builder);
        applyMainAlgorithm(builder);

        return builder.build();
    }

    private void validateQubitCount(int qubitCount) {
        if (qubitCount != getRequiredQubits()) {
            throw new IllegalArgumentException(ERROR_INVALID_QUBIT_COUNT);
        }
    }

    private QuantumCircuitBuilder createBuilder(int qubitCount) {
        return new QuantumCircuitBuilder().withQubits(qubitCount);
    }

    /**
     * Hook Method: 초기 상태 준비 (하위 클래스에서 필요시 오버라이드)
     */
    protected void prepareInitialState(QuantumCircuitBuilder builder) {
        // 기본적으로 |0⟩ 상태이므로 아무 작업 안 함
    }

    /**
     * Abstract Method: 각 알고리즘의 핵심 로직 (하위 클래스에서 구현 필수)
     */
    protected abstract void applyMainAlgorithm(QuantumCircuitBuilder builder);

    /**
     * 알고리즘 이름
     */
    public abstract String getName();

    /**
     * 알고리즘 설명
     */
    public abstract String getDescription();

    /**
     * 필요한 큐비트 개수
     */
    public abstract int getRequiredQubits();
}
