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

class CircuitAnalyzerTest {

    @Test
    @DisplayName("빈 회로를 분석한다")
    void analyzeEmptyCircuit() {
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .build();

        AnalysisReport report = CircuitAnalyzer.analyze(circuit);

        assertThat(report.getDepth()).isEqualTo(0);
        assertThat(report.getGateCount()).isEqualTo(0);
        assertThat(report.getComplexity()).isEqualTo(0);
        assertThat(report.getEntanglementDegree()).isEqualTo(0);
    }

    @Test
    @DisplayName("단일 게이트 회로를 분석한다")
    void analyzeSingleGateCircuit() {
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))))
                .build();

        AnalysisReport report = CircuitAnalyzer.analyze(circuit);

        assertThat(report.getDepth()).isEqualTo(1);
        assertThat(report.getGateCount()).isEqualTo(1);
        assertThat(report.getComplexity()).isEqualTo(1);
        assertThat(report.getEntanglementDegree()).isEqualTo(0);
    }

    @Test
    @DisplayName("Bell State 회로를 분석한다")
    void analyzeBellStateCircuit() {
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(2)
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new CNOTGate(new QubitIndex(0), new QubitIndex(1)))))
                .build();

        AnalysisReport report = CircuitAnalyzer.analyze(circuit);

        assertThat(report.getDepth()).isEqualTo(2);
        assertThat(report.getGateCount()).isEqualTo(2);
        assertThat(report.getComplexity()).isEqualTo(3);
        assertThat(report.getEntanglementDegree()).isEqualTo(1);
    }

    @Test
    @DisplayName("GHZ State 회로를 분석한다")
    void analyzeGHZStateCircuit() {
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(3)
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new CNOTGate(new QubitIndex(0), new QubitIndex(1)))))
                .addStep(new CircuitStep(List.of(new CNOTGate(new QubitIndex(1), new QubitIndex(2)))))
                .build();

        AnalysisReport report = CircuitAnalyzer.analyze(circuit);

        assertThat(report.getDepth()).isEqualTo(3);
        assertThat(report.getGateCount()).isEqualTo(3);
        assertThat(report.getComplexity()).isEqualTo(5);
        assertThat(report.getEntanglementDegree()).isEqualTo(2);
    }

    @Test
    @DisplayName("복잡한 회로를 분석한다")
    void analyzeComplexCircuit() {
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(2)
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new CNOTGate(new QubitIndex(0), new QubitIndex(1)))))
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(1)))))
                .build();

        AnalysisReport report = CircuitAnalyzer.analyze(circuit);

        assertThat(report.getDepth()).isEqualTo(4);
        assertThat(report.getGateCount()).isEqualTo(4);
        assertThat(report.getComplexity()).isEqualTo(5);
        assertThat(report.getEntanglementDegree()).isEqualTo(1);
    }

    @Test
    @DisplayName("분석 리포트는 null이 아니다")
    void reportIsNotNull() {
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .build();

        AnalysisReport report = CircuitAnalyzer.analyze(circuit);

        assertThat(report).isNotNull();
    }

    @Test
    @DisplayName("한 Step에 여러 게이트가 있는 회로를 분석한다")
    void analyzeMultipleGatesInOneStep() {
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(2)
                .addStep(new CircuitStep(List.of(
                        new PauliXGate(new QubitIndex(0)),
                        new HadamardGate(new QubitIndex(1))
                )))
                .build();

        AnalysisReport report = CircuitAnalyzer.analyze(circuit);

        assertThat(report.getDepth()).isEqualTo(1);
        assertThat(report.getGateCount()).isEqualTo(2);
        assertThat(report.getComplexity()).isEqualTo(2);
        assertThat(report.getEntanglementDegree()).isEqualTo(0);
    }
}
