package quantum.circuit.analyzer;

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

class CircuitComplexityTest {

    @Test
    @DisplayName("빈 회로의 복잡도는 0이다")
    void emptyCircuitComplexity() {
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .build();

        int complexity = CircuitComplexity.calculate(circuit);

        assertThat(complexity).isEqualTo(0);
    }

    @Test
    @DisplayName("단일 큐비트 게이트의 복잡도는 1이다")
    void singleQubitGateComplexity() {
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))))
                .build();

        int complexity = CircuitComplexity.calculate(circuit);

        assertThat(complexity).isEqualTo(1);
    }

    @Test
    @DisplayName("CNOT 게이트의 복잡도는 2이다")
    void cnotGateComplexity() {
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(2)
                .addStep(new CircuitStep(List.of(new CNOTGate(new QubitIndex(0), new QubitIndex(1)))))
                .build();

        int complexity = CircuitComplexity.calculate(circuit);

        assertThat(complexity).isEqualTo(2);
    }

    @Test
    @DisplayName("단일 큐비트 게이트 3개의 복잡도는 3이다")
    void threeGatesComplexity() {
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))))
                .build();

        int complexity = CircuitComplexity.calculate(circuit);

        assertThat(complexity).isEqualTo(3);
    }

    @Test
    @DisplayName("혼합 게이트의 복잡도를 계산한다")
    void mixedGatesComplexity() {
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(2)
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new CNOTGate(new QubitIndex(0), new QubitIndex(1)))))
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(1)))))
                .build();

        int complexity = CircuitComplexity.calculate(circuit);

        assertThat(complexity).isEqualTo(4);
    }

    @Test
    @DisplayName("한 Step에 여러 게이트가 있으면 복잡도를 합산한다")
    void multipleGatesInOneStepComplexity() {
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(2)
                .addStep(new CircuitStep(List.of(
                        new PauliXGate(new QubitIndex(0)),
                        new HadamardGate(new QubitIndex(1))
                )))
                .build();

        int complexity = CircuitComplexity.calculate(circuit);

        assertThat(complexity).isEqualTo(2);
    }

    @Test
    @DisplayName("복잡도는 항상 0 이상이다")
    void complexityIsNonNegative() {
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .build();

        int complexity = CircuitComplexity.calculate(circuit);

        assertThat(complexity).isGreaterThanOrEqualTo(0);
    }

    @Test
    @DisplayName("복잡한 회로의 복잡도를 계산한다")
    void complexCircuitComplexity() {
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(3)
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new CNOTGate(new QubitIndex(0), new QubitIndex(1)))))
                .addStep(new CircuitStep(List.of(new CNOTGate(new QubitIndex(1), new QubitIndex(2)))))
                .build();

        int complexity = CircuitComplexity.calculate(circuit);

        assertThat(complexity).isEqualTo(5);
    }
}
