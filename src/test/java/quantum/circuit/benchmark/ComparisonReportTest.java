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

class ComparisonReportTest {

    @Test
    @DisplayName("비교 리포트를 생성한다")
    void createComparisonReport() {
        QuantumCircuit original = createOriginalCircuit();
        QuantumCircuit optimized = createOptimizedCircuit();

        ComparisonReport report = new ComparisonReport(original, optimized);

        assertThat(report).isNotNull();
    }

    @Test
    @DisplayName("원본 회로의 게이트 개수를 반환한다")
    void getOriginalGateCount() {
        QuantumCircuit original = createOriginalCircuit();
        QuantumCircuit optimized = createOptimizedCircuit();

        ComparisonReport report = new ComparisonReport(original, optimized);

        assertThat(report.getOriginalGateCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("최적화된 회로의 게이트 개수를 반환한다")
    void getOptimizedGateCount() {
        QuantumCircuit original = createOriginalCircuit();
        QuantumCircuit optimized = createOptimizedCircuit();

        ComparisonReport report = new ComparisonReport(original, optimized);

        assertThat(report.getOptimizedGateCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("게이트 감소율을 계산한다")
    void calculateGateReduction() {
        QuantumCircuit original = createOriginalCircuit();
        QuantumCircuit optimized = createOptimizedCircuit();

        ComparisonReport report = new ComparisonReport(original, optimized);

        assertThat(report.getGateReduction()).isEqualTo(100.0);
    }

    @Test
    @DisplayName("원본 회로의 깊이를 반환한다")
    void getOriginalDepth() {
        QuantumCircuit original = createOriginalCircuit();
        QuantumCircuit optimized = createOptimizedCircuit();

        ComparisonReport report = new ComparisonReport(original, optimized);

        assertThat(report.getOriginalDepth()).isEqualTo(2);
    }

    @Test
    @DisplayName("최적화된 회로의 깊이를 반환한다")
    void getOptimizedDepth() {
        QuantumCircuit original = createOriginalCircuit();
        QuantumCircuit optimized = createOptimizedCircuit();

        ComparisonReport report = new ComparisonReport(original, optimized);

        assertThat(report.getOptimizedDepth()).isEqualTo(0);
    }

    @Test
    @DisplayName("깊이 감소율을 계산한다")
    void calculateDepthReduction() {
        QuantumCircuit original = createOriginalCircuit();
        QuantumCircuit optimized = createOptimizedCircuit();

        ComparisonReport report = new ComparisonReport(original, optimized);

        assertThat(report.getDepthReduction()).isEqualTo(100.0);
    }

    @Test
    @DisplayName("게이트가 감소하지 않은 경우 0%를 반환한다")
    void noReductionReturnsZero() {
        QuantumCircuit circuit = createOriginalCircuit();

        ComparisonReport report = new ComparisonReport(circuit, circuit);

        assertThat(report.getGateReduction()).isEqualTo(0.0);
        assertThat(report.getDepthReduction()).isEqualTo(0.0);
    }

    @Test
    @DisplayName("빈 회로의 감소율은 0%이다")
    void emptyCircuitReduction() {
        QuantumCircuit empty = new QuantumCircuitBuilder().withQubits(1).build();

        ComparisonReport report = new ComparisonReport(empty, empty);

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
