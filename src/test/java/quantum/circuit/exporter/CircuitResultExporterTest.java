package quantum.circuit.exporter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

class CircuitResultExporterTest {

    private static final String TEST_OUTPUT_PATH = "test_output.json";

    @AfterEach
    void cleanup() throws IOException {
        Files.deleteIfExists(Path.of(TEST_OUTPUT_PATH));
    }

    @Test
    @DisplayName("Bell State 회로를 JSON으로 출력한다")
    void exportBellStateToJson() throws IOException {
        // given
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(2)
                .addStep(new CircuitStep(List.of(
                        new HadamardGate(new QubitIndex(0))
                )))
                .addStep(new CircuitStep(List.of(
                        new CNOTGate(new QubitIndex(0), new QubitIndex(1))
                )))
                .build();

        QuantumState state = circuit.execute();

        // when
        CircuitResultExporter.exportToJson(circuit, state, TEST_OUTPUT_PATH);

        // then
        String content = Files.readString(Path.of(TEST_OUTPUT_PATH));
        assertThat(content).contains("\"circuit_name\"");
        assertThat(content).contains("\"qubit_count\": 2");
        assertThat(content).contains("\"qubit_probabilities\"");
        assertThat(content).contains("\"system_state\"");
    }

    @Test
    @DisplayName("회로 이름을 지정할 수 있다")
    void exportWithCircuitName() throws IOException {
        // given
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(2)
                .build();

        QuantumState state = circuit.execute();

        // when
        CircuitResultExporter.exportToJson(circuit, state, "Bell State", TEST_OUTPUT_PATH);

        // then
        String content = Files.readString(Path.of(TEST_OUTPUT_PATH));
        assertThat(content).contains("\"circuit_name\": \"Bell State\"");
    }

    @Test
    @DisplayName("null 경로는 예외를 발생시킨다")
    void throwExceptionWhenPathIsNull() {
        // given
        QuantumCircuit circuit = new QuantumCircuitBuilder().withQubits(2).build();
        QuantumState state = circuit.execute();

        // when & then
        assertThrows(IllegalArgumentException.class,
                () -> CircuitResultExporter.exportToJson(circuit, state, null));
    }

    @Test
    @DisplayName("빈 경로는 예외를 발생시킨다")
    void throwExceptionWhenPathIsEmpty() {
        // given
        QuantumCircuit circuit = new QuantumCircuitBuilder().withQubits(2).build();
        QuantumState state = circuit.execute();

        // when & then
        assertThrows(IllegalArgumentException.class,
                () -> CircuitResultExporter.exportToJson(circuit, state, ""));
    }
}
