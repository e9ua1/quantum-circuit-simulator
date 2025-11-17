package quantum.circuit.analyzer.metric;

import quantum.circuit.domain.circuit.QuantumCircuit;

public interface CircuitMetric {

    int calculate(QuantumCircuit circuit);

    String getMetricName();
}
