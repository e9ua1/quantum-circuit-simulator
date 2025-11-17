package quantum.circuit.optimizer;

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
import quantum.circuit.domain.gate.PauliZGate;

class RedundantGateRemoverTest {

    @Test
    @DisplayName("연속된 H 게이트를 제거한다")
    void removeConsecutiveHadamardGates() {
        CircuitOptimizer optimizer = new RedundantGateRemover();
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .build();

        QuantumCircuit optimized = optimizer.optimize(circuit);

        assertThat(optimized.getSteps()).isEmpty();
    }

    @Test
    @DisplayName("연속된 X 게이트를 제거한다")
    void removeConsecutiveXGates() {
        CircuitOptimizer optimizer = new RedundantGateRemover();
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))))
                .build();

        QuantumCircuit optimized = optimizer.optimize(circuit);

        assertThat(optimized.getSteps()).isEmpty();
    }

    @Test
    @DisplayName("연속된 Z 게이트를 제거한다")
    void removeConsecutiveZGates() {
        CircuitOptimizer optimizer = new RedundantGateRemover();
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .addStep(new CircuitStep(List.of(new PauliZGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new PauliZGate(new QubitIndex(0)))))
                .build();

        QuantumCircuit optimized = optimizer.optimize(circuit);

        assertThat(optimized.getSteps()).isEmpty();
    }

    @Test
    @DisplayName("연속되지 않은 게이트는 제거하지 않는다")
    void keepNonConsecutiveGates() {
        CircuitOptimizer optimizer = new RedundantGateRemover();
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .build();

        QuantumCircuit optimized = optimizer.optimize(circuit);

        assertThat(optimized.getSteps()).hasSize(3);
    }

    @Test
    @DisplayName("다른 큐비트의 같은 게이트는 제거하지 않는다")
    void keepGatesOnDifferentQubits() {
        CircuitOptimizer optimizer = new RedundantGateRemover();
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(2)
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(1)))))
                .build();

        QuantumCircuit optimized = optimizer.optimize(circuit);

        assertThat(optimized.getSteps()).hasSize(2);
    }

    @Test
    @DisplayName("4개 연속 게이트 중 2쌍을 제거한다")
    void removeTwoPairsOfGates() {
        CircuitOptimizer optimizer = new RedundantGateRemover();
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))))
                .build();

        QuantumCircuit optimized = optimizer.optimize(circuit);

        assertThat(optimized.getSteps()).isEmpty();
    }

    @Test
    @DisplayName("빈 회로는 빈 회로로 반환한다")
    void emptyCircuitRemainsEmpty() {
        CircuitOptimizer optimizer = new RedundantGateRemover();
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .build();

        QuantumCircuit optimized = optimizer.optimize(circuit);

        assertThat(optimized.getSteps()).isEmpty();
    }

    @Test
    @DisplayName("최적화 이름을 반환한다")
    void returnOptimizationName() {
        CircuitOptimizer optimizer = new RedundantGateRemover();

        String name = optimizer.getOptimizationName();

        assertThat(name).isEqualTo("Redundant Gate Remover");
    }

    @Test
    @DisplayName("3개 연속 게이트 중 첫 2개만 제거한다")
    void removeOnlyFirstPairFromThree() {
        CircuitOptimizer optimizer = new RedundantGateRemover();
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .build();

        QuantumCircuit optimized = optimizer.optimize(circuit);

        assertThat(optimized.getSteps()).hasSize(1);
    }
}
