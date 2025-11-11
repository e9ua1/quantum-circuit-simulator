package quantum.circuit.validator;

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

class DepthLimitValidatorTest {

    @Test
    @DisplayName("깊이 제한 검증기를 생성한다")
    void createDepthLimitValidator() {
        CircuitValidator validator = new DepthLimitValidator(5);

        assertThat(validator).isNotNull();
    }

    @Test
    @DisplayName("제한 내의 깊이는 통과한다")
    void passWhenDepthWithinLimit() {
        CircuitValidator validator = new DepthLimitValidator(5);
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))))
                .build();

        ValidationResult result = validator.validate(circuit);

        assertThat(result.isValid()).isTrue();
    }

    @Test
    @DisplayName("깊이가 제한을 초과하면 실패한다")
    void failWhenDepthExceedsLimit() {
        CircuitValidator validator = new DepthLimitValidator(2);
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .build();

        ValidationResult result = validator.validate(circuit);

        assertThat(result.isValid()).isFalse();
        assertThat(result.getMessage()).contains("깊이");
    }

    @Test
    @DisplayName("깊이가 정확히 제한과 같으면 통과한다")
    void passWhenDepthEqualsLimit() {
        CircuitValidator validator = new DepthLimitValidator(3);
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .build();

        ValidationResult result = validator.validate(circuit);

        assertThat(result.isValid()).isTrue();
    }

    @Test
    @DisplayName("빈 회로는 통과한다")
    void passEmptyCircuit() {
        CircuitValidator validator = new DepthLimitValidator(5);
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .build();

        ValidationResult result = validator.validate(circuit);

        assertThat(result.isValid()).isTrue();
    }

    @Test
    @DisplayName("검증 이름을 반환한다")
    void returnValidationName() {
        CircuitValidator validator = new DepthLimitValidator(5);

        String name = validator.getValidationName();

        assertThat(name).isEqualTo("Depth Limit Validator");
    }

    @Test
    @DisplayName("제한값 0으로 생성하면 모든 회로가 실패한다")
    void zeroLimitFailsAllCircuits() {
        CircuitValidator validator = new DepthLimitValidator(0);
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .build();

        ValidationResult result = validator.validate(circuit);

        assertThat(result.isValid()).isFalse();
    }

    @Test
    @DisplayName("매우 큰 제한값은 모든 회로를 통과시킨다")
    void largeLimitPassesAllCircuits() {
        CircuitValidator validator = new DepthLimitValidator(1000);
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))))
                .build();

        ValidationResult result = validator.validate(circuit);

        assertThat(result.isValid()).isTrue();
    }
}
