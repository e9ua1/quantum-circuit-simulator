package quantum.circuit.benchmark;

import java.util.List;
import java.util.Map;

import quantum.circuit.analyzer.CircuitDepth;
import quantum.circuit.analyzer.GateCount;
import quantum.circuit.domain.circuit.QuantumCircuit;

public class BenchmarkRunner {

    private final List<PerformanceMonitor> monitors;

    public BenchmarkRunner(List<PerformanceMonitor> monitors) {
        this.monitors = List.copyOf(monitors);
    }

    public BenchmarkReport runBenchmark(Map<String, QuantumCircuit> circuits) {
        Map<String, PerformanceMetrics> results = new java.util.HashMap<>();

        for (Map.Entry<String, QuantumCircuit> entry : circuits.entrySet()) {
            String name = entry.getKey();
            QuantumCircuit circuit = entry.getValue();

            notifyStart(name);
            PerformanceMetrics metrics = measurePerformance(circuit);
            notifyComplete(name, metrics);

            results.put(name, metrics);
        }

        return new BenchmarkReport(results);
    }

    private PerformanceMetrics measurePerformance(QuantumCircuit circuit) {
        long startTime = System.nanoTime();
        int gateCount = GateCount.calculate(circuit);
        int depth = CircuitDepth.calculate(circuit);
        long endTime = System.nanoTime();
        long executionTime = endTime - startTime;

        return new PerformanceMetrics(gateCount, depth, executionTime);
    }

    private void notifyStart(String circuitName) {
        for (PerformanceMonitor monitor : monitors) {
            monitor.onBenchmarkStart(circuitName);
        }
    }

    private void notifyComplete(String circuitName, PerformanceMetrics metrics) {
        for (PerformanceMonitor monitor : monitors) {
            monitor.onBenchmarkComplete(circuitName, metrics);
        }
    }
}
