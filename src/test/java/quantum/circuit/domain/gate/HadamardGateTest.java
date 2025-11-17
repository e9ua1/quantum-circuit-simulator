package quantum.circuit.domain.gate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import quantum.circuit.domain.circuit.QubitIndex;
import quantum.circuit.domain.state.QuantumState;

class HadamardGateTest {

    @Test
    @DisplayName("Hadamard 게이트를 생성한다")
    void createHadamardGate() {
        QubitIndex target = new QubitIndex(0);

        HadamardGate gate = new HadamardGate(target);

        assertThat(gate.getTarget()).isEqualTo(target);
    }

    @Test
    @DisplayName("Hadamard 게이트는 단일 큐비트 게이트다")
    void singleQubitGate() {
        HadamardGate gate = new HadamardGate(new QubitIndex(0));

        assertThat(gate.getQubitCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("Hadamard 게이트의 이름은 H다")
    void gateName() {
        HadamardGate gate = new HadamardGate(new QubitIndex(0));

        assertThat(gate.getName()).isEqualTo("H");
    }

    @Test
    @DisplayName("null 타겟으로 Hadamard 게이트를 생성하면 예외가 발생한다")
    void nullTargetThrowsException() {
        assertThatThrownBy(() -> new HadamardGate(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR]");
    }

    @Test
    @DisplayName("Hadamard 게이트는 중첩 상태를 만든다")
    void createsSuperposition() {
        QuantumState state = QuantumState.initialize(1);
        HadamardGate gate = new HadamardGate(new QubitIndex(0));

        gate.apply(state);

        double probability = state.getProbabilityOfOne(new QubitIndex(0)).getValue();
        assertThat(probability).isCloseTo(0.5, within(0.01));
    }
}
