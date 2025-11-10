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

class CircuitValidatorTest {

    @Test
    @DisplayName("검증기는 회로를 검증한다")
    void validateCircuit() {
        CircuitValidator validator = new TestValidator();
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .build();

        ValidationResult result = validator.validate(circuit);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("검증기는 검증 이름을 반환한다")
    void getValidationName() {
        CircuitValidator validator = new TestValidator();

        String name = validator.getValidationName();

        assertThat(name).isNotBlank();
    }

    private static class TestValidator implements CircuitValidator {
        @Override
        public ValidationResult validate(QuantumCircuit circuit) {
            return ValidationResult.success();
        }

        @Override
        public String getValidationName() {
            return "Test Validator";
        }
    }
}
