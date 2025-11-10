package quantum.circuit.domain.gate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import quantum.circuit.domain.circuit.QubitIndex;

class PauliXGateTest {

    @Test
    @DisplayName("X 게이트를 생성한다")
    void createXGate() {
        QubitIndex target = new QubitIndex(0);

        PauliXGate gate = new PauliXGate(target);

        assertThat(gate.getTarget()).isEqualTo(target);
    }

    @Test
    @DisplayName("X 게이트는 단일 큐비트 게이트다")
    void singleQubitGate() {
        PauliXGate gate = new PauliXGate(new QubitIndex(0));

        assertThat(gate.getQubitCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("X 게이트의 이름은 X다")
    void gateName() {
        PauliXGate gate = new PauliXGate(new QubitIndex(0));

        assertThat(gate.getName()).isEqualTo("X");
    }

    @Test
    @DisplayName("X 게이트는 타겟 큐비트를 반환한다")
    void targetQubit() {
        QubitIndex target = new QubitIndex(3);
        PauliXGate gate = new PauliXGate(target);

        assertThat(gate.getTarget()).isEqualTo(target);
    }

    @Test
    @DisplayName("null 타겟으로 X 게이트를 생성하면 예외가 발생한다")
    void nullTargetThrowsException() {
        assertThatThrownBy(() -> new PauliXGate(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR]");
    }
}
