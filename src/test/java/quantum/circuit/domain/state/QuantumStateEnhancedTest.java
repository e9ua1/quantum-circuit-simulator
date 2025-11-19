package quantum.circuit.domain.state;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import quantum.circuit.domain.circuit.QubitIndex;
import quantum.circuit.domain.state.executor.QuantumExecutor;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("QuantumState 향상된 테스트")
class QuantumStateEnhancedTest {

    @Test
    @DisplayName("커스텀 Executor로 초기화할 수 있다")
    void 커스텀Executor로_초기화() {
        MockQuantumExecutor mockExecutor = new MockQuantumExecutor();

        QuantumState state = QuantumState.initialize(2, mockExecutor);

        assertThat(state.getQubitCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("X 게이트 적용 시 Executor에 위임한다")
    void X게이트_Executor위임() {
        MockQuantumExecutor mockExecutor = new MockQuantumExecutor();
        QuantumState state = QuantumState.initialize(1, mockExecutor);

        state.applyXGate(new QubitIndex(0));

        assertThat(mockExecutor.xGateApplied).isTrue();
    }

    @Test
    @DisplayName("Hadamard 게이트 적용 시 Executor에 위임한다")
    void Hadamard게이트_Executor위임() {
        MockQuantumExecutor mockExecutor = new MockQuantumExecutor();
        QuantumState state = QuantumState.initialize(1, mockExecutor);

        state.applyHadamardGate(new QubitIndex(0));

        assertThat(mockExecutor.hadamardGateApplied).isTrue();
    }

    @Test
    @DisplayName("Z 게이트 적용 시 Executor에 위임한다")
    void Z게이트_Executor위임() {
        MockQuantumExecutor mockExecutor = new MockQuantumExecutor();
        QuantumState state = QuantumState.initialize(1, mockExecutor);

        state.applyZGate(new QubitIndex(0));

        assertThat(mockExecutor.zGateApplied).isTrue();
    }

    @Test
    @DisplayName("CNOT 게이트 적용 시 Executor에 위임한다")
    void CNOT게이트_Executor위임() {
        MockQuantumExecutor mockExecutor = new MockQuantumExecutor();
        QuantumState state = QuantumState.initialize(2, mockExecutor);

        state.applyCNOTGate(new QubitIndex(0), new QubitIndex(1));

        assertThat(mockExecutor.cnotGateApplied).isTrue();
    }

    @Test
    @DisplayName("확률 조회 시 Executor에 위임한다")
    void 확률조회_Executor위임() {
        MockQuantumExecutor mockExecutor = new MockQuantumExecutor();
        mockExecutor.probability = 0.75;
        QuantumState state = QuantumState.initialize(1, mockExecutor);

        Probability prob = state.getProbabilityOfOne(new QubitIndex(0));

        assertThat(prob.getValue()).isEqualTo(0.75);
    }

    @Test
    @DisplayName("측정 시 Executor에 위임한다")
    void 측정_Executor위임() {
        MockQuantumExecutor mockExecutor = new MockQuantumExecutor();
        mockExecutor.measurementResult = MeasurementResult.ONE;
        QuantumState state = QuantumState.initialize(1, mockExecutor);

        MeasurementResult result = state.measure(new QubitIndex(0));

        assertThat(result).isEqualTo(MeasurementResult.ONE);
    }

    @Test
    @DisplayName("전체 상태 확률 조회 시 Executor에 위임한다")
    void 전체상태확률_Executor위임() {
        MockQuantumExecutor mockExecutor = new MockQuantumExecutor();
        Map<String, Double> expected = new HashMap<>();
        expected.put("0", 0.6);
        expected.put("1", 0.4);
        mockExecutor.basisStateProbabilities = expected;

        QuantumState state = QuantumState.initialize(1, mockExecutor);

        Map<String, Double> probabilities = state.getBasisStateProbabilities();

        assertThat(probabilities).isEqualTo(expected);
    }

    private static class MockQuantumExecutor implements QuantumExecutor {
        boolean xGateApplied = false;
        boolean hadamardGateApplied = false;
        boolean zGateApplied = false;
        boolean cnotGateApplied = false;
        double probability = 0.5;
        MeasurementResult measurementResult = MeasurementResult.ZERO;
        Map<String, Double> basisStateProbabilities = new HashMap<>();

        @Override
        public void applyXGate(QubitIndex target) {
            xGateApplied = true;
        }

        @Override
        public void applyHadamardGate(QubitIndex target) {
            hadamardGateApplied = true;
        }

        @Override
        public void applyZGate(QubitIndex target) {
            zGateApplied = true;
        }

        @Override
        public void applyCNOTGate(QubitIndex control, QubitIndex target) {
            cnotGateApplied = true;
        }

        @Override
        public Probability getProbabilityOfOne(QubitIndex index) {
            return new Probability(probability);
        }

        @Override
        public MeasurementResult measure(QubitIndex index) {
            return measurementResult;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public Map<String, Double> getBasisStateProbabilities() {
            return basisStateProbabilities;
        }
    }
}
