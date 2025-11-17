package quantum.circuit.analyzer.metric;

import quantum.circuit.domain.circuit.QuantumCircuit;
import quantum.circuit.domain.gate.CNOTGate;
import quantum.circuit.domain.gate.QuantumGate;

public class ComplexityMetric implements CircuitMetric {

    private static final String METRIC_NAME = "Circuit Complexity";
    private static final int SINGLE_QUBIT_GATE_WEIGHT = 1;
    private static final int TWO_QUBIT_GATE_WEIGHT = 2;

    @Override
    public int calculate(QuantumCircuit circuit) {
        return circuit.getSteps().stream()
                .flatMap(step -> step.getGates().stream())
                .mapToInt(this::getGateWeight)
                .sum();
    }

    private int getGateWeight(QuantumGate gate) {
        if (gate instanceof CNOTGate) {
            return TWO_QUBIT_GATE_WEIGHT;
        }
        return SINGLE_QUBIT_GATE_WEIGHT;
    }

    @Override
    public String getMetricName() {
        return METRIC_NAME;
    }
}
