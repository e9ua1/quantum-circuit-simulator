package quantum.circuit.optimizer;

import quantum.circuit.domain.circuit.QuantumCircuit;

public class GateFusionOptimizer implements CircuitOptimizer {

    private static final String OPTIMIZATION_NAME = "Gate Fusion Optimizer";

    @Override
    public QuantumCircuit optimize(QuantumCircuit circuit) {
        return circuit;
    }

    @Override
    public String getOptimizationName() {
        return OPTIMIZATION_NAME;
    }
}
