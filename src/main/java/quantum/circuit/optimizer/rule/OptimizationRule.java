package quantum.circuit.optimizer.rule;

import quantum.circuit.domain.circuit.CircuitStep;
import quantum.circuit.domain.gate.QuantumGate;

public interface OptimizationRule {

    boolean canOptimize(QuantumGate first, QuantumGate second);

    default boolean isSameQubitSingleGate(CircuitStep step1, CircuitStep step2) {
        if (!step1.isSingleGateStep() || !step2.isSingleGateStep()) {
            return false;
        }

        QuantumGate gate1 = step1.getSingleGate();
        QuantumGate gate2 = step2.getSingleGate();

        return gate1.getAffectedQubits().equals(gate2.getAffectedQubits());
    }

    String getRuleName();
}
