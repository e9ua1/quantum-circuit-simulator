package quantum.circuit.analyzer;

import quantum.circuit.domain.circuit.QuantumCircuit;
import quantum.circuit.domain.gate.CNOTGate;

public class EntanglementDegree {

    public static int calculate(QuantumCircuit circuit) {
        return (int) circuit.getSteps().stream()
                .flatMap(step -> step.getGates().stream())
                .filter(gate -> gate instanceof CNOTGate)
                .count();
    }
}
