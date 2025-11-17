package quantum.circuit.domain.gate;

import quantum.circuit.domain.circuit.QubitIndex;
import quantum.circuit.domain.state.QuantumState;

public class PauliXGate extends SingleQubitGate {

    private static final String GATE_NAME = "X";

    public PauliXGate(QubitIndex target) {
        super(target);
    }

    @Override
    public void apply(QuantumState state) {
        state.applyXGate(getTarget());
    }

    @Override
    public String getName() {
        return GATE_NAME;
    }
}
