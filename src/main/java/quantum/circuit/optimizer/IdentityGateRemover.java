package quantum.circuit.optimizer;

import java.util.ArrayList;
import java.util.List;

import quantum.circuit.domain.circuit.CircuitStep;
import quantum.circuit.domain.circuit.QuantumCircuit;
import quantum.circuit.domain.circuit.QuantumCircuitBuilder;
import quantum.circuit.domain.gate.CNOTGate;
import quantum.circuit.domain.gate.QuantumGate;

public class IdentityGateRemover implements CircuitOptimizer {

    private static final String OPTIMIZATION_NAME = "Identity Gate Remover";

    @Override
    public QuantumCircuit optimize(QuantumCircuit circuit) {
        List<CircuitStep> steps = circuit.getSteps();
        if (steps.isEmpty()) {
            return circuit;
        }
        List<CircuitStep> optimizedSteps = removeIdentityOperations(steps);
        return buildOptimizedCircuit(circuit, optimizedSteps);
    }

    private List<CircuitStep> removeIdentityOperations(List<CircuitStep> steps) {
        List<CircuitStep> result = new ArrayList<>();
        int i = 0;
        while (i < steps.size()) {
            if (shouldSkipPair(steps, i)) {
                i += 2;
            } else {
                result.add(steps.get(i));
                i++;
            }
        }
        return result;
    }

    private boolean shouldSkipPair(List<CircuitStep> steps, int index) {
        if (index + 1 >= steps.size()) {
            return false;
        }
        CircuitStep current = steps.get(index);
        CircuitStep next = steps.get(index + 1);
        return isIdentityPair(current, next);
    }

    private boolean isIdentityPair(CircuitStep step1, CircuitStep step2) {
        if (step1.getGateCount() != 1 || step2.getGateCount() != 1) {
            return false;
        }
        QuantumGate gate1 = step1.getGates().get(0);
        QuantumGate gate2 = step2.getGates().get(0);
        if (!gate1.getName().equals(gate2.getName())) {
            return false;
        }
        if (gate1 instanceof CNOTGate) {
            return false;
        }
        return isSameTarget(gate1, gate2);
    }

    private boolean isSameTarget(QuantumGate gate1, QuantumGate gate2) {
        try {
            Object target1 = gate1.getClass().getMethod("getTarget").invoke(gate1);
            Object target2 = gate2.getClass().getMethod("getTarget").invoke(gate2);
            return target1.equals(target2);
        } catch (Exception e) {
            return false;
        }
    }

    private QuantumCircuit buildOptimizedCircuit(QuantumCircuit original, List<CircuitStep> steps) {
        return new QuantumCircuitBuilder()
                .withQubits(original.getQubitCount())
                .addSteps(steps)
                .build();
    }

    @Override
    public String getOptimizationName() {
        return OPTIMIZATION_NAME;
    }
}
