package quantum.circuit.optimizer;

import java.util.List;

import quantum.circuit.domain.circuit.QuantumCircuit;

public class OptimizationPipeline implements CircuitOptimizer {

    private static final String OPTIMIZATION_NAME = "Optimization Pipeline";

    private final List<CircuitOptimizer> optimizers;

    public OptimizationPipeline(List<CircuitOptimizer> optimizers) {
        this.optimizers = List.copyOf(optimizers);
    }

    @Override
    public QuantumCircuit optimize(QuantumCircuit circuit) {
        QuantumCircuit current = circuit;
        for (CircuitOptimizer optimizer : optimizers) {
            current = optimizer.optimize(current);
        }
        return current;
    }

    @Override
    public String getOptimizationName() {
        return OPTIMIZATION_NAME;
    }

    public int getOptimizerCount() {
        return optimizers.size();
    }
}
