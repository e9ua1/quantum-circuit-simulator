package quantum.circuit.optimizer.rule.rules;

import quantum.circuit.domain.gate.QuantumGate;
import quantum.circuit.optimizer.rule.OptimizationRule;

public class ConsecutiveSameGateRule implements OptimizationRule {

    @Override
    public boolean canOptimize(QuantumGate first, QuantumGate second) {
        if (!first.getClass().equals(second.getClass())) {
            return false;
        }

        return first.getAffectedQubits().equals(second.getAffectedQubits());
    }
}
