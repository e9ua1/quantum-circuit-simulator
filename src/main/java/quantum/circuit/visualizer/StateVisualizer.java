package quantum.circuit.visualizer;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import quantum.circuit.domain.circuit.QubitIndex;
import quantum.circuit.domain.state.Probability;
import quantum.circuit.domain.state.QuantumState;

public class StateVisualizer {

    private static final String BAR_CHARACTER = "█";
    private static final int MAX_BAR_LENGTH = 30;
    private static final String PERCENTAGE_FORMAT = "%.1f%%";

    public static String visualizeProbabilityBar(Probability probability) {
        int barLength = (int) (probability.getValue() * MAX_BAR_LENGTH);
        String bar = BAR_CHARACTER.repeat(barLength);
        String percentage = String.format(PERCENTAGE_FORMAT, probability.toPercentage());

        return String.format("%s %s", bar, percentage);
    }

    public static String visualizeQubitProbabilities(QuantumState state) {
        int qubitCount = state.getQubitCount();

        return IntStream.range(0, qubitCount)
                .mapToObj(i -> visualizeSingleQubit(state, i))
                .collect(Collectors.joining("\n"));
    }

    private static String visualizeSingleQubit(QuantumState state, int qubitIndex) {
        QubitIndex index = new QubitIndex(qubitIndex);
        Probability probOne = state.getProbabilityOfOne(index);
        Probability probZero = new Probability(1.0 - probOne.getValue());

        return String.format("Qubit %d → |0⟩: %s |1⟩: %s",
                qubitIndex,
                String.format(PERCENTAGE_FORMAT, probZero.toPercentage()),
                String.format(PERCENTAGE_FORMAT, probOne.toPercentage()));
    }

    public static String visualize(QuantumState state) {
        return visualizeQubitProbabilities(state);
    }
}
