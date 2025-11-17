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

class EntanglementDegreeTest {

    @Test
    @DisplayName("빈 회로의 얽힘 정도는 0이다")
    void emptyCircuitEntanglement() {
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .build();

        int degree = EntanglementDegree.calculate(circuit);

        assertThat(degree).isEqualTo(0);
    }

    @Test
    @DisplayName("단일 큐비트 게이트만 있으면 얽힘 정도는 0이다")
    void singleQubitGatesNoEntanglement() {
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(2)
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(1)))))
                .build();

        int degree = EntanglementDegree.calculate(circuit);

        assertThat(degree).isEqualTo(0);
    }

    @Test
    @DisplayName("CNOT 게이트 1개의 얽힘 정도는 1이다")
    void singleCNOTEntanglement() {
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(2)
                .addStep(new CircuitStep(List.of(new CNOTGate(new QubitIndex(0), new QubitIndex(1)))))
                .build();

        int degree = EntanglementDegree.calculate(circuit);

        assertThat(degree).isEqualTo(1);
    }

    @Test
    @DisplayName("CNOT 게이트 2개의 얽힘 정도는 2이다")
    void twoCNOTEntanglement() {
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(3)
                .addStep(new CircuitStep(List.of(new CNOTGate(new QubitIndex(0), new QubitIndex(1)))))
                .addStep(new CircuitStep(List.of(new CNOTGate(new QubitIndex(1), new QubitIndex(2)))))
                .build();

        int degree = EntanglementDegree.calculate(circuit);

        assertThat(degree).isEqualTo(2);
    }

    @Test
    @DisplayName("단일 큐비트 게이트와 CNOT이 섞여있으면 CNOT만 계산한다")
    void mixedGatesEntanglement() {
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(2)
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new CNOTGate(new QubitIndex(0), new QubitIndex(1)))))
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(1)))))
                .build();

        int degree = EntanglementDegree.calculate(circuit);

        assertThat(degree).isEqualTo(1);
    }

    @Test
    @DisplayName("얽힘 정도는 항상 0 이상이다")
    void entanglementIsNonNegative() {
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .build();

        int degree = EntanglementDegree.calculate(circuit);

        assertThat(degree).isGreaterThanOrEqualTo(0);
    }

    @Test
    @DisplayName("Bell State 회로의 얽힘 정도는 1이다")
    void bellStateEntanglement() {
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(2)
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new CNOTGate(new QubitIndex(0), new QubitIndex(1)))))
                .build();

        int degree = EntanglementDegree.calculate(circuit);

        assertThat(degree).isEqualTo(1);
    }

    @Test
    @DisplayName("GHZ State 회로의 얽힘 정도는 2이다")
    void ghzStateEntanglement() {
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(3)
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new CNOTGate(new QubitIndex(0), new QubitIndex(1)))))
                .addStep(new CircuitStep(List.of(new CNOTGate(new QubitIndex(1), new QubitIndex(2)))))
                .build();

        int degree = EntanglementDegree.calculate(circuit);

        assertThat(degree).isEqualTo(2);
    }
}
