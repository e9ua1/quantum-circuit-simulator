package quantum.circuit.domain.gate;

import java.util.Objects;
import java.util.Set;

import quantum.circuit.domain.circuit.QubitIndex;
import quantum.circuit.domain.state.QuantumState;

public class HadamardGate implements QuantumGate {

    private static final String ERROR_NULL_TARGET = "[ERROR] 타겟 큐비트는 null일 수 없습니다.";
    private static final String GATE_NAME = "H";
    private static final int SINGLE_QUBIT = 1;

    private final QubitIndex target;

    public HadamardGate(QubitIndex target) {
        validateNotNull(target);
        this.target = target;
    }

    private void validateNotNull(QubitIndex target) {
        if (Objects.isNull(target)) {
            throw new IllegalArgumentException(ERROR_NULL_TARGET);
        }
    }

    @Override
    public void apply(QuantumState state) {
        state.applyHadamardGate(target);
    }

    @Override
    public int getQubitCount() {
        return SINGLE_QUBIT;
    }

    @Override
    public String getName() {
        return GATE_NAME;
    }

    public QubitIndex getTarget() {
        return target;
    }

    @Override
    public Set<QubitIndex> getAffectedQubits() {
        return Set.of(target);
    }
}
