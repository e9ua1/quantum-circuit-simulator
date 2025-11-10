package quantum.circuit.benchmark;

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

class CircuitComparatorTest {

    @Test
    @DisplayName("두 회로를 비교한다")
    void compareCircuits() {
        CircuitComparator comparator = new CircuitComparator();
        QuantumCircuit original = createOriginalCircuit();
        QuantumCircuit optimized = createOptimizedCircuit();

        ComparisonReport report = comparator.compare(original, optimized);

        assertThat(report).isNotNull();
    }

    @Test
    @DisplayName("비교 결과에 게이트 감소율이 포함된다")
    void comparisonIncludesGateReduction() {
        CircuitComparator comparator = new CircuitComparator();
        QuantumCircuit original = createOriginalCircuit();
        QuantumCircuit optimized = createOptimizedCircuit();

        ComparisonReport report = comparator.compare(original, optimized);

        assertThat(report.getGateReduction()).isEqualTo(100.0);
    }

    @Test
    @DisplayName("비교 결과에 깊이 감소율이 포함된다")
    void comparisonIncludesDepthReduction() {
        CircuitComparator comparator = new CircuitComparator();
        QuantumCircuit original = createOriginalCircuit();
        QuantumCircuit optimized = createOptimizedCircuit();

        ComparisonReport report = comparator.compare(original, optimized);

        assertThat(report.getDepthReduction()).isEqualTo(100.0);
    }

    @Test
    @DisplayName("동일한 회로를 비교하면 감소율이 0%이다")
    void compareSameCircuits() {
        CircuitComparator comparator = new CircuitComparator();
        QuantumCircuit circuit = createOriginalCircuit();

        ComparisonReport report = comparator.compare(circuit, circuit);

        assertThat(report.getGateReduction()).isEqualTo(0.0);
        assertThat(report.getDepthReduction()).isEqualTo(0.0);
    }

    private QuantumCircuit createOriginalCircuit() {
        return new QuantumCircuitBuilder()
                .withQubits(2)
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .build();
    }

    private QuantumCircuit createOptimizedCircuit() {
        return new QuantumCircuitBuilder()
                .withQubits(2)
                .build();
    }
}
