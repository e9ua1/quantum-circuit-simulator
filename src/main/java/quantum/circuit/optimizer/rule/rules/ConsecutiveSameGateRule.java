package quantum.circuit.optimizer.rule.rules;

import quantum.circuit.domain.gate.QuantumGate;
import quantum.circuit.optimizer.rule.OptimizationRule;

public class ConsecutiveSameGateRule implements OptimizationRule {

    private static final String RULE_NAME = "Consecutive Same Gate Rule";

    @Override
    public boolean canOptimize(QuantumGate first, QuantumGate second) {
        if (!first.getClass().equals(second.getClass())) {
            return false;
        }

        return first.getAffectedQubits().equals(second.getAffectedQubits());
    }

    @Override
    public String getRuleName() {
        return RULE_NAME;
    }
}
