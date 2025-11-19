package quantum.circuit.domain.state;

import quantum.circuit.domain.circuit.QubitIndex;
import quantum.circuit.domain.state.executor.QuantumExecutor;
import quantum.circuit.infrastructure.executor.StrangeQuantumExecutor;

import java.util.Map;

public class QuantumState {

    private static final String ERROR_INVALID_QUBIT_COUNT = "[ERROR] 큐비트 개수는 1 이상 10 이하여야 합니다.";
    private static final String ERROR_INVALID_INDEX = "[ERROR] 큐비트 인덱스가 범위를 벗어났습니다.";
    private static final int MIN_QUBIT_COUNT = 1;
    private static final int MAX_QUBIT_COUNT = 10;

    private final int qubitCount;
    private final QuantumExecutor executor;

    private QuantumState(int qubitCount, QuantumExecutor executor) {
        validateQubitCount(qubitCount);
        this.qubitCount = qubitCount;
        this.executor = executor;
    }

    public static QuantumState initialize(int qubitCount) {
        return new QuantumState(qubitCount, new StrangeQuantumExecutor(qubitCount));
    }

    public static QuantumState initialize(int qubitCount, QuantumExecutor executor) {
        return new QuantumState(qubitCount, executor);
    }

    private void validateQubitCount(int count) {
        if (count < MIN_QUBIT_COUNT || count > MAX_QUBIT_COUNT) {
            throw new IllegalArgumentException(ERROR_INVALID_QUBIT_COUNT);
        }
    }

    public void applyXGate(QubitIndex target) {
        validateIndex(target);
        executor.applyXGate(target);
    }

    public void applyHadamardGate(QubitIndex target) {
        validateIndex(target);
        executor.applyHadamardGate(target);
    }

    public void applyZGate(QubitIndex target) {
        validateIndex(target);
        executor.applyZGate(target);
    }

    public void applyCNOTGate(QubitIndex control, QubitIndex target) {
        validateIndex(control);
        validateIndex(target);
        executor.applyCNOTGate(control, target);
    }

    private void validateIndex(QubitIndex index) {
        if (index.getValue() >= qubitCount) {
            throw new IllegalArgumentException(ERROR_INVALID_INDEX);
        }
    }

    public Probability getProbabilityOfOne(QubitIndex index) {
        validateIndex(index);
        return executor.getProbabilityOfOne(index);
    }

    public MeasurementResult measure(QubitIndex index) {
        validateIndex(index);
        return executor.measure(index);
    }

    public Map<String, Double> getBasisStateProbabilities() {
        return executor.getBasisStateProbabilities();
    }

    public int getQubitCount() {
        return qubitCount;
    }
}
