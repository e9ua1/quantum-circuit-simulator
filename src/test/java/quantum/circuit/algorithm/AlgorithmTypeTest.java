package quantum.circuit.algorithm;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AlgorithmTypeTest {

    @Test
    @DisplayName("알고리즘 타입을 문자열로 찾는다")
    void findAlgorithmTypeByString() {
        AlgorithmType type = AlgorithmType.from("BELL_STATE");

        assertThat(type).isEqualTo(AlgorithmType.BELL_STATE);
    }

    @Test
    @DisplayName("소문자 입력도 처리한다")
    void handleLowercaseInput() {
        AlgorithmType type = AlgorithmType.from("bell_state");

        assertThat(type).isEqualTo(AlgorithmType.BELL_STATE);
    }

    @Test
    @DisplayName("알고리즘을 생성한다")
    void createAlgorithm() {
        QuantumAlgorithm algorithm = AlgorithmType.BELL_STATE.create();

        assertThat(algorithm).isInstanceOf(BellStateAlgorithm.class);
        assertThat(algorithm.getName()).isEqualTo("Bell State");
    }

    @Test
    @DisplayName("지원하지 않는 알고리즘은 예외를 발생시킨다")
    void throwExceptionForUnsupportedAlgorithm() {
        assertThatThrownBy(() -> AlgorithmType.from("INVALID"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("지원하지 않는 알고리즘");
    }

    @Test
    @DisplayName("모든 알고리즘 타입의 메타데이터를 확인한다")
    void verifyAllAlgorithmMetadata() {
        assertThat(AlgorithmType.BELL_STATE.getDisplayName()).isEqualTo("Bell State");
        assertThat(AlgorithmType.BELL_STATE.getRequiredQubits()).isEqualTo(2);
        assertThat(AlgorithmType.BELL_STATE.getDescription()).isNotBlank();

        assertThat(AlgorithmType.GHZ_STATE.getDisplayName()).isEqualTo("GHZ State");
        assertThat(AlgorithmType.GHZ_STATE.getRequiredQubits()).isEqualTo(3);

        assertThat(AlgorithmType.QFT.getDisplayName()).isEqualTo("QFT");
        assertThat(AlgorithmType.GROVER.getDisplayName()).isEqualTo("Grover's Search");
        assertThat(AlgorithmType.DEUTSCH_JOZSA.getDisplayName()).isEqualTo("Deutsch-Jozsa");
    }

    @Test
    @DisplayName("각 알고리즘을 생성할 수 있다")
    void createAllAlgorithms() {
        for (AlgorithmType type : AlgorithmType.values()) {
            QuantumAlgorithm algorithm = type.create();

            assertThat(algorithm).isNotNull();
            assertThat(algorithm.getName()).isNotBlank();
            assertThat(algorithm.getRequiredQubits()).isPositive();
        }
    }

    @Test
    @DisplayName("5개의 알고리즘 타입이 존재한다")
    void verifyAlgorithmCount() {
        assertThat(AlgorithmType.values()).hasSize(5);
    }
}
