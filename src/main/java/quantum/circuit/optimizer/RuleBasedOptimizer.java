package quantum.circuit.optimizer;

import java.util.ArrayList;
import java.util.List;

import quantum.circuit.domain.circuit.CircuitStep;
import quantum.circuit.domain.circuit.QuantumCircuit;
import quantum.circuit.domain.circuit.QuantumCircuitBuilder;
import quantum.circuit.optimizer.rule.OptimizationRule;

public class RuleBasedOptimizer implements CircuitOptimizer {

    private final List<OptimizationRule> rules;
    private final String optimizationName;

    public RuleBasedOptimizer(List<OptimizationRule> rules, String optimizationName) {
        this.rules = List.copyOf(rules);
        this.optimizationName = optimizationName;
    }

    @Override
    public QuantumCircuit optimize(QuantumCircuit circuit) {
        List<CircuitStep> steps = new ArrayList<>(circuit.getSteps());
        boolean optimized;

        do {
            optimized = false;
            for (int i = 0; i < steps.size() - 1; i++) {
                if (canOptimizeSteps(steps.get(i), steps.get(i + 1))) {
                    steps.remove(i);
                    steps.remove(i);
                    optimized = true;
                    break;
                }
            }
        } while (optimized && steps.size() > 1);

        return new QuantumCircuitBuilder()
                .withQubits(circuit.getQubitCount())
                .addSteps(steps)
                .build();
    }

    private boolean canOptimizeSteps(CircuitStep step1, CircuitStep step2) {
        if (!step1.isSingleGateStep() || !step2.isSingleGateStep()) {
            return false;
        }

        for (OptimizationRule rule : rules) {
            if (rule.canOptimize(step1.getSingleGate(), step2.getSingleGate())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String getOptimizationName() {
        return optimizationName;
    }

    public int getRuleCount() {
        return rules.size();
    }
}
