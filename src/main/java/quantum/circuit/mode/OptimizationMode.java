package quantum.circuit.mode;

import java.util.List;

import quantum.circuit.domain.circuit.CircuitStep;
import quantum.circuit.domain.circuit.QuantumCircuit;
import quantum.circuit.domain.circuit.QuantumCircuitBuilder;
import quantum.circuit.optimizer.CircuitOptimizer;
import quantum.circuit.optimizer.IdentityGateRemover;
import quantum.circuit.optimizer.OptimizationPipeline;
import quantum.circuit.optimizer.RedundantGateRemover;
import quantum.circuit.util.CircuitStepBuilder;
import quantum.circuit.util.InputRetryHandler;
import quantum.circuit.view.OutputView;
import quantum.circuit.visualizer.CircuitVisualizer;

public class OptimizationMode {

    private static final String MODE_HEADER = """
            
            === 최적화 모드 ===
            회로를 입력하면 자동으로 최적화를 수행합니다.
            """;
    private static final String PROMPT_QUBIT_COUNT = "큐비트 개수를 입력하세요:";
    private static final String BEFORE_OPTIMIZATION = "\n=== 최적화 전 회로 ===";
    private static final String AFTER_OPTIMIZATION = "\n=== 최적화 후 회로 ===";
    private static final String OPTIMIZATION_RESULT_FORMAT = "\n최적화 결과: %d Step → %d Step";

    public void start() {
        System.out.println(MODE_HEADER);

        int qubitCount = InputRetryHandler.retry(this::readQubitCount);
        List<CircuitStep> steps = CircuitStepBuilder.buildSteps(qubitCount);
        QuantumCircuit circuit = buildCircuit(qubitCount, steps);

        optimizeAndDisplay(circuit);
    }

    private int readQubitCount() {
        System.out.println(PROMPT_QUBIT_COUNT);
        String input = camp.nextstep.edu.missionutils.Console.readLine();
        return Integer.parseInt(input.strip());
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
