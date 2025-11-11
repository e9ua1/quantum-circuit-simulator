package quantum.circuit.mode;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import quantum.circuit.algorithm.AlgorithmFactory;
import quantum.circuit.algorithm.QuantumAlgorithm;
import quantum.circuit.benchmark.BenchmarkReport;
import quantum.circuit.benchmark.BenchmarkRunner;
import quantum.circuit.benchmark.ResultCollector;
import quantum.circuit.domain.circuit.QuantumCircuit;
import quantum.circuit.view.InputView;
import quantum.circuit.view.OutputView;

public class BenchmarkMode {

    private static final String MODE_NAME = "Benchmark Mode";
    private static final String MODE_DESCRIPTION = "여러 알고리즘을 벤치마크하여 성능을 비교합니다";

    private final AlgorithmFactory algorithmFactory;

    public BenchmarkMode() {
        this.algorithmFactory = new AlgorithmFactory();
    }

    public void run() {
        OutputView.printHeader(MODE_NAME);
        OutputView.printMessage(MODE_DESCRIPTION);

        OutputView.printMessage("\n비교할 알고리즘 개수를 입력하세요 (2-5):");
        int algorithmCount = InputView.readInt();

        int qubitCount = InputView.readQubitCount();

        Map<String, QuantumCircuit> circuits = collectCircuits(algorithmCount, qubitCount);

        runBenchmark(circuits);
    }

    private Map<String, QuantumCircuit> collectCircuits(int count, int qubitCount) {
        Map<String, QuantumCircuit> circuits = new LinkedHashMap<>();

        for (int i = 1; i <= count; i++) {
            OutputView.printMessage("\n알고리즘 " + i + " 이름을 입력하세요:");
            String algorithmName = InputView.readAlgorithmName();
            QuantumAlgorithm algorithm = algorithmFactory.create(algorithmName);
            QuantumCircuit circuit = algorithm.build(qubitCount);
            circuits.put(algorithmName, circuit);
        }

        return circuits;
    }

    private void runBenchmark(Map<String, QuantumCircuit> circuits) {
        ResultCollector collector = new ResultCollector();
        BenchmarkRunner runner = new BenchmarkRunner(List.of(collector));

        BenchmarkReport report = runner.runBenchmark(circuits);

        OutputView.printMessage("\n" + report.toString());
        printRecommendation(report);
    }

    private void printRecommendation(BenchmarkReport report) {
        String fastest = report.getFastestCircuit();
        String efficient = report.getMostEfficientCircuit();

        if (fastest != null) {
            OutputView.printMessage("\n가장 빠른 회로: " + fastest);
        }
        if (efficient != null) {
            OutputView.printMessage("가장 효율적인 회로: " + efficient);
        }
    }

    public String getModeName() {
        return MODE_NAME;
    }

    public String getModeDescription() {
        return MODE_DESCRIPTION;
    }
}
