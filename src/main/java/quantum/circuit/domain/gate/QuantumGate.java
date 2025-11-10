package quantum.circuit.domain.gate;

import quantum.circuit.domain.state.QuantumState;

public interface QuantumGate {

    void apply(QuantumState state);

    int getQubitCount();

    String getName();
}
