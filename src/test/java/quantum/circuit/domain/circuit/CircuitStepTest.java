package quantum.circuit.domain.circuit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import quantum.circuit.domain.gate.HadamardGate;
import quantum.circuit.domain.gate.PauliXGate;
import quantum.circuit.domain.gate.QuantumGate;
import quantum.circuit.domain.state.QuantumState;

class CircuitStepTest {

    @Test
    @DisplayName("단일 게이트로 Step을 생성한다")
    void createStepWithSingleGate() {
        QuantumGate gate = new PauliXGate(new QubitIndex(0));

        CircuitStep step = new CircuitStep(List.of(gate));

        assertThat(step.getGateCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("여러 게이트로 Step을 생성한다")
    void createStepWithMultipleGates() {
        QuantumGate gate1 = new PauliXGate(new QubitIndex(0));
        QuantumGate gate2 = new HadamardGate(new QubitIndex(1));

        CircuitStep step = new CircuitStep(List.of(gate1, gate2));

        assertThat(step.getGateCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("빈 게이트 리스트로 Step을 생성하면 예외가 발생한다")
    void emptyGatesThrowsException() {
        assertThatThrownBy(() -> new CircuitStep(List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR]");
    }

    @Test
    @DisplayName("null 게이트 리스트로 Step을 생성하면 예외가 발생한다")
    void nullGatesThrowsException() {
        assertThatThrownBy(() -> new CircuitStep(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR]");
    }

    @Test
    @DisplayName("Step의 모든 게이트를 양자 상태에 적용한다")
    void applyAllGatesToState() {
        QuantumState state = QuantumState.initialize(2);
        QuantumGate gate1 = new PauliXGate(new QubitIndex(0));
        QuantumGate gate2 = new PauliXGate(new QubitIndex(1));
        CircuitStep step = new CircuitStep(List.of(gate1, gate2));

        step.applyTo(state);

        assertThat(state.getProbabilityOfOne(new QubitIndex(0)).getValue()).isEqualTo(1.0);
        assertThat(state.getProbabilityOfOne(new QubitIndex(1)).getValue()).isEqualTo(1.0);
    }

    @Test
    @DisplayName("Step의 게이트 리스트는 불변이다")
    void gatesAreImmutable() {
        QuantumGate gate = new PauliXGate(new QubitIndex(0));
        List<QuantumGate> gates = List.of(gate);
        CircuitStep step = new CircuitStep(gates);

        List<QuantumGate> retrievedGates = step.getGates();

        assertThatThrownBy(() -> retrievedGates.add(new HadamardGate(new QubitIndex(1))))
                .isInstanceOf(UnsupportedOperationException.class);
    }
}
