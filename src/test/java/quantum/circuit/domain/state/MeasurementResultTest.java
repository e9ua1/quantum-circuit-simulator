package quantum.circuit.domain.state;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MeasurementResultTest {

    @Test
    @DisplayName("ZERO는 0을 반환한다")
    void zeroToInt() {
        assertThat(MeasurementResult.ZERO.toInt()).isEqualTo(0);
    }

    @Test
    @DisplayName("ONE은 1을 반환한다")
    void oneToInt() {
        assertThat(MeasurementResult.ONE.toInt()).isEqualTo(1);
    }

    @Test
    @DisplayName("0으로 ZERO를 생성한다")
    void fromZero() {
        MeasurementResult result = MeasurementResult.from(0);

        assertThat(result).isEqualTo(MeasurementResult.ZERO);
    }

    @Test
    @DisplayName("1로 ONE을 생성한다")
    void fromOne() {
        MeasurementResult result = MeasurementResult.from(1);

        assertThat(result).isEqualTo(MeasurementResult.ONE);
    }

    @Test
    @DisplayName("0과 1이 아닌 값은 예외를 발생시킨다")
    void fromInvalidValue() {
        assertThatThrownBy(() -> MeasurementResult.from(2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR]");
    }

    @Test
    @DisplayName("음수 값은 예외를 발생시킨다")
    void fromNegativeValue() {
        assertThatThrownBy(() -> MeasurementResult.from(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR]");
    }

    @Test
    @DisplayName("ZERO를 문자열로 변환한다")
    void zeroToString() {
        assertThat(MeasurementResult.ZERO.toString()).isEqualTo("0");
    }

    @Test
    @DisplayName("ONE을 문자열로 변환한다")
    void oneToString() {
        assertThat(MeasurementResult.ONE.toString()).isEqualTo("1");
    }
}
