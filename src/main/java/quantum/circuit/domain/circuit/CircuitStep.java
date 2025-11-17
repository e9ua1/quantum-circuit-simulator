package quantum.circuit.domain.circuit;

import java.util.List;
import java.util.Objects;

import quantum.circuit.domain.gate.QuantumGate;
import quantum.circuit.domain.state.QuantumState;

public class CircuitStep {

    private static final String ERROR_NULL_GATES = "[ERROR] 게이트 리스트는 null일 수 없습니다.";
    private static final String ERROR_EMPTY_GATES = "[ERROR] 게이트 리스트는 비어있을 수 없습니다.";
    private static final String ERROR_NOT_SINGLE_GATE = "[ERROR] 이 Step은 단일 게이트 Step이 아닙니다.";

    private final List<QuantumGate> gates;

    public CircuitStep(List<QuantumGate> gates) {
        validateGates(gates);
        this.gates = List.copyOf(gates);
    }

    private void validateGates(List<QuantumGate> gates) {
        if (Objects.isNull(gates)) {
            throw new IllegalArgumentException(ERROR_NULL_GATES);
        }
        if (gates.isEmpty()) {
            throw new IllegalArgumentException(ERROR_EMPTY_GATES);
        }
    }

    public void applyTo(QuantumState state) {
        gates.forEach(gate -> gate.apply(state));
    }

    public List<QuantumGate> getGates() {
        return gates;
    }

    public int getGateCount() {
        return gates.size();
    }

    public boolean isSingleGateStep() {
        return gates.size() == 1;
    }

    public QuantumGate getSingleGate() {
        if (!isSingleGateStep()) {
            throw new IllegalStateException(ERROR_NOT_SINGLE_GATE);
        }
        return gates.get(0);
    }
}
