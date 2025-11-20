package quantum.circuit.exporter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import quantum.circuit.domain.circuit.CircuitStep;
import quantum.circuit.domain.circuit.QubitIndex;
import quantum.circuit.domain.circuit.QuantumCircuit;
import quantum.circuit.domain.gate.CNOTGate;
import quantum.circuit.domain.gate.QuantumGate;
import quantum.circuit.domain.state.QuantumState;

public class CircuitResultExporter {

    private static final String ERROR_NULL_PATH = "경로는 null일 수 없습니다.";
    private static final String ERROR_EMPTY_PATH = "경로는 비어있을 수 없습니다.";
    private static final String ERROR_WRITE_FAILED = "JSON 파일 쓰기에 실패했습니다.";
    private static final String DEFAULT_CIRCUIT_NAME = "Quantum Circuit";

    public static void exportToJson(QuantumCircuit circuit, QuantumState state, String outputPath) {
        exportToJson(circuit, state, DEFAULT_CIRCUIT_NAME, outputPath);
    }

    public static void exportToJson(
            QuantumCircuit circuit,
            QuantumState state,
            String circuitName,
            String outputPath
    ) {
        validatePath(outputPath);

        String json = buildJson(circuit, state, circuitName);
        writeToFile(json, outputPath);
    }

    private static void validatePath(String path) {
        if (path == null) {
            throw new IllegalArgumentException(ERROR_NULL_PATH);
        }
        if (path.isBlank()) {
            throw new IllegalArgumentException(ERROR_EMPTY_PATH);
        }
    }

    private static String buildJson(QuantumCircuit circuit, QuantumState state, String circuitName) {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append(String.format("  \"circuit_name\": \"%s\",\n", escapeJson(circuitName)));
        json.append(String.format("  \"qubit_count\": %d,\n", circuit.getQubitCount()));
        json.append(buildStepsJson(circuit));
        json.append(buildQubitProbabilitiesJson(state));
        json.append(buildSystemStateJson(state));
        json.append("}");
        return json.toString();
    }

    private static String buildStepsJson(QuantumCircuit circuit) {
        StringBuilder json = new StringBuilder();
        json.append("  \"steps\": [\n");

        var steps = circuit.getSteps();
        for (int i = 0; i < steps.size(); i++) {
            CircuitStep step = steps.get(i);
            json.append("    {\n");
            json.append(String.format("      \"step\": %d,\n", i));
            json.append("      \"gates\": [");

            String gateDescriptions = step.getGates().stream()
                    .map(CircuitResultExporter::describeGate)
                    .map(desc -> "\"" + desc + "\"")
                    .collect(Collectors.joining(", "));

            json.append(gateDescriptions);
            json.append("]\n");
            json.append("    }");
            if (i < steps.size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }

        json.append("  ],\n");
        return json.toString();
    }

    private static String describeGate(QuantumGate gate) {
        if (gate instanceof CNOTGate) {
            CNOTGate cnot = (CNOTGate) gate;
            return String.format("CNOT(Q%d→Q%d)",
                    cnot.getControl().getValue(),
                    cnot.getTarget().getValue());
        }

        var affected = gate.getAffectedQubits();
        if (!affected.isEmpty()) {
            QubitIndex target = affected.iterator().next();
            return String.format("%s(Q%d)", gate.getName(), target.getValue());
        }

        return gate.getName();
    }

    private static String buildQubitProbabilitiesJson(QuantumState state) {
        StringBuilder json = new StringBuilder();
        json.append("  \"qubit_probabilities\": {\n");

        int qubitCount = state.getQubitCount();
        for (int i = 0; i < qubitCount; i++) {
            double probability = state.getProbabilityOfOne(new QubitIndex(i)).getValue();
            json.append(String.format("    \"%d\": %.6f", i, probability));
            if (i < qubitCount - 1) {
                json.append(",");
            }
            json.append("\n");
        }

        json.append("  },\n");
        return json.toString();
    }

    private static String buildSystemStateJson(QuantumState state) {
        StringBuilder json = new StringBuilder();
        json.append("  \"system_state\": {\n");

        Map<String, Double> systemState = calculateSystemState(state);

        int count = 0;
        int total = systemState.size();
        for (Map.Entry<String, Double> entry : systemState.entrySet()) {
            json.append(String.format("    \"%s\": %.6f", entry.getKey(), entry.getValue()));
            count++;
            if (count < total) {
                json.append(",");
            }
            json.append("\n");
        }

        json.append("  }\n");
        return json.toString();
    }

    private static Map<String, Double> calculateSystemState(QuantumState state) {
        int qubitCount = state.getQubitCount();
        int numStates = 1 << qubitCount; // 2^qubitCount

        Map<String, Double> systemState = new HashMap<>();

        for (int i = 0; i < numStates; i++) {
            final int stateIndex = i; // final 변수로 복사
            String binaryState = IntStream.range(0, qubitCount)
                    .map(bit -> (stateIndex >> (qubitCount - 1 - bit)) & 1)
                    .mapToObj(String::valueOf)
                    .collect(Collectors.joining());

            // 간단한 근사: 각 큐비트 독립적으로 계산
            // 실제로는 얽힘을 고려해야 하지만, 현재는 기본 구현
            double probability = calculateStateProbability(state, binaryState);
            systemState.put(binaryState, probability);
        }

        return systemState;
    }

    private static double calculateStateProbability(QuantumState state, String binaryState) {
        // 간단한 근사: 각 큐비트의 확률을 곱함
        // 얽힘 상태에서는 정확하지 않지만, 기본 동작을 위한 구현
        double probability = 1.0;

        for (int i = 0; i < binaryState.length(); i++) {
            char bit = binaryState.charAt(i);
            QubitIndex index = new QubitIndex(i);
            double probOne = state.getProbabilityOfOne(index).getValue();

            if (bit == '1') {
                probability *= probOne;
            } else {
                probability *= (1.0 - probOne);
            }
        }

        return probability;
    }

    private static String escapeJson(String str) {
        return str.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private static void writeToFile(String content, String path) {
        try {
            Path filePath = Path.of(path);
            Path parent = filePath.getParent();

            // parent가 null이 아닐 때만 디렉토리 생성
            if (parent != null) {
                Files.createDirectories(parent);
            }

            Files.writeString(filePath, content);
        } catch (IOException e) {
            throw new RuntimeException(ERROR_WRITE_FAILED, e);
        }
    }
}
