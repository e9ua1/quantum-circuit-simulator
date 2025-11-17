package quantum.circuit.analyzer;

import quantum.circuit.analyzer.metric.CircuitDepthMetric;
import quantum.circuit.domain.circuit.QuantumCircuit;

public class CircuitDepth {

    private static final CircuitDepthMetric METRIC = new CircuitDepthMetric();

    public static int calculate(QuantumCircuit circuit) {
        return METRIC.calculate(circuit);
    }
}
