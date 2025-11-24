package quantum.circuit.visualizer;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import quantum.circuit.algorithm.BellStateAlgorithm;
import quantum.circuit.domain.circuit.QuantumCircuit;
import quantum.circuit.domain.state.QuantumState;

@DisplayName("PythonVisualizer - 단계별 시각화 통합 테스트")
class PythonVisualizerIntegrationTest {

    private static final String TEST_OUTPUT_PATH = "output/circuit_result.json";

    @AfterEach
    void cleanup() throws IOException {
        Path path = Path.of(TEST_OUTPUT_PATH);
        if (Files.exists(path)) {
            Files.delete(path);
        }
    }

    @Test
    @DisplayName("알고리즘 시각화 시 단계별 JSON이 생성된다")
    void generateStepByStepJson() throws IOException {
        // given
        BellStateAlgorithm algorithm = new BellStateAlgorithm();
        QuantumCircuit circuit = algorithm.build(algorithm.getRequiredQubits());

        // when
        PythonVisualizer.visualize(circuit, algorithm.getName());

        // then
        assertThat(Files.exists(Path.of(TEST_OUTPUT_PATH))).isTrue();
        String json = Files.readString(Path.of(TEST_OUTPUT_PATH));

        // step_states 배열 존재 확인
        assertThat(json).contains("\"step_states\"");
        
        // 초기 상태 확인
        assertThat(json).contains("\"step\": 0");
        assertThat(json).contains("\"description\": \"Initial State\"");
        
        // Bell State 이름 확인
        assertThat(json).contains("\"circuit_name\": \"Bell State\"");
    }

    @Test
    @DisplayName("생성된 JSON에 정확한 얽힘 확률이 포함된다")
    void includesAccurateEntanglementProbabilities() throws IOException {
        // given
        BellStateAlgorithm algorithm = new BellStateAlgorithm();
        QuantumCircuit circuit = algorithm.build(algorithm.getRequiredQubits());

        // when
        PythonVisualizer.visualize(circuit, algorithm.getName());

        // then
        String json = Files.readString(Path.of(TEST_OUTPUT_PATH));

        // Bell State의 정확한 확률 검증
        // 최종 상태에서 |00⟩ = 50%, |11⟩ = 50%
        assertThat(json).containsPattern("\"00\": 0\\.5");
        assertThat(json).containsPattern("\"11\": 0\\.5");
    }

    @Test
    @DisplayName("단계별 상태가 모두 포함된다")
    void includesAllSteps() throws IOException {
        // given
        BellStateAlgorithm algorithm = new BellStateAlgorithm();
        QuantumCircuit circuit = algorithm.build(algorithm.getRequiredQubits());

        // when
        PythonVisualizer.visualize(circuit, algorithm.getName());

        // then
        String json = Files.readString(Path.of(TEST_OUTPUT_PATH));

        // Bell State는 3개 단계: 초기, H 게이트, CNOT
        assertThat(json).contains("\"step\": 0"); // 초기
        assertThat(json).contains("\"step\": 1"); // H
        assertThat(json).contains("\"step\": 2"); // CNOT
    }

    @Test
    @DisplayName("각 단계마다 qubit_probabilities와 system_state가 포함된다")
    void eachStepHasProbabilities() throws IOException {
        // given
        BellStateAlgorithm algorithm = new BellStateAlgorithm();
        QuantumCircuit circuit = algorithm.build(algorithm.getRequiredQubits());

        // when
        PythonVisualizer.visualize(circuit, algorithm.getName());

        // then
        String json = Files.readString(Path.of(TEST_OUTPUT_PATH));

        // 각 단계마다 두 속성이 모두 존재
        assertThat(json).contains("\"qubit_probabilities\"");
        assertThat(json).contains("\"system_state\"");
    }
}
