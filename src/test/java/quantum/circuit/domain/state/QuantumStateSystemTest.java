package quantum.circuit.domain.state;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import quantum.circuit.domain.circuit.CircuitStep;
import quantum.circuit.domain.circuit.QubitIndex;
import quantum.circuit.domain.circuit.QuantumCircuit;
import quantum.circuit.domain.circuit.QuantumCircuitBuilder;
import quantum.circuit.domain.gate.CNOTGate;
import quantum.circuit.domain.gate.HadamardGate;
import quantum.circuit.domain.gate.PauliXGate;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("QuantumState 전체 시스템 상태 테스트")
class QuantumStateSystemTest {

    private static final double TOLERANCE = 0.05;

    @Test
    @DisplayName("1큐비트 초기 상태는 0 확률이 100퍼센트다")
    void 초기상태_0_100퍼센트() {
        QuantumState state = QuantumState.initialize(1);

        Map<String, Double> probabilities = state.getBasisStateProbabilities();

        assertThat(probabilities.get("0")).isCloseTo(1.0, org.assertj.core.data.Offset.offset(TOLERANCE));
        assertThat(probabilities.get("1")).isCloseTo(0.0, org.assertj.core.data.Offset.offset(TOLERANCE));
    }

    @Test
    @DisplayName("X 게이트 후 1 상태는 100퍼센트다")
    void X게이트_1상태_100퍼센트() {
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))))
                .build();
        QuantumState state = circuit.execute();

        Map<String, Double> probabilities = state.getBasisStateProbabilities();

        assertThat(probabilities.get("0")).isCloseTo(0.0, org.assertj.core.data.Offset.offset(TOLERANCE));
        assertThat(probabilities.get("1")).isCloseTo(1.0, org.assertj.core.data.Offset.offset(TOLERANCE));
    }

    @Test
    @DisplayName("H 게이트 후 0과 1이 각각 50퍼센트다")
    void H게이트_중첩상태_50퍼센트() {
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .build();
        QuantumState state = circuit.execute();

        Map<String, Double> probabilities = state.getBasisStateProbabilities();

        assertThat(probabilities.get("0")).isCloseTo(0.5, org.assertj.core.data.Offset.offset(TOLERANCE));
        assertThat(probabilities.get("1")).isCloseTo(0.5, org.assertj.core.data.Offset.offset(TOLERANCE));
    }

    @Test
    @DisplayName("Bell State는 00과 11이 각각 50퍼센트다")
    void BellState_00과11_50퍼센트() {
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(2)
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new CNOTGate(new QubitIndex(0), new QubitIndex(1)))))
                .build();
        QuantumState state = circuit.execute();

        Map<String, Double> probabilities = state.getBasisStateProbabilities();

        assertThat(probabilities.get("00")).isCloseTo(0.5, org.assertj.core.data.Offset.offset(TOLERANCE));
        assertThat(probabilities.get("01")).isCloseTo(0.0, org.assertj.core.data.Offset.offset(TOLERANCE));
        assertThat(probabilities.get("10")).isCloseTo(0.0, org.assertj.core.data.Offset.offset(TOLERANCE));
        assertThat(probabilities.get("11")).isCloseTo(0.5, org.assertj.core.data.Offset.offset(TOLERANCE));
    }

    @Test
    @DisplayName("모든 기저 상태 확률의 합은 1이다")
    void 전체확률합_1() {
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(2)
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .build();
        QuantumState state = circuit.execute();

        Map<String, Double> probabilities = state.getBasisStateProbabilities();

        double sum = probabilities.values().stream().mapToDouble(Double::doubleValue).sum();
        assertThat(sum).isCloseTo(1.0, org.assertj.core.data.Offset.offset(TOLERANCE));
    }
}
