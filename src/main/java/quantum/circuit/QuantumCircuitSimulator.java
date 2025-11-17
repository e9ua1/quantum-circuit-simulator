package quantum.circuit;

import java.util.List;

import quantum.circuit.domain.circuit.CircuitStep;
import quantum.circuit.domain.circuit.QuantumCircuit;
import quantum.circuit.domain.circuit.QuantumCircuitBuilder;
import quantum.circuit.domain.state.QuantumState;
import quantum.circuit.util.CircuitStepBuilder;
import quantum.circuit.util.InputRetryHandler;
import quantum.circuit.view.InputView;
import quantum.circuit.view.OutputView;

public class QuantumCircuitSimulator {

    public void start() {
        int qubitCount = InputRetryHandler.retry(InputView::readQubitCount);
        List<CircuitStep> steps = CircuitStepBuilder.buildSteps(qubitCount);
        QuantumCircuit circuit = buildCircuit(qubitCount, steps);
        executeAndDisplay(circuit);
    }

    private QuantumCircuit buildCircuit(int qubitCount, List<CircuitStep> steps) {
        return new QuantumCircuitBuilder()
                .withQubits(qubitCount)
                .addSteps(steps)
                .build();
    }

    private void executeAndDisplay(QuantumCircuit circuit) {
        OutputView.printSeparator();
        OutputView.printCircuit(circuit);
        OutputView.printNewLine();

        QuantumState state = circuit.execute();
        OutputView.printState(state);
        OutputView.printSeparator();
    }
}
