package quantum.circuit.domain.gate;

import java.util.Objects;
import java.util.Set;

import quantum.circuit.domain.circuit.QubitIndex;

public abstract class SingleQubitGate implements QuantumGate {

    private static final String ERROR_NULL_TARGET = "[ERROR] 타겟 큐비트는 null일 수 없습니다.";
    private static final int SINGLE_QUBIT = 1;

    private final QubitIndex target;

    protected SingleQubitGate(QubitIndex target) {
        validateNotNull(target);
        this.target = target;
    }

    private void validateNotNull(QubitIndex target) {
        if (Objects.isNull(target)) {
            throw new IllegalArgumentException(ERROR_NULL_TARGET);
        }
    }

    @Override
    public int getQubitCount() {
        return SINGLE_QUBIT;
    }

    public QubitIndex getTarget() {
        return target;
    }

    @Override
    public Set<QubitIndex> getAffectedQubits() {
        return Set.of(target);
    }
}
