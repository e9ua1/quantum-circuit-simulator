package quantum.circuit.domain.circuit;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import quantum.circuit.domain.state.QuantumState;

public class QuantumCircuit {

    private static final String ERROR_INVALID_QUBIT_COUNT = "[ERROR] 큐비트 개수는 1 이상이어야 합니다.";
    private static final String ERROR_NULL_STEPS = "[ERROR] Step 리스트는 null일 수 없습니다.";

    private final int qubitCount;
    private final List<CircuitStep> steps;

    public QuantumCircuit(int qubitCount, List<CircuitStep> steps) {
        validateQubitCount(qubitCount);
        validateSteps(steps);
        this.qubitCount = qubitCount;
        this.steps = new ArrayList<>(steps);
    }

    private void validateQubitCount(int qubitCount) {
        if (qubitCount < 1) {
            throw new IllegalArgumentException(ERROR_INVALID_QUBIT_COUNT);
        }
    }

    private void validateSteps(List<CircuitStep> steps) {
        if (Objects.isNull(steps)) {
            throw new IllegalArgumentException(ERROR_NULL_STEPS);
        }
    }

    public QuantumState execute() {
        QuantumState state = QuantumState.initialize(qubitCount);
        for (CircuitStep step : steps) {
            step.applyTo(state);
        }
        return state;
    }

    public int getQubitCount() {
        return qubitCount;
    }

    public List<CircuitStep> getSteps() {
        return List.copyOf(steps);
    }

    public int getStepCount() {
        return steps.size();
    }

    public boolean isEmpty() {
        return steps.isEmpty();
    }

    public int getTotalGateCount() {
        return steps.stream()
                .mapToInt(CircuitStep::getGateCount)
                .sum();
    }

    public int getDepth() {
        return steps.size();
    }
}
