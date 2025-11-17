package quantum.circuit.optimizer;

import java.util.List;

import quantum.circuit.domain.circuit.QuantumCircuit;
import quantum.circuit.optimizer.rule.rules.ConsecutiveSameGateRule;

public class IdentityGateRemover implements CircuitOptimizer {

    private static final String OPTIMIZATION_NAME = "Identity Gate Remover";
    private final CircuitOptimizer optimizer;

    public IdentityGateRemover() {
        this.optimizer = new RuleBasedOptimizer(
                List.of(new ConsecutiveSameGateRule()),
                OPTIMIZATION_NAME
        );
    }

    @Override
    public QuantumCircuit optimize(QuantumCircuit circuit) {
        return optimizer.optimize(circuit);
    }

    @Override
    public String getOptimizationName() {
        return OPTIMIZATION_NAME;
    }
}
