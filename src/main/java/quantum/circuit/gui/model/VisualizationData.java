package quantum.circuit.gui.model;

import quantum.circuit.domain.circuit.CircuitStep;
import quantum.circuit.domain.circuit.QuantumCircuit;
import quantum.circuit.domain.gate.QuantumGate;

import java.util.List;
import java.util.Optional;

public class VisualizationData {

    private final int qubitCount;
    private final List<CircuitStep> steps;

    private VisualizationData(int qubitCount, List<CircuitStep> steps) {
        this.qubitCount = qubitCount;
        this.steps = List.copyOf(steps);
    }

    public static VisualizationData from(QuantumCircuit circuit) {
        return new VisualizationData(circuit.getQubitCount(), circuit.getSteps());
    }

    public int getQubitCount() {
        return qubitCount;
    }

    public int getStepCount() {
        return steps.size();
    }

    public Optional<QuantumGate> getGateAt(int stepIndex, int qubitIndex) {
        if (stepIndex < 0 || stepIndex >= steps.size()) {
            return Optional.empty();
        }

        CircuitStep step = steps.get(stepIndex);
        return step.getGates().stream()
                .filter(gate -> gate.getAffectedQubits().stream()
                        .anyMatch(index -> index.getValue() == qubitIndex))
                .findFirst();
    }

    public CircuitStep getStep(int stepIndex) {
        return steps.get(stepIndex);
    }
}
