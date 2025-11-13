package quantum.circuit.analyzer;

import quantum.circuit.domain.circuit.QuantumCircuit;

public class CircuitDepth {

    public static int calculate(QuantumCircuit circuit) {
        return circuit.getDepth();
    }
}
