package quantum.circuit.visualizer;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import quantum.circuit.domain.circuit.CircuitStep;
import quantum.circuit.domain.circuit.QuantumCircuit;
import quantum.circuit.domain.gate.CNOTGate;
import quantum.circuit.domain.gate.QuantumGate;

public class CircuitVisualizer {

    private static final String QUBIT_PREFIX = "Q";
    private static final String WIRE = "─";
    private static final String CONTROL_SYMBOL = "●";
    private static final String TARGET_SYMBOL = "X";
    private static final String VERTICAL_LINE = "│";
    private static final String SPACE = " ";

    public static String visualize(QuantumCircuit circuit) {
        int qubitCount = circuit.getQubitCount();
        List<CircuitStep> steps = circuit.getSteps();

        return IntStream.range(0, qubitCount)
                .mapToObj(qubit -> buildQubitLine(qubit, steps, qubitCount))
                .collect(Collectors.joining("\n"));
    }

    private static String buildQubitLine(int qubitIndex, List<CircuitStep> steps, int qubitCount) {
        StringBuilder line = new StringBuilder(QUBIT_PREFIX + qubitIndex + ": " + WIRE);

        for (CircuitStep step : steps) {
            line.append(getGateSymbolForQubit(qubitIndex, step, qubitCount));
            line.append(WIRE);
        }

        return line.toString();
    }

    private static String getGateSymbolForQubit(int qubitIndex, CircuitStep step, int qubitCount) {
        for (QuantumGate gate : step.getGates()) {
            if (gate instanceof CNOTGate) {
                CNOTGate cnot = (CNOTGate) gate;
                if (cnot.getControl().getValue() == qubitIndex) {
                    return CONTROL_SYMBOL;
                }
                if (cnot.getTarget().getValue() == qubitIndex) {
                    return TARGET_SYMBOL;
                }
            }
            if (isGateAppliedToQubit(gate, qubitIndex)) {
                return gate.getName();
            }
        }
        return WIRE;
    }

    private static boolean isGateAppliedToQubit(QuantumGate gate, int qubitIndex) {
        if (gate instanceof CNOTGate) {
            return false;
        }
        try {
            int targetIndex = (int) gate.getClass().getMethod("getTarget").invoke(gate).getClass()
                    .getMethod("getValue").invoke(gate.getClass().getMethod("getTarget").invoke(gate));
            return targetIndex == qubitIndex;
        } catch (Exception e) {
            return false;
        }
    }

    public static String generateSummary(QuantumCircuit circuit) {
        int qubitCount = circuit.getQubitCount();
        int stepCount = circuit.getStepCount();

        return String.format("Quantum Circuit (%d qubit%s, %d step%s)",
                qubitCount, qubitCount > 1 ? "s" : "",
                stepCount, stepCount > 1 ? "s" : "");
    }

    public static String generateStepDescription(QuantumCircuit circuit) {
        List<CircuitStep> steps = circuit.getSteps();

        return IntStream.range(0, steps.size())
                .mapToObj(i -> buildStepDescription(i + 1, steps.get(i)))
                .collect(Collectors.joining("\n"));
    }

    private static String buildStepDescription(int stepNumber, CircuitStep step) {
        String gateDescriptions = step.getGates().stream()
                .map(CircuitVisualizer::describeGate)
                .collect(Collectors.joining(", "));

        return String.format("Step %d: %s", stepNumber, gateDescriptions);
    }

    private static String describeGate(QuantumGate gate) {
        if (gate instanceof CNOTGate) {
            CNOTGate cnot = (CNOTGate) gate;
            return String.format("CNOT(Q%d→Q%d)",
                    cnot.getControl().getValue(),
                    cnot.getTarget().getValue());
        }

        try {
            int targetIndex = (int) gate.getClass().getMethod("getTarget").invoke(gate).getClass()
                    .getMethod("getValue").invoke(gate.getClass().getMethod("getTarget").invoke(gate));
            return String.format("%s(Q%d)", gate.getName(), targetIndex);
        } catch (Exception e) {
            return gate.getName();
        }
    }
}
