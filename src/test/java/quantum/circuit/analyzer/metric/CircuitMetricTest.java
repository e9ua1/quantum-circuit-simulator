package quantum.circuit.analyzer.metric;

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

class CircuitMetricTest {

    @Test
    @DisplayName("GateCountMetric은 게이트 개수를 계산한다")
    void gateCountMetric() {
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .build();

        CircuitMetric metric = new GateCountMetric();

        assertThat(metric.calculate(circuit)).isEqualTo(2);
        assertThat(metric.getMetricName()).isEqualTo("Gate Count");
    }

    @Test
    @DisplayName("CircuitDepthMetric은 회로 깊이를 계산한다")
    void circuitDepthMetric() {
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .build();

        CircuitMetric metric = new CircuitDepthMetric();

        assertThat(metric.calculate(circuit)).isEqualTo(2);
        assertThat(metric.getMetricName()).isEqualTo("Circuit Depth");
    }

    @Test
    @DisplayName("ComplexityMetric은 회로 복잡도를 계산한다")
    void complexityMetric() {
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(2)
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new CNOTGate(new QubitIndex(0), new QubitIndex(1)))))
                .build();

        CircuitMetric metric = new ComplexityMetric();

        assertThat(metric.calculate(circuit)).isEqualTo(3);
        assertThat(metric.getMetricName()).isEqualTo("Circuit Complexity");
    }

    @Test
    @DisplayName("EntanglementMetric은 얽힘 정도를 계산한다")
    void entanglementMetric() {
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(2)
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new CNOTGate(new QubitIndex(0), new QubitIndex(1)))))
                .build();

        CircuitMetric metric = new EntanglementMetric();

        assertThat(metric.calculate(circuit)).isEqualTo(1);
        assertThat(metric.getMetricName()).isEqualTo("Entanglement Degree");
    }

    @Test
    @DisplayName("빈 회로에 대해 모든 메트릭은 0을 반환한다")
    void emptyCircuitMetrics() {
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .build();

        assertThat(new GateCountMetric().calculate(circuit)).isEqualTo(0);
        assertThat(new CircuitDepthMetric().calculate(circuit)).isEqualTo(0);
        assertThat(new ComplexityMetric().calculate(circuit)).isEqualTo(0);
        assertThat(new EntanglementMetric().calculate(circuit)).isEqualTo(0);
    }
}
