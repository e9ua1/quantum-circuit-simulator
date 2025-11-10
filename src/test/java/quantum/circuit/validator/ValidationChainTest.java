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

class ValidationChainTest {

    @Test
    @DisplayName("모든 검증기가 통과하면 성공한다")
    void allValidatorsPass() {
        ValidationChain chain = new ValidationChain(List.of(
                new QubitRangeValidator(),
                new GateCompatibilityValidator()
        ));
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(2)
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new CNOTGate(new QubitIndex(0), new QubitIndex(1)))))
                .build();

        ValidationResult result = chain.validate(circuit);

        assertThat(result.isValid()).isTrue();
    }

    @Test
    @DisplayName("첫 번째 검증기가 실패하면 실패한다")
    void firstValidatorFails() {
        ValidationChain chain = new ValidationChain(List.of(
                new QubitRangeValidator(),
                new GateCompatibilityValidator()
        ));
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(2)
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(5)))))
                .build();

        ValidationResult result = chain.validate(circuit);

        assertThat(result.isValid()).isFalse();
        assertThat(result.getMessage()).contains("범위");
    }

    @Test
    @DisplayName("두 번째 검증기가 실패하면 실패한다")
    void secondValidatorFails() {
        ValidationChain chain = new ValidationChain(List.of(
                new QubitRangeValidator(),
                new GateCompatibilityValidator()
        ));
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(2)
                .addStep(new CircuitStep(List.of(new CNOTGate(new QubitIndex(0), new QubitIndex(0)))))
                .build();

        ValidationResult result = chain.validate(circuit);

        assertThat(result.isValid()).isFalse();
        assertThat(result.getMessage()).contains("호환");
    }

    @Test
    @DisplayName("빈 검증기 리스트는 통과한다")
    void emptyValidatorList() {
        ValidationChain chain = new ValidationChain(List.of());
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))))
                .build();

        ValidationResult result = chain.validate(circuit);

        assertThat(result.isValid()).isTrue();
    }

    @Test
    @DisplayName("단일 검증기를 실행한다")
    void singleValidator() {
        ValidationChain chain = new ValidationChain(List.of(
                new QubitRangeValidator()
        ));
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(2)
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))))
                .build();

        ValidationResult result = chain.validate(circuit);

        assertThat(result.isValid()).isTrue();
    }

    @Test
    @DisplayName("검증기 개수를 반환한다")
    void returnValidatorCount() {
        ValidationChain chain = new ValidationChain(List.of(
                new QubitRangeValidator(),
                new GateCompatibilityValidator()
        ));

        int count = chain.getValidatorCount();

        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("복잡한 회로를 체인으로 검증한다")
    void validateComplexCircuit() {
        ValidationChain chain = new ValidationChain(List.of(
                new QubitRangeValidator(),
                new GateCompatibilityValidator()
        ));
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(3)
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new CNOTGate(new QubitIndex(0), new QubitIndex(1)))))
                .addStep(new CircuitStep(List.of(new CNOTGate(new QubitIndex(1), new QubitIndex(2)))))
                .build();

        ValidationResult result = chain.validate(circuit);

        assertThat(result.isValid()).isTrue();
    }

    @Test
    @DisplayName("첫 번째 실패에서 멈춘다")
    void stopsAtFirstFailure() {
        ValidationChain chain = new ValidationChain(List.of(
                new QubitRangeValidator(),
                new GateCompatibilityValidator()
        ));
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(2)
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(10)))))
                .build();

        ValidationResult result = chain.validate(circuit);

        assertThat(result.isValid()).isFalse();
    }
}
