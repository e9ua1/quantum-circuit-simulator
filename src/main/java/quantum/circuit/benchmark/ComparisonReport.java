package quantum.circuit.benchmark;

import quantum.circuit.analyzer.CircuitDepth;
import quantum.circuit.analyzer.GateCount;
import quantum.circuit.domain.circuit.QuantumCircuit;

public class ComparisonReport {

    private final QuantumCircuit original;
    private final QuantumCircuit optimized;

    public ComparisonReport(QuantumCircuit original, QuantumCircuit optimized) {
        this.original = original;
        this.optimized = optimized;
    }

    public int getOriginalGateCount() {
        return GateCount.calculate(original);
    }

    public int getOptimizedGateCount() {
        return GateCount.calculate(optimized);
    }

    public double getGateReduction() {
        return calculateReduction(getOriginalGateCount(), getOptimizedGateCount());
    }

    public int getOriginalDepth() {
        return CircuitDepth.calculate(original);
    }

    public int getOptimizedDepth() {
        return CircuitDepth.calculate(optimized);
    }

    public double getDepthReduction() {
        return calculateReduction(getOriginalDepth(), getOptimizedDepth());
    }

    private double calculateReduction(int original, int optimized) {
        if (original == 0) {
            return 0.0;
        }
        return (double) (original - optimized) / original * 100;
    }
}
