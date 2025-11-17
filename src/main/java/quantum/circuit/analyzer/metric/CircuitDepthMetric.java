package quantum.circuit.analyzer.metric;

import quantum.circuit.domain.circuit.QuantumCircuit;

public class CircuitDepthMetric implements CircuitMetric {

    private static final String METRIC_NAME = "Circuit Depth";

    @Override
    public int calculate(QuantumCircuit circuit) {
        return circuit.getDepth();
    }

    @Override
    public String getMetricName() {
        return METRIC_NAME;
    }
}
