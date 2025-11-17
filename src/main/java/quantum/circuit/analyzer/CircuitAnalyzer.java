package quantum.circuit.analyzer;

import java.util.List;

import quantum.circuit.analyzer.metric.CircuitDepthMetric;
import quantum.circuit.analyzer.metric.CircuitMetric;
import quantum.circuit.analyzer.metric.ComplexityMetric;
import quantum.circuit.analyzer.metric.EntanglementMetric;
import quantum.circuit.analyzer.metric.GateCountMetric;
import quantum.circuit.domain.circuit.QuantumCircuit;

public class CircuitAnalyzer {

    private final List<CircuitMetric> metrics;

    public CircuitAnalyzer() {
        this(createDefaultMetrics());
    }

    public CircuitAnalyzer(List<CircuitMetric> metrics) {
        this.metrics = List.copyOf(metrics);
    }

    private static List<CircuitMetric> createDefaultMetrics() {
        return List.of(
                new CircuitDepthMetric(),
                new GateCountMetric(),
                new ComplexityMetric(),
                new EntanglementMetric()
        );
    }

    public AnalysisReport performAnalysis(QuantumCircuit circuit) {
        int depth = calculateMetric(circuit, CircuitDepthMetric.class);
        int gateCount = calculateMetric(circuit, GateCountMetric.class);
        int complexity = calculateMetric(circuit, ComplexityMetric.class);
        int entanglement = calculateMetric(circuit, EntanglementMetric.class);

        return new AnalysisReport(depth, gateCount, complexity, entanglement);
    }

    public static AnalysisReport analyze(QuantumCircuit circuit) {
        return new CircuitAnalyzer().performAnalysis(circuit);
    }

    private int calculateMetric(QuantumCircuit circuit, Class<? extends CircuitMetric> metricClass) {
        return metrics.stream()
                .filter(metricClass::isInstance)
                .findFirst()
                .map(metric -> metric.calculate(circuit))
                .orElse(0);
    }

    public int getMetricCount() {
        return metrics.size();
    }
}
