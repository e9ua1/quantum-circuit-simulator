package quantum.circuit.domain.circuit;

import java.util.List;

import quantum.circuit.domain.state.QuantumState;

public class QuantumCircuit {

    private static final String ERROR_INVALID_QUBIT_COUNT = "[ERROR] 큐비트 개수는 1 이상 10 이하여야 합니다.";
    private static final int MIN_QUBIT_COUNT = 1;
    private static final int MAX_QUBIT_COUNT = 10;

    private final int qubitCount;
    private final List<CircuitStep> steps;

    public QuantumCircuit(int qubitCount, List<CircuitStep> steps) {
        validateQubitCount(qubitCount);
        this.qubitCount = qubitCount;
        this.steps = List.copyOf(steps);
    }

    private void validateQubitCount(int count) {
        if (count < MIN_QUBIT_COUNT || count > MAX_QUBIT_COUNT) {
            throw new IllegalArgumentException(ERROR_INVALID_QUBIT_COUNT);
        }
    }

    public QuantumState execute() {
        QuantumState state = QuantumState.initialize(qubitCount);
        steps.forEach(step -> step.applyTo(state));
        return state;
    }

    public int getQubitCount() {
        return qubitCount;
    }

    public int getStepCount() {
        return steps.size();
    }

    public List<CircuitStep> getSteps() {
        return steps;
    }
}
