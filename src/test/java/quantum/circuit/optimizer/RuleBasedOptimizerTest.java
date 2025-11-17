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
import quantum.circuit.optimizer.rule.rules.ConsecutiveSameGateRule;

class RuleBasedOptimizerTest {

    @Test
    @DisplayName("규칙 기반 최적화기를 생성한다")
    void createRuleBasedOptimizer() {
        CircuitOptimizer optimizer = new RuleBasedOptimizer(
                List.of(new ConsecutiveSameGateRule()),
                "Test Optimizer"
        );

        assertThat(optimizer).isNotNull();
        assertThat(optimizer.getOptimizationName()).isEqualTo("Test Optimizer");
    }

    @Test
    @DisplayName("X-X 패턴을 제거한다")
    void removeXXPattern() {
        CircuitOptimizer optimizer = new RuleBasedOptimizer(
                List.of(new ConsecutiveSameGateRule()),
                "Test Optimizer"
        );
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))))
                .build();

        QuantumCircuit optimized = optimizer.optimize(circuit);

        assertThat(optimized.getSteps()).isEmpty();
    }

    @Test
    @DisplayName("H-H 패턴을 제거한다")
    void removeHHPattern() {
        CircuitOptimizer optimizer = new RuleBasedOptimizer(
                List.of(new ConsecutiveSameGateRule()),
                "Test Optimizer"
        );
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .build();

        QuantumCircuit optimized = optimizer.optimize(circuit);

        assertThat(optimized.getSteps()).isEmpty();
    }

    @Test
    @DisplayName("여러 규칙을 동시에 적용한다")
    void applyMultipleRules() {
        CircuitOptimizer optimizer = new RuleBasedOptimizer(
                List.of(new ConsecutiveSameGateRule()),
                "Test Optimizer"
        );
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .build();

        QuantumCircuit optimized = optimizer.optimize(circuit);

        assertThat(optimized.getSteps()).isEmpty();
    }

    @Test
    @DisplayName("최적화할 수 없는 패턴은 유지한다")
    void keepNonOptimizablePatterns() {
        CircuitOptimizer optimizer = new RuleBasedOptimizer(
                List.of(new ConsecutiveSameGateRule()),
                "Test Optimizer"
        );
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .build();

        QuantumCircuit optimized = optimizer.optimize(circuit);

        assertThat(optimized.getSteps()).hasSize(2);
    }

    @Test
    @DisplayName("다른 큐비트의 같은 게이트는 제거하지 않는다")
    void keepGatesOnDifferentQubits() {
        CircuitOptimizer optimizer = new RuleBasedOptimizer(
                List.of(new ConsecutiveSameGateRule()),
                "Test Optimizer"
        );
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(2)
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(1)))))
                .build();

        QuantumCircuit optimized = optimizer.optimize(circuit);

        assertThat(optimized.getSteps()).hasSize(2);
    }

    @Test
    @DisplayName("4개 연속 게이트를 모두 제거한다")
    void removeFourConsecutiveGates() {
        CircuitOptimizer optimizer = new RuleBasedOptimizer(
                List.of(new ConsecutiveSameGateRule()),
                "Test Optimizer"
        );
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .addStep(new CircuitStep(List.of(new PauliZGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new PauliZGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new PauliZGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new PauliZGate(new QubitIndex(0)))))
                .build();

        QuantumCircuit optimized = optimizer.optimize(circuit);

        assertThat(optimized.getSteps()).isEmpty();
    }

    @Test
    @DisplayName("빈 회로는 빈 회로로 반환한다")
    void emptyCircuitRemainsEmpty() {
        CircuitOptimizer optimizer = new RuleBasedOptimizer(
                List.of(new ConsecutiveSameGateRule()),
                "Test Optimizer"
        );
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .build();

        QuantumCircuit optimized = optimizer.optimize(circuit);

        assertThat(optimized.getSteps()).isEmpty();
    }

    @Test
    @DisplayName("규칙 개수를 반환한다")
    void returnRuleCount() {
        RuleBasedOptimizer optimizer = new RuleBasedOptimizer(
                List.of(new ConsecutiveSameGateRule()),
                "Test Optimizer"
        );

        int count = optimizer.getRuleCount();

        assertThat(count).isEqualTo(1);
    }
}
