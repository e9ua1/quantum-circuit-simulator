package quantum.circuit.mode;

import java.util.ArrayList;
import java.util.List;

import quantum.circuit.domain.circuit.CircuitStep;
import quantum.circuit.domain.circuit.QubitIndex;
import quantum.circuit.domain.circuit.QuantumCircuit;
import quantum.circuit.domain.circuit.QuantumCircuitBuilder;
import quantum.circuit.domain.gate.CNOTGate;
import quantum.circuit.domain.gate.HadamardGate;
import quantum.circuit.domain.gate.PauliXGate;
import quantum.circuit.domain.gate.PauliZGate;
import quantum.circuit.domain.gate.QuantumGate;
import quantum.circuit.optimizer.CircuitOptimizer;
import quantum.circuit.optimizer.IdentityGateRemover;
import quantum.circuit.optimizer.OptimizationPipeline;
import quantum.circuit.optimizer.RedundantGateRemover;
import quantum.circuit.util.InputRetryHandler;
import quantum.circuit.view.OutputView;
import quantum.circuit.visualizer.CircuitVisualizer;

public class OptimizationMode {

    private static final String MODE_HEADER = """
            
            === 최적화 모드 ===
            회로를 입력하면 자동으로 최적화를 수행합니다.
            """;
    private static final String GATE_X = "X";
    private static final String GATE_H = "H";
    private static final String GATE_Z = "Z";
    private static final String GATE_CNOT = "CNOT";
    private static final String CONTINUE_YES = "Y";
    private static final String PROMPT_QUBIT_COUNT = "큐비트 개수를 입력하세요:";
    private static final String PROMPT_GATE_TYPE = "게이트 종류를 입력하세요 (X, H, Z, CNOT):";
    private static final String PROMPT_TARGET_QUBIT = "타겟 큐비트 인덱스를 입력하세요 (0부터 시작):";
    private static final String PROMPT_CONTROL_QUBIT = "제어 큐비트 인덱스를 입력하세요 (0부터 시작):";
    private static final String PROMPT_CONTINUE = "게이트를 더 추가하시겠습니까? (y/n):";
    private static final String ERROR_UNSUPPORTED_GATE = "지원하지 않는 게이트입니다.";
    private static final String BEFORE_OPTIMIZATION = "\n=== 최적화 전 회로 ===";
    private static final String AFTER_OPTIMIZATION = "\n=== 최적화 후 회로 ===";
    private static final String OPTIMIZATION_RESULT_FORMAT = "\n최적화 결과: %d Step → %d Step";

    public void start() {
        System.out.println(MODE_HEADER);

        int qubitCount = InputRetryHandler.retry(this::readQubitCount);
        List<CircuitStep> steps = buildSteps(qubitCount);
        QuantumCircuit circuit = buildCircuit(qubitCount, steps);

        optimizeAndDisplay(circuit);
    }

    private int readQubitCount() {
        System.out.println(PROMPT_QUBIT_COUNT);
        String input = camp.nextstep.edu.missionutils.Console.readLine();
        return Integer.parseInt(input.strip());
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
        System.out.println(PROMPT_GATE_TYPE);
        String gateType = camp.nextstep.edu.missionutils.Console.readLine().strip().toUpperCase();

        if (GATE_CNOT.equals(gateType)) {
            return createCNOTGate();
        }
        return createSingleQubitGate(gateType);
    }

    private QuantumGate createCNOTGate() {
        int control = InputRetryHandler.retry(this::readControlQubit);
        int target = InputRetryHandler.retry(this::readTargetQubit);
        return new CNOTGate(new QubitIndex(control), new QubitIndex(target));
    }

    private int readControlQubit() {
        System.out.println(PROMPT_CONTROL_QUBIT);
        String input = camp.nextstep.edu.missionutils.Console.readLine();
        return Integer.parseInt(input.strip());
    }

    private int readTargetQubit() {
        System.out.println(PROMPT_TARGET_QUBIT);
        String input = camp.nextstep.edu.missionutils.Console.readLine();
        return Integer.parseInt(input.strip());
    }

    private QuantumGate createSingleQubitGate(String gateType) {
        int target = InputRetryHandler.retry(this::readTargetQubit);
        QubitIndex targetIndex = new QubitIndex(target);

        if (GATE_X.equals(gateType)) {
            return new PauliXGate(targetIndex);
        }
        if (GATE_H.equals(gateType)) {
            return new HadamardGate(targetIndex);
        }
        if (GATE_Z.equals(gateType)) {
            return new PauliZGate(targetIndex);
        }
        throw new IllegalArgumentException(ERROR_UNSUPPORTED_GATE);
    }

    private boolean shouldContinue() {
        System.out.println(PROMPT_CONTINUE);
        String response = InputRetryHandler.retry(() -> camp.nextstep.edu.missionutils.Console.readLine());
        return CONTINUE_YES.equalsIgnoreCase(response.strip());
    }

    private QuantumCircuit buildCircuit(int qubitCount, List<CircuitStep> steps) {
        return new QuantumCircuitBuilder()
                .withQubits(qubitCount)
                .addSteps(steps)
                .build();
    }

    private void optimizeAndDisplay(QuantumCircuit circuit) {
        System.out.println(BEFORE_OPTIMIZATION);
        System.out.println(CircuitVisualizer.visualize(circuit));
        System.out.println(CircuitVisualizer.generateStepDescription(circuit));

        CircuitOptimizer pipeline = createOptimizationPipeline();
        QuantumCircuit optimized = pipeline.optimize(circuit);

        System.out.println(AFTER_OPTIMIZATION);
        System.out.println(CircuitVisualizer.visualize(optimized));
        System.out.println(CircuitVisualizer.generateStepDescription(optimized));

        printOptimizationResult(circuit, optimized);
        OutputView.printSeparator();
    }

    private CircuitOptimizer createOptimizationPipeline() {
        return new OptimizationPipeline(
                List.of(
                        new RedundantGateRemover(),
                        new IdentityGateRemover()
                )
        );
    }

    private void printOptimizationResult(QuantumCircuit original, QuantumCircuit optimized) {
        System.out.printf(OPTIMIZATION_RESULT_FORMAT + "%n",
                original.getStepCount(),
                optimized.getStepCount());
    }
}
