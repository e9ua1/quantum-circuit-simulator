package quantum.circuit.algorithm;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import quantum.circuit.domain.circuit.QuantumCircuit;

class QFTAlgorithmTest {

    @Test
    @DisplayName("QFT 알고리즘으로 2큐비트 회로를 생성한다")
    void buildQFTCircuit() {
        QFTAlgorithm algorithm = new QFTAlgorithm();

        QuantumCircuit circuit = algorithm.build(2);

        assertThat(circuit.getQubitCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("QFT 회로는 여러 Step을 가진다")
    void qftHasMultipleSteps() {
        QFTAlgorithm algorithm = new QFTAlgorithm();

        QuantumCircuit circuit = algorithm.build(2);

        assertThat(circuit.getSteps()).hasSizeGreaterThan(0);
    }

    @Test
    @DisplayName("QFT 회로는 Hadamard 게이트를 포함한다")
    void containsHadamardGate() {
        QFTAlgorithm algorithm = new QFTAlgorithm();

        QuantumCircuit circuit = algorithm.build(2);

        boolean hasHadamard = circuit.getSteps().stream()
                .flatMap(step -> step.getGates().stream())
                .anyMatch(gate -> gate.getName().contains("H"));

        assertThat(hasHadamard).isTrue();
    }

    @Test
    @DisplayName("알고리즘 이름을 반환한다")
    void returnAlgorithmName() {
        QFTAlgorithm algorithm = new QFTAlgorithm();

        assertThat(algorithm.getName()).isEqualTo("QFT");
    }

    @Test
    @DisplayName("알고리즘 설명을 반환한다")
    void returnDescription() {
        QFTAlgorithm algorithm = new QFTAlgorithm();

        assertThat(algorithm.getDescription()).isNotBlank();
    }

    @Test
    @DisplayName("필요한 큐비트 개수를 반환한다")
    void returnRequiredQubits() {
        QFTAlgorithm algorithm = new QFTAlgorithm();

        assertThat(algorithm.getRequiredQubits()).isEqualTo(2);
    }

    @Test
    @DisplayName("2개가 아닌 큐비트로 생성하면 예외가 발생한다")
    void throwExceptionWhenInvalidQubitCount() {
        QFTAlgorithm algorithm = new QFTAlgorithm();

        assertThatThrownBy(() -> algorithm.build(3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("큐비트");
    }
}
