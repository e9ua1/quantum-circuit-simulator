package quantum.circuit.domain.circuit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class QubitIndexTest {

    @Test
    @DisplayName("유효한 큐비트 인덱스를 생성한다")
    void createValidQubitIndex() {
        QubitIndex index = new QubitIndex(0);

        assertThat(index.getValue()).isEqualTo(0);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 5, 9})
    @DisplayName("0 이상의 인덱스는 유효하다")
    void validIndices(int value) {
        QubitIndex index = new QubitIndex(value);

        assertThat(index.getValue()).isEqualTo(value);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -5, -100})
    @DisplayName("음수 인덱스는 예외를 발생시킨다")
    void negativeIndexThrowsException(int value) {
        assertThatThrownBy(() -> new QubitIndex(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR]");
    }

    @Test
    @DisplayName("같은 값의 큐비트 인덱스는 동일하다")
    void equalityWithSameValue() {
        QubitIndex index1 = new QubitIndex(3);
        QubitIndex index2 = new QubitIndex(3);

        assertThat(index1).isEqualTo(index2);
    }

    @Test
    @DisplayName("다른 값의 큐비트 인덱스는 다르다")
    void equalityWithDifferentValue() {
        QubitIndex index1 = new QubitIndex(3);
        QubitIndex index2 = new QubitIndex(5);

        assertThat(index1).isNotEqualTo(index2);
    }

    @Test
    @DisplayName("같은 값의 큐비트 인덱스는 같은 해시코드를 가진다")
    void hashCodeWithSameValue() {
        QubitIndex index1 = new QubitIndex(3);
        QubitIndex index2 = new QubitIndex(3);

        assertThat(index1.hashCode()).isEqualTo(index2.hashCode());
    }
}
