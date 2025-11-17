package quantum.circuit.view;

import quantum.circuit.domain.circuit.QuantumCircuit;
import quantum.circuit.domain.state.MeasurementResult;
import quantum.circuit.domain.state.QuantumState;
import quantum.circuit.visualizer.CircuitVisualizer;
import quantum.circuit.visualizer.StateVisualizer;

public class OutputView {

    private static final String SEPARATOR = "===================================";
    private static final String ERROR_PREFIX = "[ERROR] ";
    private static final String CIRCUIT_HEADER = "=== Quantum Circuit ===";
    private static final String STATE_HEADER = "=== Quantum State ===";
    private static final String MEASUREMENT_HEADER = "=== Measurement Result ===";

    public static void printHeader(String title) {
        printSeparator();
        printMessage(title);
        printSeparator();
    }

    public static void printMessage(String message) {
        System.out.println(message);
    }

    public static void printCircuit(QuantumCircuit circuit) {
        printMessage(CIRCUIT_HEADER);
        printMessage(CircuitVisualizer.generateSummary(circuit));
        printNewLine();
        printMessage(CircuitVisualizer.visualize(circuit));
        printNewLine();
        printMessage(CircuitVisualizer.generateStepDescription(circuit));
    }

    public static void printState(QuantumState state) {
        printMessage(STATE_HEADER);
        printMessage(StateVisualizer.visualize(state));
    }

    public static void printMeasurementResult(int qubitIndex, MeasurementResult result) {
        printMessage(MEASUREMENT_HEADER);
        System.out.printf("Qubit %d: %s%n", qubitIndex, result.toString());
    }

    public static void printErrorMessage(String message) {
        printMessage(ERROR_PREFIX + message);
    }

    public static void printSeparator() {
        System.out.println(SEPARATOR);
    }

    public static void printNewLine() {
        System.out.println();
    }
}
