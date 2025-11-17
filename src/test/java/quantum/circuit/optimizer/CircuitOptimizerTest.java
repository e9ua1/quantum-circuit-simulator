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

class CircuitOptimizerTest {

    @Test
    @DisplayName("최적화기는 회로를 최적화한다")
    void optimizeCircuit() {
        CircuitOptimizer optimizer = new TestOptimizer();
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .build();

        QuantumCircuit optimized = optimizer.optimize(circuit);

        assertThat(optimized).isNotNull();
    }

    @Test
    @DisplayName("최적화기는 이름을 반환한다")
    void getOptimizationName() {
        CircuitOptimizer optimizer = new TestOptimizer();

        String name = optimizer.getOptimizationName();

        assertThat(name).isNotBlank();
    }

    private static class TestOptimizer implements CircuitOptimizer {
        @Override
        public QuantumCircuit optimize(QuantumCircuit circuit) {
            return circuit;
        }

        @Override
        public String getOptimizationName() {
            return "Test Optimizer";
        }
    }
}
