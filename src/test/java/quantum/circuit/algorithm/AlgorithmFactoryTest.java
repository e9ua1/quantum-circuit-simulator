package quantum.circuit.algorithm;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AlgorithmFactoryTest {

    @Test
    @DisplayName("Bell State 알고리즘을 생성한다")
    void createBellStateAlgorithm() {
        AlgorithmFactory factory = new AlgorithmFactory();

        QuantumAlgorithm algorithm = factory.create("BELL_STATE");

        assertThat(algorithm).isInstanceOf(BellStateAlgorithm.class);
        assertThat(algorithm.getName()).isEqualTo("Bell State");
    }

    @Test
    @DisplayName("GHZ State 알고리즘을 생성한다")
    void createGHZStateAlgorithm() {
        AlgorithmFactory factory = new AlgorithmFactory();

        QuantumAlgorithm algorithm = factory.create("GHZ_STATE");

        assertThat(algorithm).isInstanceOf(GHZStateAlgorithm.class);
        assertThat(algorithm.getName()).isEqualTo("GHZ State");
    }

    @Test
    @DisplayName("소문자 입력도 알고리즘을 생성한다")
    void createWithLowerCase() {
        AlgorithmFactory factory = new AlgorithmFactory();

        QuantumAlgorithm algorithm = factory.create("bell_state");

        assertThat(algorithm).isInstanceOf(BellStateAlgorithm.class);
    }

    @Test
    @DisplayName("지원하지 않는 알고리즘 이름이면 예외가 발생한다")
    void throwExceptionWhenUnsupportedAlgorithm() {
        AlgorithmFactory factory = new AlgorithmFactory();

        assertThatThrownBy(() -> factory.create("UNKNOWN"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("지원하지 않는 알고리즘");
    }

    @Test
    @DisplayName("null 입력 시 예외가 발생한다")
    void throwExceptionWhenNull() {
        AlgorithmFactory factory = new AlgorithmFactory();

        assertThatThrownBy(() -> factory.create(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("알고리즘 이름");
    }

    @Test
    @DisplayName("빈 문자열 입력 시 예외가 발생한다")
    void throwExceptionWhenEmpty() {
        AlgorithmFactory factory = new AlgorithmFactory();

        assertThatThrownBy(() -> factory.create(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("알고리즘 이름");
    }

    @Test
    @DisplayName("사용 가능한 알고리즘 목록을 반환한다")
    void getAvailableAlgorithms() {
        AlgorithmFactory factory = new AlgorithmFactory();

        var algorithms = factory.getAvailableAlgorithms();

        assertThat(algorithms).contains("BELL_STATE", "GHZ_STATE");
    }
}
