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

class GateCompatibilityValidatorTest {

    @Test
    @DisplayName("유효한 게이트 조합은 통과한다")
    void validGateCombination() {
        CircuitValidator validator = new GateCompatibilityValidator();
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(2)
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new CNOTGate(new QubitIndex(0), new QubitIndex(1)))))
                .build();

        ValidationResult result = validator.validate(circuit);

        assertThat(result.isValid()).isTrue();
    }

    @Test
    @DisplayName("CNOT 게이트의 제어와 타겟이 같으면 실패한다")
    void cnotSameQubitFails() {
        CircuitValidator validator = new GateCompatibilityValidator();
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(2)
                .addStep(new CircuitStep(List.of(new CNOTGate(new QubitIndex(0), new QubitIndex(0)))))
                .build();

        ValidationResult result = validator.validate(circuit);

        assertThat(result.isValid()).isFalse();
        assertThat(result.getMessage()).contains("호환");
    }

    @Test
    @DisplayName("단일 큐비트 게이트만 있으면 통과한다")
    void singleQubitGatesPass() {
        CircuitValidator validator = new GateCompatibilityValidator();
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(2)
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(1)))))
                .build();

        ValidationResult result = validator.validate(circuit);

        assertThat(result.isValid()).isTrue();
    }

    @Test
    @DisplayName("빈 회로는 통과한다")
    void emptyCircuitPasses() {
        CircuitValidator validator = new GateCompatibilityValidator();
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .build();

        ValidationResult result = validator.validate(circuit);

        assertThat(result.isValid()).isTrue();
    }

    @Test
    @DisplayName("검증 이름을 반환한다")
    void returnValidationName() {
        CircuitValidator validator = new GateCompatibilityValidator();

        String name = validator.getValidationName();

        assertThat(name).isEqualTo("Gate Compatibility Validator");
    }

    @Test
    @DisplayName("여러 CNOT 게이트가 모두 유효하면 통과한다")
    void multipleCNOTGatesPass() {
        CircuitValidator validator = new GateCompatibilityValidator();
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(3)
                .addStep(new CircuitStep(List.of(new CNOTGate(new QubitIndex(0), new QubitIndex(1)))))
                .addStep(new CircuitStep(List.of(new CNOTGate(new QubitIndex(1), new QubitIndex(2)))))
                .build();

        ValidationResult result = validator.validate(circuit);

        assertThat(result.isValid()).isTrue();
    }

    @Test
    @DisplayName("여러 게이트 중 하나라도 호환되지 않으면 실패한다")
    void failsIfAnyGateIncompatible() {
        CircuitValidator validator = new GateCompatibilityValidator();
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(2)
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new CNOTGate(new QubitIndex(1), new QubitIndex(1)))))
                .build();

        ValidationResult result = validator.validate(circuit);

        assertThat(result.isValid()).isFalse();
    }
}
