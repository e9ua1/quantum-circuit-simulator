package quantum.circuit.domain.circuit;

import java.util.ArrayList;
import java.util.List;

public class QuantumCircuitBuilder {

    private static final String ERROR_QUBIT_COUNT_NOT_SET = "[ERROR] 큐비트 개수가 설정되지 않았습니다.";

    private Integer qubitCount;
    private final List<CircuitStep> steps;

    public QuantumCircuitBuilder() {
        this.steps = new ArrayList<>();
    }

    public QuantumCircuitBuilder withQubits(int count) {
        this.qubitCount = count;
        return this;
    }

    public QuantumCircuitBuilder addStep(CircuitStep step) {
        this.steps.add(step);
        return this;
    }

    public QuantumCircuitBuilder addSteps(List<CircuitStep> steps) {
        this.steps.addAll(steps);
        return this;
    }

    public QuantumCircuit build() {
        validateQubitCount();
        return new QuantumCircuit(qubitCount, steps);
    }

    private void validateQubitCount() {
        if (qubitCount == null) {
            throw new IllegalArgumentException(ERROR_QUBIT_COUNT_NOT_SET);
        }
    }
}
