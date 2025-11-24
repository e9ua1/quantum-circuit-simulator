package quantum.circuit.visualizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

import quantum.circuit.domain.circuit.QuantumCircuit;
import quantum.circuit.exporter.CircuitResultExporter;

public class PythonVisualizer {

    private static final String PYTHON_SCRIPT_PATH = "src/main/python/main.py";
    private static final String DEFAULT_OUTPUT_JSON = "output/circuit_result.json";
    private static final String ERROR_SCRIPT_NOT_FOUND = "Python 스크립트를 찾을 수 없습니다: ";
    private static final String ERROR_PYTHON_EXECUTION_FAILED = "Python 시각화 실행에 실패했습니다.";

    public static void visualize(QuantumCircuit circuit, String circuitName) {
        CircuitResultExporter.exportStepByStep(circuit, circuitName, DEFAULT_OUTPUT_JSON);
        executePythonScript();
    }

    private static void executePythonScript() {
        validateScriptExists();

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "python3",
                    PYTHON_SCRIPT_PATH,
                    DEFAULT_OUTPUT_JSON
            );

            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            printProcessOutput(process);

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException(ERROR_PYTHON_EXECUTION_FAILED + " (exit code: " + exitCode + ")");
            }

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(ERROR_PYTHON_EXECUTION_FAILED, e);
        }
    }

    private static void validateScriptExists() {
        if (!Files.exists(Path.of(PYTHON_SCRIPT_PATH))) {
            throw new IllegalStateException(ERROR_SCRIPT_NOT_FOUND + PYTHON_SCRIPT_PATH);
        }
    }

    private static void printProcessOutput(Process process) throws IOException {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }
    }
}
