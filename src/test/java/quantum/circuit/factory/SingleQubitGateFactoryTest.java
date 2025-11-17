package quantum.circuit.factory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import quantum.circuit.domain.circuit.QubitIndex;
import quantum.circuit.domain.gate.HadamardGate;
import quantum.circuit.domain.gate.PauliXGate;
import quantum.circuit.domain.gate.PauliZGate;
import quantum.circuit.domain.gate.QuantumGate;

class SingleQubitGateFactoryTest {

    @Test
    @DisplayName("X 게이트를 생성한다")
    void createXGate() {
        QubitIndex target = new QubitIndex(0);

        QuantumGate gate = SingleQubitGateFactory.create("X", target);

        assertThat(gate).isInstanceOf(PauliXGate.class);
        assertThat(gate.getName()).isEqualTo("X");
        assertThat(gate.getAffectedQubits()).containsExactly(target);
    }

    @Test
    @DisplayName("H 게이트를 생성한다")
    void createHGate() {
        QubitIndex target = new QubitIndex(1);

        QuantumGate gate = SingleQubitGateFactory.create("H", target);

        assertThat(gate).isInstanceOf(HadamardGate.class);
        assertThat(gate.getName()).isEqualTo("H");
        assertThat(gate.getAffectedQubits()).containsExactly(target);
    }

    @Test
    @DisplayName("Z 게이트를 생성한다")
    void createZGate() {
        QubitIndex target = new QubitIndex(2);

        QuantumGate gate = SingleQubitGateFactory.create("Z", target);

        assertThat(gate).isInstanceOf(PauliZGate.class);
        assertThat(gate.getName()).isEqualTo("Z");
        assertThat(gate.getAffectedQubits()).containsExactly(target);
    }

    @Test
    @DisplayName("지원하지 않는 게이트 타입이면 예외가 발생한다")
    void throwExceptionForUnsupportedGate() {
        QubitIndex target = new QubitIndex(0);

        assertThatThrownBy(() -> SingleQubitGateFactory.create("INVALID", target))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("지원하지 않는 게이트입니다");
    }

    @Test
    @DisplayName("null 게이트 타입이면 예외가 발생한다")
    void throwExceptionForNullGateType() {
        QubitIndex target = new QubitIndex(0);

        assertThatThrownBy(() -> SingleQubitGateFactory.create(null, target))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("빈 문자열 게이트 타입이면 예외가 발생한다")
    void throwExceptionForEmptyGateType() {
        QubitIndex target = new QubitIndex(0);

        assertThatThrownBy(() -> SingleQubitGateFactory.create("", target))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("소문자 게이트 타입도 처리한다")
    void createGateWithLowerCase() {
        QubitIndex target = new QubitIndex(0);

        QuantumGate gate = SingleQubitGateFactory.create("x", target);

        assertThat(gate).isInstanceOf(PauliXGate.class);
    }

    @Test
    @DisplayName("다양한 큐비트 인덱스로 게이트를 생성한다")
    void createGatesWithDifferentQubitIndices() {
        QuantumGate gate0 = SingleQubitGateFactory.create("X", new QubitIndex(0));
        QuantumGate gate5 = SingleQubitGateFactory.create("H", new QubitIndex(5));
        QuantumGate gate10 = SingleQubitGateFactory.create("Z", new QubitIndex(10));

        assertThat(gate0.getAffectedQubits()).containsExactly(new QubitIndex(0));
        assertThat(gate5.getAffectedQubits()).containsExactly(new QubitIndex(5));
        assertThat(gate10.getAffectedQubits()).containsExactly(new QubitIndex(10));
    }
}
