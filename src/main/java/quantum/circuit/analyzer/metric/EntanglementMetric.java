package quantum.circuit.analyzer.metric;

import quantum.circuit.domain.circuit.QuantumCircuit;
import quantum.circuit.domain.gate.CNOTGate;

public class EntanglementMetric implements CircuitMetric {

    private static final String METRIC_NAME = "Entanglement Degree";

    @Override
    public int calculate(QuantumCircuit circuit) {
        return (int) circuit.getSteps().stream()
                .flatMap(step -> step.getGates().stream())
                .filter(gate -> gate instanceof CNOTGate)
                .count();
    }

    @Override
    public String getMetricName() {
        return METRIC_NAME;
    }
}
