package quantum.circuit;

import quantum.circuit.mode.AlgorithmMode;
import quantum.circuit.mode.BenchmarkMode;
import quantum.circuit.mode.OptimizationMode;
import quantum.circuit.util.InputRetryHandler;

public class Application {

    private static final String WELCOME_MESSAGE = """
            ===================================
            Quantum Circuit Simulator
            ===================================
            """;
    private static final String MODE_SELECTION_MENU = """
            모드를 선택하세요:
            1. 자유 모드 (Free Mode)
            2. 알고리즘 라이브러리 (Algorithm Library)
            3. 최적화 모드 (Optimization Mode)
            4. 벤치마크 모드 (Benchmark Mode)
            """;
    private static final String PROMPT_MODE = "선택:";
    private static final String ERROR_INVALID_MODE = "올바른 모드를 선택해주세요.";
    private static final int FREE_MODE = 1;
    private static final int ALGORITHM_MODE = 2;
    private static final int OPTIMIZATION_MODE = 3;
    private static final int BENCHMARK_MODE = 4;

    public static void main(String[] args) {
        System.out.println(WELCOME_MESSAGE);
        System.out.println(MODE_SELECTION_MENU);

        int mode = selectMode();
        runMode(mode);
    }

    private static int selectMode() {
        return InputRetryHandler.retry(() -> {
            System.out.println(PROMPT_MODE);
            String input = camp.nextstep.edu.missionutils.Console.readLine();
            return parseMode(input);
        });
    }

    private static int parseMode(String input) {
        try {
            int mode = Integer.parseInt(input.strip());
            validateMode(mode);
            return mode;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ERROR_INVALID_MODE);
        }
    }

    private static void validateMode(int mode) {
        if (mode != FREE_MODE && mode != ALGORITHM_MODE && mode != OPTIMIZATION_MODE && mode != BENCHMARK_MODE) {
            throw new IllegalArgumentException(ERROR_INVALID_MODE);
        }
    }

    private static void runMode(int mode) {
        if (mode == FREE_MODE) {
            new QuantumCircuitSimulator().start();
        }
        if (mode == ALGORITHM_MODE) {
            new AlgorithmMode().start();
        }
        if (mode == OPTIMIZATION_MODE) {
            new OptimizationMode().start();
        }
        if (mode == BENCHMARK_MODE) {
            new BenchmarkMode().run();
        }
    }
}
