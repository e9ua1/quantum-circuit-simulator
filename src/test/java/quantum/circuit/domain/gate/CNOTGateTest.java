package quantum.circuit.domain.gate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import quantum.circuit.domain.circuit.QubitIndex;
import quantum.circuit.domain.state.MeasurementResult;
import quantum.circuit.domain.state.QuantumState;

class CNOTGateTest {

    @Test
    @DisplayName("CNOT 게이트를 생성한다")
    void createCNOTGate() {
        QubitIndex control = new QubitIndex(0);
        QubitIndex target = new QubitIndex(1);

        CNOTGate gate = new CNOTGate(control, target);

        assertThat(gate.getControl()).isEqualTo(control);
        assertThat(gate.getTarget()).isEqualTo(target);
    }

    @Test
    @DisplayName("CNOT 게이트는 2큐비트 게이트다")
    void twoQubitGate() {
        CNOTGate gate = new CNOTGate(new QubitIndex(0), new QubitIndex(1));

        assertThat(gate.getQubitCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("CNOT 게이트의 이름은 CNOT이다")
    void gateName() {
        CNOTGate gate = new CNOTGate(new QubitIndex(0), new QubitIndex(1));

        assertThat(gate.getName()).isEqualTo("CNOT");
    }

    @Test
    @DisplayName("제어 큐비트와 타겟 큐비트가 같으면 예외가 발생한다")
    void sameQubitThrowsException() {
        QubitIndex same = new QubitIndex(0);

        assertThatThrownBy(() -> new CNOTGate(same, same))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR]");
    }

    @Test
    @DisplayName("null 제어 큐비트로 CNOT 게이트를 생성하면 예외가 발생한다")
    void nullControlThrowsException() {
        assertThatThrownBy(() -> new CNOTGate(null, new QubitIndex(1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR]");
    }

    @Test
    @DisplayName("null 타겟 큐비트로 CNOT 게이트를 생성하면 예외가 발생한다")
    void nullTargetThrowsException() {
        assertThatThrownBy(() -> new CNOTGate(new QubitIndex(0), null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR]");
    }

    @Test
    @DisplayName("제어 큐비트가 |0⟩이면 타겟 큐비트는 변하지 않는다")
    void controlZeroDoesNothing() {
        QuantumState state = QuantumState.initialize(2);
        CNOTGate gate = new CNOTGate(new QubitIndex(0), new QubitIndex(1));

        gate.apply(state);

        assertThat(state.measure(new QubitIndex(0))).isEqualTo(MeasurementResult.ZERO);
        assertThat(state.measure(new QubitIndex(1))).isEqualTo(MeasurementResult.ZERO);
    }

    @Test
    @DisplayName("제어 큐비트가 |1⟩이면 타겟 큐비트가 반전된다")
    void controlOneFlipsTarget() {
        QuantumState state = QuantumState.initialize(2);
        state.applyXGate(new QubitIndex(0));
        CNOTGate gate = new CNOTGate(new QubitIndex(0), new QubitIndex(1));

        gate.apply(state);

        assertThat(state.measure(new QubitIndex(0))).isEqualTo(MeasurementResult.ONE);
        assertThat(state.measure(new QubitIndex(1))).isEqualTo(MeasurementResult.ONE);
    }
}
