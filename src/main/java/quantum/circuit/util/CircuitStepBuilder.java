package quantum.circuit.util;

import java.util.ArrayList;
import java.util.List;

import quantum.circuit.domain.circuit.CircuitStep;
import quantum.circuit.domain.circuit.QubitIndex;
import quantum.circuit.domain.gate.CNOTGate;
import quantum.circuit.domain.gate.QuantumGate;
import quantum.circuit.factory.SingleQubitGateFactory;
import quantum.circuit.view.InputView;

public class CircuitStepBuilder {

    private static final String GATE_CNOT = "CNOT";
    private static final String CONTINUE_YES = "Y";
    private static final String PROMPT_CONTINUE = "게이트를 더 추가하시겠습니까? (y/n):";
    private static final String QUBIT_RANGE_FORMAT = "(큐비트 인덱스: 0부터 %d까지 사용 가능)%n";

    private CircuitStepBuilder() {
    }

    public static List<CircuitStep> buildSteps(int qubitCount) {
        List<CircuitStep> steps = new ArrayList<>();
        System.out.printf(QUBIT_RANGE_FORMAT, qubitCount - 1);

        do {
            QuantumGate gate = InputRetryHandler.retry(() -> readGate(qubitCount));
            steps.add(new CircuitStep(List.of(gate)));
        } while (shouldContinue());

        return steps;
    }

    private static QuantumGate readGate(int qubitCount) {
        String gateType = InputView.readGateType();

        if (GATE_CNOT.equals(gateType)) {
            return createCNOTGate();
        }
        return createSingleQubitGate(gateType);
    }

    private static QuantumGate createCNOTGate() {
        int control = InputRetryHandler.retry(InputView::readControlQubit);
        int target = InputRetryHandler.retry(InputView::readTargetQubit);
        return new CNOTGate(new QubitIndex(control), new QubitIndex(target));
    }

    private static QuantumGate createSingleQubitGate(String gateType) {
        int target = InputRetryHandler.retry(InputView::readTargetQubit);
        QubitIndex targetIndex = new QubitIndex(target);
        return SingleQubitGateFactory.create(gateType, targetIndex);
    }

    private static boolean shouldContinue() {
        System.out.println(PROMPT_CONTINUE);
        String response = InputRetryHandler.retry(() -> camp.nextstep.edu.missionutils.Console.readLine());
        return CONTINUE_YES.equalsIgnoreCase(response.strip());
    }
}
