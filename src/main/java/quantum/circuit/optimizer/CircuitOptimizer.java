package quantum.circuit.optimizer;

import quantum.circuit.domain.circuit.QuantumCircuit;

public interface CircuitOptimizer {

    QuantumCircuit optimize(QuantumCircuit circuit);

    String getOptimizationName();
}
