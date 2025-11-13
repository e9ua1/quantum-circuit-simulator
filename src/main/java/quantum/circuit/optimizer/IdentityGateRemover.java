package quantum.circuit.optimizer;

import java.util.ArrayList;
import java.util.List;

import quantum.circuit.domain.circuit.CircuitStep;
import quantum.circuit.domain.circuit.QuantumCircuit;
import quantum.circuit.domain.circuit.QuantumCircuitBuilder;
import quantum.circuit.domain.gate.QuantumGate;

public class IdentityGateRemover implements CircuitOptimizer {

    private static final String OPTIMIZATION_NAME = "Identity Gate Remover";

    @Override
    public QuantumCircuit optimize(QuantumCircuit circuit) {
        List<CircuitStep> steps = circuit.getSteps();
        List<CircuitStep> optimizedSteps = removeIdentityPairs(steps);

        return new QuantumCircuitBuilder()
                .withQubits(circuit.getQubitCount())
                .addSteps(optimizedSteps)
                .build();
    }

    private List<CircuitStep> removeIdentityPairs(List<CircuitStep> steps) {
        List<CircuitStep> result = new ArrayList<>();
        int i = 0;

        while (i < steps.size()) {
            if (i < steps.size() - 1 && isIdentityPair(steps.get(i), steps.get(i + 1))) {
                i += 2;
            } else {
                result.add(steps.get(i));
                i++;
            }
        }

        return result;
    }

    private boolean isIdentityPair(CircuitStep step1, CircuitStep step2) {
        if (step1.getGateCount() != 1 || step2.getGateCount() != 1) {
            return false;
        }

        QuantumGate gate1 = step1.getGates().get(0);
        QuantumGate gate2 = step2.getGates().get(0);

        return isSameGate(gate1, gate2);
    }

    private boolean isSameGate(QuantumGate gate1, QuantumGate gate2) {
        if (!gate1.getName().equals(gate2.getName())) {
            return false;
        }

        return gate1.getAffectedQubits().equals(gate2.getAffectedQubits());
    }

    @Override
    public String getOptimizationName() {
        return OPTIMIZATION_NAME;
    }
}
