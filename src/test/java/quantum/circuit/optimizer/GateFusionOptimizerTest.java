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

class GateFusionOptimizerTest {

    @Test
    @DisplayName("현재는 최적화를 수행하지 않는다")
    void noOptimizationYet() {
        CircuitOptimizer optimizer = new GateFusionOptimizer();
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new PauliZGate(new QubitIndex(0)))))
                .build();

        QuantumCircuit optimized = optimizer.optimize(circuit);

        assertThat(optimized.getSteps()).hasSize(2);
    }

    @Test
    @DisplayName("빈 회로는 빈 회로로 반환한다")
    void emptyCircuitRemainsEmpty() {
        CircuitOptimizer optimizer = new GateFusionOptimizer();
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .build();

        QuantumCircuit optimized = optimizer.optimize(circuit);

        assertThat(optimized.getSteps()).isEmpty();
    }

    @Test
    @DisplayName("최적화 이름을 반환한다")
    void returnOptimizationName() {
        CircuitOptimizer optimizer = new GateFusionOptimizer();

        String name = optimizer.getOptimizationName();

        assertThat(name).isEqualTo("Gate Fusion Optimizer");
    }

    @Test
    @DisplayName("단일 게이트 회로는 그대로 반환한다")
    void singleGateCircuitUnchanged() {
        CircuitOptimizer optimizer = new GateFusionOptimizer();
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .build();

        QuantumCircuit optimized = optimizer.optimize(circuit);

        assertThat(optimized.getSteps()).hasSize(1);
    }

    @Test
    @DisplayName("다중 큐비트 회로를 처리한다")
    void handleMultiQubitCircuit() {
        CircuitOptimizer optimizer = new GateFusionOptimizer();
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(2)
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(1)))))
                .build();

        QuantumCircuit optimized = optimizer.optimize(circuit);

        assertThat(optimized).isNotNull();
        assertThat(optimized.getQubitCount()).isEqualTo(2);
    }
}
