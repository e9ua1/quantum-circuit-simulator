package quantum.circuit.analyzer;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import quantum.circuit.analyzer.metric.CircuitDepthMetric;
import quantum.circuit.analyzer.metric.CircuitMetric;
import quantum.circuit.analyzer.metric.GateCountMetric;
import quantum.circuit.domain.circuit.CircuitStep;
import quantum.circuit.domain.circuit.QubitIndex;
import quantum.circuit.domain.circuit.QuantumCircuit;
import quantum.circuit.domain.circuit.QuantumCircuitBuilder;
import quantum.circuit.domain.gate.HadamardGate;
import quantum.circuit.domain.gate.PauliXGate;

class CircuitAnalyzerEnhancedTest {

    @Test
    @DisplayName("기본 생성자로 생성하면 4개의 메트릭을 가진다")
    void defaultConstructorHasFourMetrics() {
        CircuitAnalyzer analyzer = new CircuitAnalyzer();

        assertThat(analyzer.getMetricCount()).isEqualTo(4);
    }

    @Test
    @DisplayName("커스텀 메트릭 리스트로 분석기를 생성할 수 있다")
    void customMetrics() {
        List<CircuitMetric> customMetrics = List.of(
                new GateCountMetric(),
                new CircuitDepthMetric()
        );

        CircuitAnalyzer analyzer = new CircuitAnalyzer(customMetrics);

        assertThat(analyzer.getMetricCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("커스텀 메트릭으로 회로를 분석한다")
    void analyzeWithCustomMetrics() {
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .build();

        List<CircuitMetric> customMetrics = List.of(
                new GateCountMetric(),
                new CircuitDepthMetric()
        );
        CircuitAnalyzer analyzer = new CircuitAnalyzer(customMetrics);

        AnalysisReport report = analyzer.analyze(circuit);

        assertThat(report.gateCount()).isEqualTo(2);
        assertThat(report.depth()).isEqualTo(2);
    }

    @Test
    @DisplayName("인스턴스 메서드와 정적 메서드 모두 동일한 결과를 반환한다")
    void instanceAndStaticMethodsReturnSameResult() {
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))))
                .build();

        CircuitAnalyzer analyzer = new CircuitAnalyzer();
        AnalysisReport instanceResult = analyzer.analyze(circuit);
        AnalysisReport staticResult = CircuitAnalyzer.analyze(circuit);

        assertThat(instanceResult.depth()).isEqualTo(staticResult.depth());
        assertThat(instanceResult.gateCount()).isEqualTo(staticResult.gateCount());
        assertThat(instanceResult.complexity()).isEqualTo(staticResult.complexity());
        assertThat(instanceResult.entanglementDegree()).isEqualTo(staticResult.entanglementDegree());
    }
}
