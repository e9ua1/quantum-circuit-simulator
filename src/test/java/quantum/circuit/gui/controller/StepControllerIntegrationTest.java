package quantum.circuit.gui.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import quantum.circuit.algorithm.AlgorithmFactory;
import quantum.circuit.algorithm.QuantumAlgorithm;
import quantum.circuit.domain.circuit.QubitIndex;
import quantum.circuit.domain.circuit.QuantumCircuit;
import quantum.circuit.domain.state.Probability;
import quantum.circuit.domain.state.QuantumState;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("StepController 통합 테스트")
class StepControllerIntegrationTest {

    @Test
    @DisplayName("Bell State를 단계별로 실행하면 상태가 변한다")
    void bellState_단계별실행_상태변화() {
        // given
        AlgorithmFactory factory = new AlgorithmFactory();
        QuantumAlgorithm algorithm = factory.create("BELL_STATE");
        QuantumCircuit circuit = algorithm.build(2);

        // when & then
        // Step 0: 초기 상태 |00⟩
        QuantumState state0 = circuit.executeUntilStep(0);
        Probability prob0 = state0.getProbabilityOfOne(new QubitIndex(0));
        assertThat(prob0.getValue()).isEqualTo(0.0);

        // Step 1: H 게이트 적용 후 |+0⟩ (Q0은 50%)
        QuantumState state1 = circuit.executeUntilStep(1);
        Probability prob1 = state1.getProbabilityOfOne(new QubitIndex(0));
        assertThat(prob1.getValue()).isCloseTo(0.5, org.assertj.core.data.Offset.offset(0.01));

        // Step 2: CNOT 적용 후 Bell State (Q0은 여전히 50%)
        QuantumState state2 = circuit.executeUntilStep(2);
        Probability prob2 = state2.getProbabilityOfOne(new QubitIndex(0));
        assertThat(prob2.getValue()).isCloseTo(0.5, org.assertj.core.data.Offset.offset(0.01));
    }
}
