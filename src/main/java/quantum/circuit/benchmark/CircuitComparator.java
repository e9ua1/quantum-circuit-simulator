package quantum.circuit.benchmark;

import quantum.circuit.domain.circuit.QuantumCircuit;

public class CircuitComparator {

    public ComparisonReport compare(QuantumCircuit original, QuantumCircuit optimized) {
        return new ComparisonReport(original, optimized);
    }
}
