package quantum.circuit.algorithm;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import quantum.circuit.domain.circuit.QuantumCircuit;

class BellStateAlgorithmTest {

    @Test
    @DisplayName("Bell State 알고리즘으로 2큐비트 회로를 생성한다")
    void buildBellStateCircuit() {
        BellStateAlgorithm algorithm = new BellStateAlgorithm();

        QuantumCircuit circuit = algorithm.build(2);

        assertThat(circuit.getQubitCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("Bell State 회로는 2개의 Step을 가진다")
    void bellStateHasTwoSteps() {
        BellStateAlgorithm algorithm = new BellStateAlgorithm();

        QuantumCircuit circuit = algorithm.build(2);

        assertThat(circuit.getSteps()).hasSize(2);
    }

    @Test
    @DisplayName("Bell State 회로의 첫 번째 Step은 Hadamard 게이트를 포함한다")
    void firstStepContainsHadamard() {
        BellStateAlgorithm algorithm = new BellStateAlgorithm();

        QuantumCircuit circuit = algorithm.build(2);

        assertThat(circuit.getSteps().get(0).getGates()).hasSize(1);
        assertThat(circuit.getSteps().get(0).getGates().get(0).getName())
                .contains("H");
    }

    @Test
    @DisplayName("Bell State 회로의 두 번째 Step은 CNOT 게이트를 포함한다")
    void secondStepContainsCNOT() {
        BellStateAlgorithm algorithm = new BellStateAlgorithm();

        QuantumCircuit circuit = algorithm.build(2);

        assertThat(circuit.getSteps().get(1).getGates()).hasSize(1);
        assertThat(circuit.getSteps().get(1).getGates().get(0).getName())
                .contains("CNOT");
    }

    @Test
    @DisplayName("알고리즘 이름을 반환한다")
    void returnAlgorithmName() {
        BellStateAlgorithm algorithm = new BellStateAlgorithm();

        assertThat(algorithm.getName()).isEqualTo("Bell State");
    }

    @Test
    @DisplayName("알고리즘 설명을 반환한다")
    void returnDescription() {
        BellStateAlgorithm algorithm = new BellStateAlgorithm();

        assertThat(algorithm.getDescription()).isNotBlank();
    }

    @Test
    @DisplayName("필요한 큐비트 개수를 반환한다")
    void returnRequiredQubits() {
        BellStateAlgorithm algorithm = new BellStateAlgorithm();

        assertThat(algorithm.getRequiredQubits()).isEqualTo(2);
    }
}
