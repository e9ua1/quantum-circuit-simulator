package quantum.circuit.exporter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import quantum.circuit.domain.circuit.CircuitStep;
import quantum.circuit.domain.circuit.QubitIndex;
import quantum.circuit.domain.circuit.QuantumCircuit;
import quantum.circuit.domain.circuit.QuantumCircuitBuilder;
import quantum.circuit.domain.gate.CNOTGate;
import quantum.circuit.domain.gate.HadamardGate;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CircuitResultExporter - 단계별 상태 출력 테스트")
class CircuitResultExporterStepByStepTest {

    private static final String TEST_OUTPUT_PATH = "test_output/step_by_step_result.json";

    @AfterEach
    void cleanup() throws IOException {
        Path path = Path.of(TEST_OUTPUT_PATH);
        if (Files.exists(path)) {
            Files.delete(path);
        }
    }

    @Test
    @DisplayName("단계별 상태를 JSON으로 출력한다")
    void exportStepByStep() throws IOException {
        // given
        QuantumCircuit circuit = createBellStateCircuit();

        // when
        CircuitResultExporter.exportStepByStep(circuit, "Bell State", TEST_OUTPUT_PATH);

        // then
        assertThat(Files.exists(Path.of(TEST_OUTPUT_PATH))).isTrue();
        String json = Files.readString(Path.of(TEST_OUTPUT_PATH));

        // JSON 구조 검증
        assertThat(json).contains("\"circuit_name\": \"Bell State\"");
        assertThat(json).contains("\"qubit_count\": 2");
        assertThat(json).contains("\"step_states\"");
    }

    @Test
    @DisplayName("step_states 배열에 초기 상태가 포함된다")
    void includesInitialState() throws IOException {
        // given
        QuantumCircuit circuit = createBellStateCircuit();

        // when
        CircuitResultExporter.exportStepByStep(circuit, "Bell State", TEST_OUTPUT_PATH);

        // then
        String json = Files.readString(Path.of(TEST_OUTPUT_PATH));

        // 초기 상태 검증
        assertThat(json).contains("\"step\": 0");
        assertThat(json).contains("\"description\": \"Initial State\"");
        assertThat(json).contains("\"00\": 1.0"); // 초기 상태는 |00⟩
    }

    @Test
    @DisplayName("step_states 배열에 각 게이트 적용 후 상태가 포함된다")
    void includesStatesAfterEachGate() throws IOException {
        // given
        QuantumCircuit circuit = createBellStateCircuit();

        // when
        CircuitResultExporter.exportStepByStep(circuit, "Bell State", TEST_OUTPUT_PATH);

        // then
        String json = Files.readString(Path.of(TEST_OUTPUT_PATH));

        // H 게이트 적용 후 상태 검증
        assertThat(json).contains("\"step\": 1");
        assertThat(json).contains("\"description\": \"After H(Q0)\"");

        // CNOT 게이트 적용 후 상태 검증
        assertThat(json).contains("\"step\": 2");
        assertThat(json).contains("\"description\": \"After CNOT(Q0→Q1)\"");
    }

    @Test
    @DisplayName("각 step_state에 qubit_probabilities가 포함된다")
    void includesQubitProbabilitiesInEachStep() throws IOException {
        // given
        QuantumCircuit circuit = createBellStateCircuit();

        // when
        CircuitResultExporter.exportStepByStep(circuit, "Bell State", TEST_OUTPUT_PATH);

        // then
        String json = Files.readString(Path.of(TEST_OUTPUT_PATH));

        // qubit_probabilities 존재 확인
        assertThat(json).contains("\"qubit_probabilities\"");
        // 각 큐비트의 확률 키 확인
        assertThat(json).contains("\"0\":");
        assertThat(json).contains("\"1\":");
    }

    @Test
    @DisplayName("각 step_state에 system_state가 포함된다")
    void includesSystemStateInEachStep() throws IOException {
        // given
        QuantumCircuit circuit = createBellStateCircuit();

        // when
        CircuitResultExporter.exportStepByStep(circuit, "Bell State", TEST_OUTPUT_PATH);

        // then
        String json = Files.readString(Path.of(TEST_OUTPUT_PATH));

        // system_state 존재 확인
        assertThat(json).contains("\"system_state\"");
        // 2큐비트 상태 키 확인
        assertThat(json).contains("\"00\":");
        assertThat(json).contains("\"01\":");
        assertThat(json).contains("\"10\":");
        assertThat(json).contains("\"11\":");
    }

    @Test
    @DisplayName("총 단계 수는 회로의 스텝 수 + 1(초기 상태)이다")
    void totalStepsIsCircuitStepsPlusOne() throws IOException {
        // given
        QuantumCircuit circuit = createBellStateCircuit();
        int expectedSteps = circuit.getStepCount() + 1; // 초기 상태 포함

        // when
        CircuitResultExporter.exportStepByStep(circuit, "Bell State", TEST_OUTPUT_PATH);

        // then
        String json = Files.readString(Path.of(TEST_OUTPUT_PATH));

        // step 0, 1, 2가 모두 존재하는지 확인
        for (int i = 0; i < expectedSteps; i++) {
            assertThat(json).contains("\"step\": " + i);
        }
    }

    @Test
    @DisplayName("단일 게이트 회로도 정상적으로 처리된다")
    void handlesSingleGateCircuit() throws IOException {
        // given
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .addStep(new CircuitStep(List.of(
                        new HadamardGate(new QubitIndex(0))
                )))
                .build();

        // when
        CircuitResultExporter.exportStepByStep(circuit, "Single H Gate", TEST_OUTPUT_PATH);

        // then
        String json = Files.readString(Path.of(TEST_OUTPUT_PATH));

        // 초기 상태 + H 게이트 = 2개 step
        assertThat(json).contains("\"step\": 0");
        assertThat(json).contains("\"step\": 1");
        assertThat(json).doesNotContain("\"step\": 2");
    }

    private QuantumCircuit createBellStateCircuit() {
        return new QuantumCircuitBuilder()
                .withQubits(2)
                .addStep(new CircuitStep(List.of(
                        new HadamardGate(new QubitIndex(0))
                )))
                .addStep(new CircuitStep(List.of(
                        new CNOTGate(new QubitIndex(0), new QubitIndex(1))
                )))
                .build();
    }
}
