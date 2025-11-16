package quantum.circuit.optimizer;

import java.util.List;

import quantum.circuit.domain.circuit.QuantumCircuit;

public class OptimizationPipeline implements CircuitOptimizer {

    private static final String OPTIMIZATION_NAME = "Optimization Pipeline";
    private static final String ERROR_NULL_OPTIMIZERS = "최적화기 리스트는 null일 수 없습니다.";

    private final List<CircuitOptimizer> optimizers;

    public OptimizationPipeline(List<CircuitOptimizer> optimizers) {
        validateOptimizers(optimizers);
        this.optimizers = List.copyOf(optimizers);
    }

    private void validateOptimizers(List<CircuitOptimizer> optimizers) {
        if (optimizers == null) {
            throw new IllegalArgumentException(ERROR_NULL_OPTIMIZERS);
        }
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
