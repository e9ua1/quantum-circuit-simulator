package quantum.circuit.domain.gate;

import java.util.Set;

import quantum.circuit.domain.circuit.QubitIndex;
import quantum.circuit.domain.state.QuantumState;

public interface QuantumGate {

    void apply(QuantumState state);

    int getQubitCount();

    String getName();

    Set<QubitIndex> getAffectedQubits();
}
