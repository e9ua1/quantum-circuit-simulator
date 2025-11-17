package quantum.circuit.algorithm;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import quantum.circuit.domain.circuit.QuantumCircuit;

class GroverAlgorithmTest {

    @Test
    @DisplayName("Grover 알고리즘으로 2큐비트 회로를 생성한다")
    void buildGroverCircuit() {
        GroverAlgorithm algorithm = new GroverAlgorithm();

        QuantumCircuit circuit = algorithm.build(2);

        assertThat(circuit.getQubitCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("Grover 회로는 여러 Step을 가진다")
    void groverHasMultipleSteps() {
        GroverAlgorithm algorithm = new GroverAlgorithm();

        QuantumCircuit circuit = algorithm.build(2);

        assertThat(circuit.getSteps()).hasSizeGreaterThan(0);
    }

    @Test
    @DisplayName("Grover 회로는 Hadamard 게이트를 포함한다")
    void containsHadamardGate() {
        GroverAlgorithm algorithm = new GroverAlgorithm();

        QuantumCircuit circuit = algorithm.build(2);

        boolean hasHadamard = circuit.getSteps().stream()
                .flatMap(step -> step.getGates().stream())
                .anyMatch(gate -> gate.getName().contains("H"));

        assertThat(hasHadamard).isTrue();
    }

    @Test
    @DisplayName("알고리즘 이름을 반환한다")
    void returnAlgorithmName() {
        GroverAlgorithm algorithm = new GroverAlgorithm();

        assertThat(algorithm.getName()).isEqualTo("Grover's Search");
    }

    @Test
    @DisplayName("알고리즘 설명을 반환한다")
    void returnDescription() {
        GroverAlgorithm algorithm = new GroverAlgorithm();

        assertThat(algorithm.getDescription()).isNotBlank();
    }

    @Test
    @DisplayName("필요한 큐비트 개수를 반환한다")
    void returnRequiredQubits() {
        GroverAlgorithm algorithm = new GroverAlgorithm();

        assertThat(algorithm.getRequiredQubits()).isEqualTo(2);
    }

    @Test
    @DisplayName("2개가 아닌 큐비트로 생성하면 예외가 발생한다")
    void throwExceptionWhenInvalidQubitCount() {
        GroverAlgorithm algorithm = new GroverAlgorithm();

        assertThatThrownBy(() -> algorithm.build(3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("큐비트");
    }
}
