package quantum.circuit.domain.gate;

import quantum.circuit.domain.circuit.QubitIndex;
import quantum.circuit.domain.state.QuantumState;

public class PauliZGate extends SingleQubitGate {

    private static final String GATE_NAME = "Z";

    public PauliZGate(QubitIndex target) {
        super(target);
    }

    @Override
    public void apply(QuantumState state) {
        state.applyZGate(getTarget());
    }

    @Override
    public String getName() {
        return GATE_NAME;
    }
}
