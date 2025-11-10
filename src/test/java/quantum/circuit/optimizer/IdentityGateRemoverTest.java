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

class IdentityGateRemoverTest {

    @Test
    @DisplayName("X-X 항등 연산을 제거한다")
    void removeXXIdentity() {
        CircuitOptimizer optimizer = new IdentityGateRemover();
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))))
                .build();

        QuantumCircuit optimized = optimizer.optimize(circuit);

        assertThat(optimized.getSteps()).isEmpty();
    }

    @Test
    @DisplayName("H-H 항등 연산을 제거한다")
    void removeHHIdentity() {
        CircuitOptimizer optimizer = new IdentityGateRemover();
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .build();

        QuantumCircuit optimized = optimizer.optimize(circuit);

        assertThat(optimized.getSteps()).isEmpty();
    }

    @Test
    @DisplayName("Z-Z 항등 연산을 제거한다")
    void removeZZIdentity() {
        CircuitOptimizer optimizer = new IdentityGateRemover();
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .addStep(new CircuitStep(List.of(new PauliZGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new PauliZGate(new QubitIndex(0)))))
                .build();

        QuantumCircuit optimized = optimizer.optimize(circuit);

        assertThat(optimized.getSteps()).isEmpty();
    }

    @Test
    @DisplayName("비연속 게이트는 유지한다")
    void keepNonConsecutiveGates() {
        CircuitOptimizer optimizer = new IdentityGateRemover();
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))))
                .build();

        QuantumCircuit optimized = optimizer.optimize(circuit);

        assertThat(optimized.getSteps()).hasSize(3);
    }

    @Test
    @DisplayName("빈 회로는 빈 회로로 반환한다")
    void emptyCircuitRemainsEmpty() {
        CircuitOptimizer optimizer = new IdentityGateRemover();
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .build();

        QuantumCircuit optimized = optimizer.optimize(circuit);

        assertThat(optimized.getSteps()).isEmpty();
    }

    @Test
    @DisplayName("최적화 이름을 반환한다")
    void returnOptimizationName() {
        CircuitOptimizer optimizer = new IdentityGateRemover();

        String name = optimizer.getOptimizationName();

        assertThat(name).isEqualTo("Identity Gate Remover");
    }

    @Test
    @DisplayName("4개 연속 X 게이트는 모두 제거한다")
    void removeFourConsecutiveX() {
        CircuitOptimizer optimizer = new IdentityGateRemover();
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))))
                .build();

        QuantumCircuit optimized = optimizer.optimize(circuit);

        assertThat(optimized.getSteps()).isEmpty();
    }

    @Test
    @DisplayName("다른 큐비트의 같은 게이트는 제거하지 않는다")
    void keepGatesOnDifferentQubits() {
        CircuitOptimizer optimizer = new IdentityGateRemover();
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(2)
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(1)))))
                .build();

        QuantumCircuit optimized = optimizer.optimize(circuit);

        assertThat(optimized.getSteps()).hasSize(2);
    }
}
