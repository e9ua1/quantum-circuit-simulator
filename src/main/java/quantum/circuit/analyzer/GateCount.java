package quantum.circuit.analyzer;

import quantum.circuit.domain.circuit.QuantumCircuit;

public class GateCount {

    public static int calculate(QuantumCircuit circuit) {
        return circuit.getSteps().stream()
                .mapToInt(step -> step.getGateCount())
                .sum();
    }
}
