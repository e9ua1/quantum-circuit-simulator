package quantum.circuit.validator;

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

class QubitRangeValidatorTest {

    @Test
    @DisplayName("유효한 큐비트 범위는 통과한다")
    void validQubitRange() {
        CircuitValidator validator = new QubitRangeValidator();
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(2)
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(1)))))
                .build();

        ValidationResult result = validator.validate(circuit);

        assertThat(result.isValid()).isTrue();
    }

    @Test
    @DisplayName("범위를 벗어난 큐비트는 실패한다")
    void invalidQubitRange() {
        CircuitValidator validator = new QubitRangeValidator();
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(2)
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(5)))))
                .build();

        ValidationResult result = validator.validate(circuit);

        assertThat(result.isValid()).isFalse();
        assertThat(result.getMessage()).contains("범위");
    }

    @Test
    @DisplayName("CNOT 게이트의 제어 큐비트가 범위를 벗어나면 실패한다")
    void cnotControlOutOfRange() {
        CircuitValidator validator = new QubitRangeValidator();
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(2)
                .addStep(new CircuitStep(List.of(new CNOTGate(new QubitIndex(5), new QubitIndex(1)))))
                .build();

        ValidationResult result = validator.validate(circuit);

        assertThat(result.isValid()).isFalse();
    }

    @Test
    @DisplayName("CNOT 게이트의 타겟 큐비트가 범위를 벗어나면 실패한다")
    void cnotTargetOutOfRange() {
        CircuitValidator validator = new QubitRangeValidator();
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(2)
                .addStep(new CircuitStep(List.of(new CNOTGate(new QubitIndex(0), new QubitIndex(5)))))
                .build();

        ValidationResult result = validator.validate(circuit);

        assertThat(result.isValid()).isFalse();
    }

    @Test
    @DisplayName("빈 회로는 통과한다")
    void emptyCircuitPasses() {
        CircuitValidator validator = new QubitRangeValidator();
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .build();

        ValidationResult result = validator.validate(circuit);

        assertThat(result.isValid()).isTrue();
    }

    @Test
    @DisplayName("검증 이름을 반환한다")
    void returnValidationName() {
        CircuitValidator validator = new QubitRangeValidator();

        String name = validator.getValidationName();

        assertThat(name).isEqualTo("Qubit Range Validator");
    }

    @Test
    @DisplayName("모든 게이트가 유효한 범위면 통과한다")
    void allGatesInValidRange() {
        CircuitValidator validator = new QubitRangeValidator();
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(3)
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new CNOTGate(new QubitIndex(0), new QubitIndex(1)))))
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(2)))))
                .build();

        ValidationResult result = validator.validate(circuit);

        assertThat(result.isValid()).isTrue();
    }
}
