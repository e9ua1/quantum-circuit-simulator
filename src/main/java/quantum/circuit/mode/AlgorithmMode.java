package quantum.circuit.mode;

import quantum.circuit.algorithm.AlgorithmFactory;
import quantum.circuit.algorithm.QuantumAlgorithm;
import quantum.circuit.domain.circuit.QuantumCircuit;
import quantum.circuit.domain.state.QuantumState;
import quantum.circuit.util.InputRetryHandler;
import quantum.circuit.view.OutputView;
import quantum.circuit.visualizer.CircuitVisualizer;
import quantum.circuit.visualizer.PythonVisualizer;
import quantum.circuit.visualizer.StateVisualizer;

public class AlgorithmMode {

    private static final String MODE_HEADER = """
            
            === 알고리즘 라이브러리 ===
            사용 가능한 알고리즘:
            """;
    private static final String BELL_STATE_INFO = "1. Bell State (2큐비트) - 최대 얽힘 상태 생성";
    private static final String GHZ_STATE_INFO = "2. GHZ State (3큐비트) - 3큐비트 얽힘 상태";
    private static final String QFT_INFO = "3. QFT (2큐비트) - 양자 푸리에 변환";
    private static final String GROVER_INFO = "4. Grover's Search (2큐비트) - 양자 검색 알고리즘";
    private static final String DEUTSCH_JOZSA_INFO = "5. Deutsch-Jozsa (2큐비트) - 함수 판별 알고리즘";
    private static final String PROMPT_ALGORITHM = "\n알고리즘을 선택하세요 (예: BELL_STATE):";
    private static final String ALGORITHM_HEADER_FORMAT = "\n=== %s Algorithm ===";
    private static final String DESCRIPTION_FORMAT = "설명: %s";
    private static final String VISUALIZATION_START = "\n🎨 단계별 시각화 생성 중...";
    private static final String VISUALIZATION_COMPLETE = "\n✅ 시각화 완료!";
    private static final String VISUALIZATION_INFO = """
            📊 생성된 파일:
              - output/bloch_sphere.png (최종 상태)
              - output/histogram.png (최종 상태)
              - output/bloch_steps.png (단계별 궤적) ⭐
              - output/histogram_steps.png (단계별 비교) ⭐
            💡 확인: open output/*.png""";

    private final AlgorithmFactory algorithmFactory;

    public AlgorithmMode() {
        this(new AlgorithmFactory());
    }

    public AlgorithmMode(AlgorithmFactory algorithmFactory) {
        this.algorithmFactory = algorithmFactory;
    }

    public void start() {
        printAlgorithmList();
        QuantumAlgorithm algorithm = selectAlgorithm();
        executeAlgorithm(algorithm);
    }

    private void printAlgorithmList() {
        System.out.println(MODE_HEADER);
        System.out.println(BELL_STATE_INFO);
        System.out.println(GHZ_STATE_INFO);
        System.out.println(QFT_INFO);
        System.out.println(GROVER_INFO);
        System.out.println(DEUTSCH_JOZSA_INFO);
    }

    private QuantumAlgorithm selectAlgorithm() {
        return InputRetryHandler.retry(() -> {
            System.out.println(PROMPT_ALGORITHM);
            String algorithmName = camp.nextstep.edu.missionutils.Console.readLine();
            return algorithmFactory.create(algorithmName);
        });
    }

    private void executeAlgorithm(QuantumAlgorithm algorithm) {
        printAlgorithmInfo(algorithm);

        QuantumCircuit circuit = algorithm.build(algorithm.getRequiredQubits());
        printCircuit(circuit);

        QuantumState state = circuit.execute();
        printState(state);

        // Python 시각화 자동 실행 (단계별 + 정확한 얽힘 상태)
        visualizeWithPython(circuit, state, algorithm.getName());
    }

    private void printAlgorithmInfo(QuantumAlgorithm algorithm) {
        System.out.printf(ALGORITHM_HEADER_FORMAT + "%n", algorithm.getName());
        System.out.printf(DESCRIPTION_FORMAT + "%n", algorithm.getDescription());
    }

    private void printCircuit(QuantumCircuit circuit) {
        OutputView.printSeparator();
        System.out.println(CircuitVisualizer.visualize(circuit));
    }

    private void printState(QuantumState state) {
        System.out.println();
        System.out.println(StateVisualizer.visualizeQubitProbabilities(state));
        OutputView.printSeparator();
    }

    private void visualizeWithPython(QuantumCircuit circuit, QuantumState state, String algorithmName) {
        try {
            System.out.println(VISUALIZATION_START);
            PythonVisualizer.visualize(circuit, state, algorithmName);
            System.out.println(VISUALIZATION_COMPLETE);
            System.out.println(VISUALIZATION_INFO);
        } catch (Exception e) {
            System.err.println("\n⚠️  Python 시각화 실패: " + e.getMessage());
            System.err.println("💡 Python 환경을 확인하세요: ./install.sh");
        }
    }
}
