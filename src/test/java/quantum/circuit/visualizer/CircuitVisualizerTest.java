package quantum.circuit.visualizer;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import quantum.circuit.domain.circuit.CircuitStep;
import quantum.circuit.domain.circuit.QubitIndex;
import quantum.circuit.domain.circuit.QuantumCircuit;
import quantum.circuit.domain.circuit.QuantumCircuitBuilder;
import quantum.circuit.domain.gate.CNOTGate;
import quantum.circuit.domain.gate.HadamardGate;
import quantum.circuit.domain.gate.PauliXGate;

class CircuitVisualizerTest {

    @Test
    @DisplayName("단일 큐비트 단일 게이트 회로를 시각화한다")
    void visualizeSingleQubitSingleGate() {
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))))
                .build();

        String visualization = CircuitVisualizer.visualize(circuit);

        assertThat(visualization).contains("Q0: ─X─");
    }

    @Test
    @DisplayName("다중 큐비트 회로를 시각화한다")
    void visualizeMultipleQubits() {
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(2)
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(1)))))
                .build();

        String visualization = CircuitVisualizer.visualize(circuit);

        assertThat(visualization).contains("Q0:");
        assertThat(visualization).contains("Q1:");
    }

    @Test
    @DisplayName("CNOT 게이트를 시각화한다")
    void visualizeCNOTGate() {
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(2)
                .addStep(new CircuitStep(List.of(new CNOTGate(new QubitIndex(0), new QubitIndex(1)))))
                .build();

        String visualization = CircuitVisualizer.visualize(circuit);

        assertThat(visualization).contains("●");
        assertThat(visualization).contains("X");
    }

    @Test
    @DisplayName("회로 요약 정보를 생성한다")
    void generateSummary() {
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(2)
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))))
                .build();

        String summary = CircuitVisualizer.generateSummary(circuit);

        assertThat(summary).contains("2 qubits");
        assertThat(summary).contains("1 step");
    }

    @Test
    @DisplayName("Step별 상세 설명을 생성한다")
    void generateStepDescription() {
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(2)
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(1)))))
                .build();

        String description = CircuitVisualizer.generateStepDescription(circuit);

        assertThat(description).contains("Step 1:");
        assertThat(description).contains("Step 2:");
        assertThat(description).contains("X(Q0)");
        assertThat(description).contains("H(Q1)");
    }

    @Test
    @DisplayName("빈 회로를 시각화한다")
    void visualizeEmptyCircuit() {
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .build();

        String visualization = CircuitVisualizer.visualize(circuit);

        assertThat(visualization).contains("Q0:");
    }
}
