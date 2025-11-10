package quantum.circuit.domain.circuit;

import java.util.List;
import java.util.Objects;

import quantum.circuit.domain.gate.QuantumGate;
import quantum.circuit.domain.state.QuantumState;

public class CircuitStep {

    private static final String ERROR_NULL_GATES = "[ERROR] 게이트 리스트는 null일 수 없습니다.";
    private static final String ERROR_EMPTY_GATES = "[ERROR] Step은 최소 하나의 게이트를 포함해야 합니다.";

    private final List<QuantumGate> gates;

    public CircuitStep(List<QuantumGate> gates) {
        validateNotNull(gates);
        validateNotEmpty(gates);
        this.gates = List.copyOf(gates);
    }

    private void validateNotNull(List<QuantumGate> gates) {
        if (Objects.isNull(gates)) {
            throw new IllegalArgumentException(ERROR_NULL_GATES);
        }
    }

    private void validateNotEmpty(List<QuantumGate> gates) {
        if (gates.isEmpty()) {
            throw new IllegalArgumentException(ERROR_EMPTY_GATES);
        }
    }

    public void applyTo(QuantumState state) {
        gates.forEach(gate -> gate.apply(state));
    }

    public int getGateCount() {
        return gates.size();
    }

    public List<QuantumGate> getGates() {
        return gates;
    }
}
