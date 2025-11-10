package quantum.circuit.domain.gate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import quantum.circuit.domain.circuit.QubitIndex;

class PauliZGateTest {

    @Test
    @DisplayName("Z 게이트를 생성한다")
    void createZGate() {
        QubitIndex target = new QubitIndex(0);

        PauliZGate gate = new PauliZGate(target);

        assertThat(gate.getTarget()).isEqualTo(target);
    }

    @Test
    @DisplayName("Z 게이트는 단일 큐비트 게이트다")
    void singleQubitGate() {
        PauliZGate gate = new PauliZGate(new QubitIndex(0));

        assertThat(gate.getQubitCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("Z 게이트의 이름은 Z다")
    void gateName() {
        PauliZGate gate = new PauliZGate(new QubitIndex(0));

        assertThat(gate.getName()).isEqualTo("Z");
    }

    @Test
    @DisplayName("null 타겟으로 Z 게이트를 생성하면 예외가 발생한다")
    void nullTargetThrowsException() {
        assertThatThrownBy(() -> new PauliZGate(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR]");
    }
}
