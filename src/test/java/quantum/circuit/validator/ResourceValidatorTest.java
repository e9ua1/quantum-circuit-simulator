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

class ResourceValidatorTest {

    @Test
    @DisplayName("리소스 검증기를 생성한다")
    void createResourceValidator() {
        CircuitValidator validator = new ResourceValidator(10);

        assertThat(validator).isNotNull();
    }

    @Test
    @DisplayName("제한 내의 큐비트 개수는 통과한다")
    void passWhenQubitCountWithinLimit() {
        CircuitValidator validator = new ResourceValidator(10);
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(5)
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .build();

        ValidationResult result = validator.validate(circuit);

        assertThat(result.isValid()).isTrue();
    }

    @Test
    @DisplayName("큐비트 개수가 제한을 초과하면 실패한다")
    void failWhenQubitCountExceedsLimit() {
        CircuitValidator validator = new ResourceValidator(5);
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(10)
                .build();

        ValidationResult result = validator.validate(circuit);

        assertThat(result.isValid()).isFalse();
        assertThat(result.getMessage()).contains("리소스");
    }

    @Test
    @DisplayName("큐비트 개수가 정확히 제한과 같으면 통과한다")
    void passWhenQubitCountEqualsLimit() {
        CircuitValidator validator = new ResourceValidator(5);
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(5)
                .build();

        ValidationResult result = validator.validate(circuit);

        assertThat(result.isValid()).isTrue();
    }

    @Test
    @DisplayName("최소 큐비트 개수(1)는 통과한다")
    void passMinimumQubitCount() {
        CircuitValidator validator = new ResourceValidator(10);
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .build();

        ValidationResult result = validator.validate(circuit);

        assertThat(result.isValid()).isTrue();
    }

    @Test
    @DisplayName("검증 이름을 반환한다")
    void returnValidationName() {
        CircuitValidator validator = new ResourceValidator(10);

        String name = validator.getValidationName();

        assertThat(name).isEqualTo("Resource Validator");
    }

    @Test
    @DisplayName("제한값 1로 생성하면 1큐비트만 허용한다")
    void limitOneAllowsOnlyOneQubit() {
        CircuitValidator validator = new ResourceValidator(1);
        QuantumCircuit circuit1 = new QuantumCircuitBuilder().withQubits(1).build();
        QuantumCircuit circuit2 = new QuantumCircuitBuilder().withQubits(2).build();

        ValidationResult result1 = validator.validate(circuit1);
        ValidationResult result2 = validator.validate(circuit2);

        assertThat(result1.isValid()).isTrue();
        assertThat(result2.isValid()).isFalse();
    }

    @Test
    @DisplayName("매우 큰 제한값은 모든 회로를 통과시킨다")
    void largeLimitPassesAllCircuits() {
        CircuitValidator validator = new ResourceValidator(1000);
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(10)
                .build();

        ValidationResult result = validator.validate(circuit);

        assertThat(result.isValid()).isTrue();
    }
}
