package quantum.circuit.domain.gate;

import quantum.circuit.domain.circuit.QubitIndex;
import quantum.circuit.domain.state.QuantumState;

public class HadamardGate extends SingleQubitGate {

    private static final String GATE_NAME = "H";

    public HadamardGate(QubitIndex target) {
        super(target);
    }

    @Override
    public void apply(QuantumState state) {
        state.applyHadamardGate(getTarget());
    }

    @Override
    public String getName() {
        return GATE_NAME;
    }
}
