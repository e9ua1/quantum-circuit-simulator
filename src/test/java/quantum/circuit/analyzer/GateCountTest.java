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
import quantum.circuit.domain.gate.PauliZGate;

class GateCountTest {

    @Test
    @DisplayName("빈 회로의 게이트 개수는 0이다")
    void emptyCircuitGateCount() {
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .build();

        int count = GateCount.calculate(circuit);

        assertThat(count).isEqualTo(0);
    }

    @Test
    @DisplayName("단일 게이트 회로의 게이트 개수는 1이다")
    void singleGateCircuitCount() {
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))))
                .build();

        int count = GateCount.calculate(circuit);

        assertThat(count).isEqualTo(1);
    }

    @Test
    @DisplayName("3개 게이트 회로의 게이트 개수는 3이다")
    void threeGateCircuitCount() {
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new PauliZGate(new QubitIndex(0)))))
                .build();

        int count = GateCount.calculate(circuit);

        assertThat(count).isEqualTo(3);
    }

    @Test
    @DisplayName("한 Step에 여러 게이트가 있으면 모두 계산한다")
    void multipleGatesInOneStep() {
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(2)
                .addStep(new CircuitStep(List.of(
                        new PauliXGate(new QubitIndex(0)),
                        new HadamardGate(new QubitIndex(1))
                )))
                .build();

        int count = GateCount.calculate(circuit);

        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("CNOT 게이트도 1개로 계산한다")
    void cnotGateCountsAsOne() {
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(2)
                .addStep(new CircuitStep(List.of(new CNOTGate(new QubitIndex(0), new QubitIndex(1)))))
                .build();

        int count = GateCount.calculate(circuit);

        assertThat(count).isEqualTo(1);
    }

    @Test
    @DisplayName("복잡한 회로의 게이트 개수를 계산한다")
    void complexCircuitGateCount() {
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(2)
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new CNOTGate(new QubitIndex(0), new QubitIndex(1)))))
                .addStep(new CircuitStep(List.of(
                        new PauliXGate(new QubitIndex(0)),
                        new PauliZGate(new QubitIndex(1))
                )))
                .build();

        int count = GateCount.calculate(circuit);

        assertThat(count).isEqualTo(4);
    }

    @Test
    @DisplayName("게이트 개수는 항상 0 이상이다")
    void gateCountIsNonNegative() {
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .build();

        int count = GateCount.calculate(circuit);

        assertThat(count).isGreaterThanOrEqualTo(0);
    }
}
