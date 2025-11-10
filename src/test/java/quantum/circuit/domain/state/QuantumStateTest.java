package quantum.circuit.domain.state;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import quantum.circuit.domain.circuit.QubitIndex;

class QuantumStateTest {

    @Test
    @DisplayName("초기 양자 상태를 생성한다")
    void createInitialState() {
        QuantumState state = QuantumState.initialize(2);

        assertThat(state.getQubitCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("초기 상태는 모든 큐비트가 |0⟩ 상태다")
    void initialStateIsZero() {
        QuantumState state = QuantumState.initialize(1);
        QubitIndex index = new QubitIndex(0);

        Probability probability = state.getProbabilityOfOne(index);

        assertThat(probability.getValue()).isEqualTo(0.0);
    }

    @Test
    @DisplayName("큐비트 개수가 0 이하면 예외가 발생한다")
    void invalidQubitCountThrowsException() {
        assertThatThrownBy(() -> QuantumState.initialize(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR]");
    }

    @Test
    @DisplayName("큐비트 개수가 10을 초과하면 예외가 발생한다")
    void tooManyQubitsThrowsException() {
        assertThatThrownBy(() -> QuantumState.initialize(11))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR]");
    }

    @Test
    @DisplayName("X 게이트를 적용하면 상태가 반전된다")
    void applyXGate() {
        QuantumState state = QuantumState.initialize(1);
        QubitIndex index = new QubitIndex(0);

        state.applyXGate(index);
        Probability probability = state.getProbabilityOfOne(index);

        assertThat(probability.getValue()).isEqualTo(1.0);
    }

    @Test
    @DisplayName("범위를 벗어난 인덱스에 게이트를 적용하면 예외가 발생한다")
    void applyGateToInvalidIndexThrowsException() {
        QuantumState state = QuantumState.initialize(2);
        QubitIndex invalidIndex = new QubitIndex(5);

        assertThatThrownBy(() -> state.applyXGate(invalidIndex))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR]");
    }

    @Test
    @DisplayName("측정하면 0 또는 1을 반환한다")
    void measureReturnsZeroOrOne() {
        QuantumState state = QuantumState.initialize(1);
        QubitIndex index = new QubitIndex(0);

        MeasurementResult result = state.measure(index);

        assertThat(result).isIn(MeasurementResult.ZERO, MeasurementResult.ONE);
    }

    @Test
    @DisplayName("|0⟩ 상태를 측정하면 항상 0이다")
    void measureZeroState() {
        QuantumState state = QuantumState.initialize(1);
        QubitIndex index = new QubitIndex(0);

        MeasurementResult result = state.measure(index);

        assertThat(result).isEqualTo(MeasurementResult.ZERO);
    }

    @Test
    @DisplayName("|1⟩ 상태를 측정하면 항상 1이다")
    void measureOneState() {
        QuantumState state = QuantumState.initialize(1);
        QubitIndex index = new QubitIndex(0);
        state.applyXGate(index);

        MeasurementResult result = state.measure(index);

        assertThat(result).isEqualTo(MeasurementResult.ONE);
    }
}
