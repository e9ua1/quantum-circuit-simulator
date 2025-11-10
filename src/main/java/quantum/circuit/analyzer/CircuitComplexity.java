package quantum.circuit.analyzer;

import quantum.circuit.domain.circuit.QuantumCircuit;
import quantum.circuit.domain.gate.CNOTGate;
import quantum.circuit.domain.gate.QuantumGate;

public class CircuitComplexity {

    private static final int SINGLE_QUBIT_GATE_WEIGHT = 1;
    private static final int TWO_QUBIT_GATE_WEIGHT = 2;

    public static int calculate(QuantumCircuit circuit) {
        return circuit.getSteps().stream()
                .flatMap(step -> step.getGates().stream())
                .mapToInt(CircuitComplexity::getGateWeight)
                .sum();
    }

    private static int getGateWeight(QuantumGate gate) {
        if (gate instanceof CNOTGate) {
            return TWO_QUBIT_GATE_WEIGHT;
        }
        return SINGLE_QUBIT_GATE_WEIGHT;
    }
}
