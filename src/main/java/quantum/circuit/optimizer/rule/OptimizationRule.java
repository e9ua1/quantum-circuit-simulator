package quantum.circuit.optimizer.rule;

import quantum.circuit.domain.gate.QuantumGate;

public interface OptimizationRule {

    boolean canOptimize(QuantumGate first, QuantumGate second);
}
