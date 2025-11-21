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
import quantum.circuit.domain.state.QuantumState;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

@DisplayName("CircuitResultExporter - 얽힘 상태 정확도 테스트")
class CircuitResultExporterEntanglementTest {

    private static final String TEST_OUTPUT_PATH = "test_output/entanglement_test.json";
    private static final double EPSILON = 0.001; // 오차 허용 범위

    @AfterEach
    void cleanup() throws IOException {
        Path path = Path.of(TEST_OUTPUT_PATH);
        if (Files.exists(path)) {
            Files.delete(path);
        }
    }

    @Test
    @DisplayName("Bell State는 |00⟩=50%, |11⟩=50%를 정확히 반환한다")
    void bellStateAccuracy() throws IOException {
        // given
        QuantumCircuit circuit = createBellStateCircuit();
        QuantumState state = circuit.execute();

        // when
        CircuitResultExporter.exportToJson(circuit, state, "Bell State", TEST_OUTPUT_PATH);

        // then
        String json = Files.readString(Path.of(TEST_OUTPUT_PATH));

        // Bell State의 정확한 확률 검증
        // |00⟩ = 50%, |11⟩ = 50%, |01⟩ = 0%, |10⟩ = 0%
        assertThat(json).contains("\"00\": 0.5");
        assertThat(json).contains("\"11\": 0.5");
        
        // |01⟩과 |10⟩은 0에 가까워야 함
        assertThat(json).containsPattern("\"01\": 0\\.0+[0-9]*");
        assertThat(json).containsPattern("\"10\": 0\\.0+[0-9]*");
    }

    @Test
    @DisplayName("GHZ State는 |000⟩=50%, |111⟩=50%를 정확히 반환한다")
    void ghzStateAccuracy() throws IOException {
        // given
        QuantumCircuit circuit = createGHZStateCircuit();
        QuantumState state = circuit.execute();

        // when
        CircuitResultExporter.exportToJson(circuit, state, "GHZ State", TEST_OUTPUT_PATH);

        // then
        String json = Files.readString(Path.of(TEST_OUTPUT_PATH));

        // GHZ State의 정확한 확률 검증
        // |000⟩ = 50%, |111⟩ = 50%, 나머지 = 0%
        assertThat(json).contains("\"000\": 0.5");
        assertThat(json).contains("\"111\": 0.5");
        
        // 나머지 6개 상태는 모두 0에 가까워야 함
        assertThat(json).containsPattern("\"001\": 0\\.0+[0-9]*");
        assertThat(json).containsPattern("\"010\": 0\\.0+[0-9]*");
    }

    @Test
    @DisplayName("단일 큐비트 H 게이트는 |0⟩=50%, |1⟩=50%를 정확히 반환한다")
    void singleQubitSuperposition() throws IOException {
        // given
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .addStep(new CircuitStep(List.of(
                        new HadamardGate(new QubitIndex(0))
                )))
                .build();
        QuantumState state = circuit.execute();

        // when
        CircuitResultExporter.exportToJson(circuit, state, "Single H", TEST_OUTPUT_PATH);

        // then
        String json = Files.readString(Path.of(TEST_OUTPUT_PATH));

        // |0⟩ = 50%, |1⟩ = 50%
        assertThat(json).contains("\"0\": 0.5");
        assertThat(json).contains("\"1\": 0.5");
    }

    @Test
    @DisplayName("초기 상태는 |00⟩=100%를 정확히 반환한다")
    void initialStateAccuracy() throws IOException {
        // given
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(2)
                .build();
        QuantumState state = circuit.execute();

        // when
        CircuitResultExporter.exportToJson(circuit, state, "Initial", TEST_OUTPUT_PATH);

        // then
        String json = Files.readString(Path.of(TEST_OUTPUT_PATH));

        // |00⟩ = 100%, 나머지 = 0%
        assertThat(json).contains("\"00\": 1.0");
        assertThat(json).containsPattern("\"01\": 0\\.0+[0-9]*");
        assertThat(json).containsPattern("\"10\": 0\\.0+[0-9]*");
        assertThat(json).containsPattern("\"11\": 0\\.0+[0-9]*");
    }

    @Test
    @DisplayName("단계별 출력도 정확한 얽힘 확률을 반환한다")
    void stepByStepEntanglementAccuracy() throws IOException {
        // given
        QuantumCircuit circuit = createBellStateCircuit();

        // when
        CircuitResultExporter.exportStepByStep(circuit, "Bell State", TEST_OUTPUT_PATH);

        // then
        String json = Files.readString(Path.of(TEST_OUTPUT_PATH));

        // Step 0 (초기): |00⟩ = 100%
        assertThat(json).contains("\"step\": 0");
        
        // Step 2 (CNOT 후): |00⟩ = 50%, |11⟩ = 50%
        assertThat(json).contains("\"step\": 2");
        // 최종 상태에서 Bell State 확률 검증
        assertThat(json).containsPattern("\"00\": 0\\.5");
        assertThat(json).containsPattern("\"11\": 0\\.5");
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

    private QuantumCircuit createGHZStateCircuit() {
        return new QuantumCircuitBuilder()
                .withQubits(3)
                .addStep(new CircuitStep(List.of(
                        new HadamardGate(new QubitIndex(0))
                )))
                .addStep(new CircuitStep(List.of(
                        new CNOTGate(new QubitIndex(0), new QubitIndex(1))
                )))
                .addStep(new CircuitStep(List.of(
                        new CNOTGate(new QubitIndex(1), new QubitIndex(2))
                )))
                .build();
    }
}
