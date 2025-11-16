package quantum.circuit.optimizer;

import quantum.circuit.domain.circuit.QuantumCircuit;

public class GateFusionOptimizer implements CircuitOptimizer {

    private static final String OPTIMIZATION_NAME = "Gate Fusion Optimizer";

    @Override
    public QuantumCircuit optimize(QuantumCircuit circuit) {
        // TODO: 게이트 융합 최적화 구현
        return circuit;
    }

    @Override
    public String getOptimizationName() {
        return OPTIMIZATION_NAME;
    }
}
