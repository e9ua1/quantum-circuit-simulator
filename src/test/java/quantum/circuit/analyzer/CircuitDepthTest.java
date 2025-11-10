package quantum.circuit.analyzer;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import quantum.circuit.domain.circuit.CircuitStep;
import quantum.circuit.domain.circuit.QubitIndex;
import quantum.circuit.domain.circuit.QuantumCircuit;
import quantum.circuit.domain.circuit.QuantumCircuitBuilder;
import quantum.circuit.domain.gate.HadamardGate;
import quantum.circuit.domain.gate.PauliXGate;

class CircuitDepthTest {

    @Test
    @DisplayName("빈 회로의 깊이는 0이다")
    void emptyCircuitDepth() {
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .build();

        int depth = CircuitDepth.calculate(circuit);

        assertThat(depth).isEqualTo(0);
    }

    @Test
    @DisplayName("단일 Step 회로의 깊이는 1이다")
    void singleStepCircuitDepth() {
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))))
                .build();

        int depth = CircuitDepth.calculate(circuit);

        assertThat(depth).isEqualTo(1);
    }

    @Test
    @DisplayName("3개 Step 회로의 깊이는 3이다")
    void threeStepCircuitDepth() {
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))))
                .build();

        int depth = CircuitDepth.calculate(circuit);

        assertThat(depth).isEqualTo(3);
    }

    @Test
    @DisplayName("다중 큐비트 회로의 깊이를 계산한다")
    void multiQubitCircuitDepth() {
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(2)
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(1)))))
                .build();

        int depth = CircuitDepth.calculate(circuit);

        assertThat(depth).isEqualTo(2);
    }

    @Test
    @DisplayName("회로 깊이는 항상 0 이상이다")
    void depthIsNonNegative() {
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .build();

        int depth = CircuitDepth.calculate(circuit);

        assertThat(depth).isGreaterThanOrEqualTo(0);
    }
}
