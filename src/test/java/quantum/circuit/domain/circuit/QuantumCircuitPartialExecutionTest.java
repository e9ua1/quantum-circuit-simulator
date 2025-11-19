package quantum.circuit.domain.circuit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import quantum.circuit.domain.gate.HadamardGate;
import quantum.circuit.domain.gate.PauliXGate;
import quantum.circuit.domain.state.Probability;
import quantum.circuit.domain.state.QuantumState;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("QuantumCircuit 부분 실행 테스트")
class QuantumCircuitPartialExecutionTest {

    @Test
    @DisplayName("0단계까지 실행하면 초기 상태를 반환한다")
    void executeUntilStep_0단계_초기상태() {
        // given
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))))
                .build();

        // when
        QuantumState state = circuit.executeUntilStep(0);

        // then - |0⟩ 상태 (초기 상태)
        Probability probOne = state.getProbabilityOfOne(new QubitIndex(0));
        assertThat(probOne.getValue()).isEqualTo(0.0);
    }

    @Test
    @DisplayName("1단계까지 실행하면 첫 번째 게이트만 적용된다")
    void executeUntilStep_1단계_첫번째게이트만적용() {
        // given
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .build();

        // when
        QuantumState state = circuit.executeUntilStep(1);

        // then - X 게이트만 적용: |1⟩ 상태
        Probability probOne = state.getProbabilityOfOne(new QubitIndex(0));
        assertThat(probOne.getValue()).isEqualTo(1.0);
    }

    @Test
    @DisplayName("전체 단계까지 실행하면 execute()와 동일한 결과")
    void executeUntilStep_전체단계_execute와동일() {
        // given
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .build();

        // when
        QuantumState statePartial = circuit.executeUntilStep(1);
        QuantumState stateFull = circuit.execute();

        // then
        Probability probOnePartial = statePartial.getProbabilityOfOne(new QubitIndex(0));
        Probability probOneFull = stateFull.getProbabilityOfOne(new QubitIndex(0));
        assertThat(probOnePartial.getValue())
                .isCloseTo(probOneFull.getValue(), org.assertj.core.data.Offset.offset(0.01));
    }

    @Test
    @DisplayName("단계가 음수이면 예외를 발생시킨다")
    void executeUntilStep_음수단계_예외발생() {
        // given
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .build();

        // when & then
        assertThatThrownBy(() -> circuit.executeUntilStep(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("단계");
    }

    @Test
    @DisplayName("단계가 전체 단계를 초과하면 전체 실행")
    void executeUntilStep_초과단계_전체실행() {
        // given
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .build();

        // when
        QuantumState state = circuit.executeUntilStep(999);

        // then - H 게이트 적용: 50% 확률
        Probability probOne = state.getProbabilityOfOne(new QubitIndex(0));
        assertThat(probOne.getValue()).isCloseTo(0.5, org.assertj.core.data.Offset.offset(0.01));
    }
}
