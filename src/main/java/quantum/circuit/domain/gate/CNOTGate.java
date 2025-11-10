package quantum.circuit.domain.gate;

import java.util.Objects;

import quantum.circuit.domain.circuit.QubitIndex;
import quantum.circuit.domain.state.QuantumState;

public class CNOTGate implements QuantumGate {

    private static final String ERROR_NULL_CONTROL = "[ERROR] 제어 큐비트는 null일 수 없습니다.";
    private static final String ERROR_NULL_TARGET = "[ERROR] 타겟 큐비트는 null일 수 없습니다.";
    private static final String ERROR_SAME_QUBIT = "[ERROR] 제어 큐비트와 타겟 큐비트는 달라야 합니다.";
    private static final String GATE_NAME = "CNOT";
    private static final int TWO_QUBITS = 2;

    private final QubitIndex control;
    private final QubitIndex target;

    public CNOTGate(QubitIndex control, QubitIndex target) {
        validateNotNull(control, target);
        validateDifferent(control, target);
        this.control = control;
        this.target = target;
    }

    private void validateNotNull(QubitIndex control, QubitIndex target) {
        if (Objects.isNull(control)) {
            throw new IllegalArgumentException(ERROR_NULL_CONTROL);
        }
        if (Objects.isNull(target)) {
            throw new IllegalArgumentException(ERROR_NULL_TARGET);
        }
    }

    private void validateDifferent(QubitIndex control, QubitIndex target) {
        if (control.equals(target)) {
            throw new IllegalArgumentException(ERROR_SAME_QUBIT);
        }
    }

    @Override
    public void apply(QuantumState state) {
        state.applyCNOTGate(control, target);
    }

    @Override
    public int getQubitCount() {
        return TWO_QUBITS;
    }

    @Override
    public String getName() {
        return GATE_NAME;
    }

    public QubitIndex getControl() {
        return control;
    }

    public QubitIndex getTarget() {
        return target;
    }
}
