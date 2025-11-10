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
        int originalCount = getOriginalGateCount();
        if (originalCount == 0) {
            return 0.0;
        }
        return (double) (originalCount - getOptimizedGateCount()) / originalCount * 100;
    }

    public int getOriginalDepth() {
        return CircuitDepth.calculate(original);
    }

    public int getOptimizedDepth() {
        return CircuitDepth.calculate(optimized);
    }

    public double getDepthReduction() {
        int originalDepth = getOriginalDepth();
        if (originalDepth == 0) {
            return 0.0;
        }
        return (double) (originalDepth - getOptimizedDepth()) / originalDepth * 100;
    }
}
