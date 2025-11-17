package quantum.circuit.analyzer;

import quantum.circuit.analyzer.metric.ComplexityMetric;
import quantum.circuit.domain.circuit.QuantumCircuit;

public class CircuitComplexity {

    private static final ComplexityMetric METRIC = new ComplexityMetric();

    public static int calculate(QuantumCircuit circuit) {
        return METRIC.calculate(circuit);
    }
}
