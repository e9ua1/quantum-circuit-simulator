package quantum.circuit;

import java.util.ArrayList;
import java.util.List;

import quantum.circuit.domain.circuit.CircuitStep;
import quantum.circuit.domain.circuit.QubitIndex;
import quantum.circuit.domain.circuit.QuantumCircuit;
import quantum.circuit.domain.circuit.QuantumCircuitBuilder;
import quantum.circuit.domain.gate.CNOTGate;
import quantum.circuit.domain.gate.QuantumGate;
import quantum.circuit.domain.state.QuantumState;
import quantum.circuit.factory.SingleQubitGateFactory;
import quantum.circuit.util.InputRetryHandler;
import quantum.circuit.view.InputView;
import quantum.circuit.view.OutputView;

public class QuantumCircuitSimulator {

    private static final String GATE_CNOT = "CNOT";
    private static final String CONTINUE_YES = "Y";
    private static final String PROMPT_CONTINUE = "게이트를 더 추가하시겠습니까? (y/n):";

    public void start() {
        int qubitCount = InputRetryHandler.retry(InputView::readQubitCount);
        List<CircuitStep> steps = buildSteps(qubitCount);
        QuantumCircuit circuit = buildCircuit(qubitCount, steps);
        executeAndDisplay(circuit);
    }

    private List<CircuitStep> buildSteps(int qubitCount) {
        List<CircuitStep> steps = new ArrayList<>();
        System.out.printf("(큐비트 인덱스: 0부터 %d까지 사용 가능)%n", qubitCount - 1);

        do {
            QuantumGate gate = InputRetryHandler.retry(() -> readGate(qubitCount));
            steps.add(new CircuitStep(List.of(gate)));
        } while (shouldContinue());

        return steps;
    }

    private QuantumGate readGate(int qubitCount) {
        String gateType = InputView.readGateType();

        if (GATE_CNOT.equals(gateType)) {
            return createCNOTGate(qubitCount);
        }
        return createSingleQubitGate(gateType);
    }

    private QuantumGate createCNOTGate(int qubitCount) {
        int control = InputRetryHandler.retry(InputView::readControlQubit);
        int target = InputRetryHandler.retry(InputView::readTargetQubit);
        return new CNOTGate(new QubitIndex(control), new QubitIndex(target));
    }

    private QuantumGate createSingleQubitGate(String gateType) {
        int target = InputRetryHandler.retry(InputView::readTargetQubit);
        QubitIndex targetIndex = new QubitIndex(target);
        return SingleQubitGateFactory.create(gateType, targetIndex);
    }

    private boolean shouldContinue() {
        System.out.println(PROMPT_CONTINUE);
        String response = InputRetryHandler.retry(() -> camp.nextstep.edu.missionutils.Console.readLine());
        return CONTINUE_YES.equalsIgnoreCase(response.trim());
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
