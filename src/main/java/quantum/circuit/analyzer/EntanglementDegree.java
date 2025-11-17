package quantum.circuit.analyzer;

import quantum.circuit.analyzer.metric.EntanglementMetric;
import quantum.circuit.domain.circuit.QuantumCircuit;

public class EntanglementDegree {

    private static final EntanglementMetric METRIC = new EntanglementMetric();

    public static int calculate(QuantumCircuit circuit) {
        return METRIC.calculate(circuit);
    }
}
