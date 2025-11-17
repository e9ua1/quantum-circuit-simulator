package quantum.circuit.analyzer;

import quantum.circuit.analyzer.metric.GateCountMetric;
import quantum.circuit.domain.circuit.QuantumCircuit;

public class GateCount {

    private static final GateCountMetric METRIC = new GateCountMetric();

    public static int calculate(QuantumCircuit circuit) {
        return METRIC.calculate(circuit);
    }
}
