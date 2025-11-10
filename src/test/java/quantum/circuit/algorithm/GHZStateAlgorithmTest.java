package quantum.circuit.algorithm;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import quantum.circuit.domain.circuit.QuantumCircuit;

class GHZStateAlgorithmTest {

    @Test
    @DisplayName("GHZ State 알고리즘으로 3큐비트 회로를 생성한다")
    void buildGHZStateCircuit() {
        GHZStateAlgorithm algorithm = new GHZStateAlgorithm();

        QuantumCircuit circuit = algorithm.build(3);

        assertThat(circuit.getQubitCount()).isEqualTo(3);
    }

    @Test
    @DisplayName("GHZ State 회로는 3개의 Step을 가진다")
    void ghzStateHasThreeSteps() {
        GHZStateAlgorithm algorithm = new GHZStateAlgorithm();

        QuantumCircuit circuit = algorithm.build(3);

        assertThat(circuit.getSteps()).hasSize(3);
    }

    @Test
    @DisplayName("GHZ State 회로의 첫 번째 Step은 Hadamard 게이트를 포함한다")
    void firstStepContainsHadamard() {
        GHZStateAlgorithm algorithm = new GHZStateAlgorithm();

        QuantumCircuit circuit = algorithm.build(3);

        assertThat(circuit.getSteps().get(0).getGates()).hasSize(1);
        assertThat(circuit.getSteps().get(0).getGates().get(0).getName())
                .contains("H");
    }

    @Test
    @DisplayName("GHZ State 회로의 두 번째 Step은 CNOT 게이트를 포함한다")
    void secondStepContainsCNOT() {
        GHZStateAlgorithm algorithm = new GHZStateAlgorithm();

        QuantumCircuit circuit = algorithm.build(3);

        assertThat(circuit.getSteps().get(1).getGates()).hasSize(1);
        assertThat(circuit.getSteps().get(1).getGates().get(0).getName())
                .contains("CNOT");
    }

    @Test
    @DisplayName("GHZ State 회로의 세 번째 Step은 CNOT 게이트를 포함한다")
    void thirdStepContainsCNOT() {
        GHZStateAlgorithm algorithm = new GHZStateAlgorithm();

        QuantumCircuit circuit = algorithm.build(3);

        assertThat(circuit.getSteps().get(2).getGates()).hasSize(1);
        assertThat(circuit.getSteps().get(2).getGates().get(0).getName())
                .contains("CNOT");
    }

    @Test
    @DisplayName("알고리즘 이름을 반환한다")
    void returnAlgorithmName() {
        GHZStateAlgorithm algorithm = new GHZStateAlgorithm();

        assertThat(algorithm.getName()).isEqualTo("GHZ State");
    }

    @Test
    @DisplayName("알고리즘 설명을 반환한다")
    void returnDescription() {
        GHZStateAlgorithm algorithm = new GHZStateAlgorithm();

        assertThat(algorithm.getDescription()).isNotBlank();
    }

    @Test
    @DisplayName("필요한 큐비트 개수를 반환한다")
    void returnRequiredQubits() {
        GHZStateAlgorithm algorithm = new GHZStateAlgorithm();

        assertThat(algorithm.getRequiredQubits()).isEqualTo(3);
    }

    @Test
    @DisplayName("3개가 아닌 큐비트로 생성하면 예외가 발생한다")
    void throwExceptionWhenInvalidQubitCount() {
        GHZStateAlgorithm algorithm = new GHZStateAlgorithm();

        assertThatThrownBy(() -> algorithm.build(2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("큐비트");
    }
}
