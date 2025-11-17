package quantum.circuit.analyzer.metric;

import quantum.circuit.domain.circuit.QuantumCircuit;

public class GateCountMetric implements CircuitMetric {

    private static final String METRIC_NAME = "Gate Count";

    @Override
    public int calculate(QuantumCircuit circuit) {
        return circuit.getTotalGateCount();
    }

    @Override
    public String getMetricName() {
        return METRIC_NAME;
    }
}
