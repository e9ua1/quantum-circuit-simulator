package quantum.circuit.visualizer;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import quantum.circuit.domain.circuit.CircuitStep;
import quantum.circuit.domain.circuit.QubitIndex;
import quantum.circuit.domain.circuit.QuantumCircuit;
import quantum.circuit.domain.gate.CNOTGate;
import quantum.circuit.domain.gate.QuantumGate;

public class CircuitVisualizer {

    private static final String WIRE = "─";
    private static final String GATE_PREFIX = "─";
    private static final String GATE_SUFFIX = "─";
    private static final String CONTROL_SYMBOL = "●";
    private static final String TARGET_SYMBOL = "X";
    private static final String VERTICAL_LINE = "│";
    private static final String NEWLINE = "\n";

    public static String visualize(QuantumCircuit circuit) {
        StringBuilder visualization = new StringBuilder();
        int qubitCount = circuit.getQubitCount();
        List<CircuitStep> steps = circuit.getSteps();

        for (int i = 0; i < qubitCount; i++) {
            visualization.append(String.format("Q%d: ", i));
            for (CircuitStep step : steps) {
                visualization.append(getGateSymbolForQubit(i, step, qubitCount));
            }
            visualization.append(NEWLINE);
        }

        return visualization.toString();
    }

    private static String getGateSymbolForQubit(int qubitIndex, CircuitStep step, int qubitCount) {
        for (QuantumGate gate : step.getGates()) {
            if (gate instanceof CNOTGate cnot) {
                int controlIndex = cnot.getControl().getValue();
                int targetIndex = cnot.getTarget().getValue();

                if (qubitIndex == controlIndex) {
                    return GATE_PREFIX + CONTROL_SYMBOL + GATE_SUFFIX;
                }
                if (qubitIndex == targetIndex) {
                    return GATE_PREFIX + TARGET_SYMBOL + GATE_SUFFIX;
                }
                if (isBetween(qubitIndex, controlIndex, targetIndex)) {
                    return GATE_PREFIX + VERTICAL_LINE + GATE_SUFFIX;
                }
            }

            if (isGateAppliedToQubit(gate, qubitIndex)) {
                return GATE_PREFIX + gate.getName() + GATE_SUFFIX;
            }
        }
        return WIRE + WIRE + WIRE;
    }

    private static boolean isGateAppliedToQubit(QuantumGate gate, int qubitIndex) {
        if (gate instanceof CNOTGate) {
            return false;
        }

        return gate.getAffectedQubits().stream()
                .anyMatch(index -> index.getValue() == qubitIndex);
    }

    private static boolean isBetween(int qubit, int control, int target) {
        int min = Math.min(control, target);
        int max = Math.max(control, target);
        return qubit > min && qubit < max;
    }

    public static String generateSummary(QuantumCircuit circuit) {
        int qubitCount = circuit.getQubitCount();
        int stepCount = circuit.getSteps().size();
        String qubitText = qubitCount == 1 ? "qubit" : "qubits";
        String stepText = stepCount == 1 ? "step" : "steps";

        return String.format("Circuit: %d %s, %d %s", qubitCount, qubitText, stepCount, stepText);
    }

    public static String generateStepDescription(QuantumCircuit circuit) {
        StringBuilder description = new StringBuilder();
        List<CircuitStep> steps = circuit.getSteps();

        for (int i = 0; i < steps.size(); i++) {
            description.append(String.format("Step %d: ", i + 1));
            CircuitStep step = steps.get(i);
            List<String> gateDescriptions = step.getGates().stream()
                    .map(CircuitVisualizer::describeGate)
                    .collect(Collectors.toList());
            description.append(String.join(", ", gateDescriptions));
            description.append(NEWLINE);
        }

        return description.toString();
    }

    private static String describeGate(QuantumGate gate) {
        if (gate instanceof CNOTGate cnot) {
            return String.format("CNOT(Q%d→Q%d)",
                    cnot.getControl().getValue(),
                    cnot.getTarget().getValue());
        }

        Set<QubitIndex> affected = gate.getAffectedQubits();
        if (!affected.isEmpty()) {
            QubitIndex target = affected.iterator().next();
            return String.format("%s(Q%d)", gate.getName(), target.getValue());
        }

        return gate.getName();
    }
}
