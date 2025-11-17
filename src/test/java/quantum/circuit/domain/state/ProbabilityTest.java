package quantum.circuit.domain.state;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ProbabilityTest {

    @Test
    @DisplayName("유효한 확률을 생성한다")
    void createValidProbability() {
        Probability probability = new Probability(0.5);

        assertThat(probability.getValue()).isEqualTo(0.5);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, 0.3, 0.5, 0.7, 1.0})
    @DisplayName("0.0 이상 1.0 이하의 확률은 유효하다")
    void validProbabilities(double value) {
        Probability probability = new Probability(value);

        assertThat(probability.getValue()).isEqualTo(value);
    }

    @ParameterizedTest
    @ValueSource(doubles = {-0.1, -1.0, 1.1, 2.0})
    @DisplayName("0.0 미만 또는 1.0 초과 확률은 예외를 발생시킨다")
    void invalidProbabilityThrowsException(double value) {
        assertThatThrownBy(() -> new Probability(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR]");
    }

    @Test
    @DisplayName("확률을 백분율로 변환한다")
    void toPercentage() {
        Probability probability = new Probability(0.5);

        assertThat(probability.toPercentage()).isCloseTo(50.0, within(0.01));
    }

    @Test
    @DisplayName("0%를 백분율로 변환한다")
    void toPercentageZero() {
        Probability probability = new Probability(0.0);

        assertThat(probability.toPercentage()).isCloseTo(0.0, within(0.01));
    }

    @Test
    @DisplayName("100%를 백분율로 변환한다")
    void toPercentageHundred() {
        Probability probability = new Probability(1.0);

        assertThat(probability.toPercentage()).isCloseTo(100.0, within(0.01));
    }

    @Test
    @DisplayName("같은 값의 확률은 동일하다")
    void equalityWithSameValue() {
        Probability prob1 = new Probability(0.5);
        Probability prob2 = new Probability(0.5);

        assertThat(prob1).isEqualTo(prob2);
    }

    @Test
    @DisplayName("다른 값의 확률은 다르다")
    void equalityWithDifferentValue() {
        Probability prob1 = new Probability(0.5);
        Probability prob2 = new Probability(0.7);

        assertThat(prob1).isNotEqualTo(prob2);
    }
}
