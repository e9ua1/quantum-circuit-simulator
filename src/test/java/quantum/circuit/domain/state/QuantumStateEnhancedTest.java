package quantum.circuit.domain.state;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import quantum.circuit.domain.circuit.QubitIndex;
import quantum.circuit.domain.state.executor.QuantumExecutor;

class QuantumStateEnhancedTest {

    @Test
    @DisplayName("커스텀 Executor로 QuantumState를 생성한다")
    void createWithCustomExecutor() {
        MockQuantumExecutor mockExecutor = new MockQuantumExecutor();

        QuantumState state = QuantumState.initialize(2, mockExecutor);

        assertThat(state.getQubitCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("X 게이트가 Executor에 위임된다")
    void xGateDelegation() {
        MockQuantumExecutor mockExecutor = new MockQuantumExecutor();
        QuantumState state = QuantumState.initialize(1, mockExecutor);
        QubitIndex index = new QubitIndex(0);

        state.applyXGate(index);

        assertThat(mockExecutor.xGateCount).isEqualTo(1);
        assertThat(mockExecutor.lastXGateTarget).isEqualTo(index);
    }

    @Test
    @DisplayName("Hadamard 게이트가 Executor에 위임된다")
    void hadamardGateDelegation() {
        MockQuantumExecutor mockExecutor = new MockQuantumExecutor();
        QuantumState state = QuantumState.initialize(1, mockExecutor);
        QubitIndex index = new QubitIndex(0);

        state.applyHadamardGate(index);

        assertThat(mockExecutor.hadamardGateCount).isEqualTo(1);
        assertThat(mockExecutor.lastHadamardTarget).isEqualTo(index);
    }

    @Test
    @DisplayName("Z 게이트가 Executor에 위임된다")
    void zGateDelegation() {
        MockQuantumExecutor mockExecutor = new MockQuantumExecutor();
        QuantumState state = QuantumState.initialize(1, mockExecutor);
        QubitIndex index = new QubitIndex(0);

        state.applyZGate(index);

        assertThat(mockExecutor.zGateCount).isEqualTo(1);
        assertThat(mockExecutor.lastZGateTarget).isEqualTo(index);
    }

    @Test
    @DisplayName("CNOT 게이트가 Executor에 위임된다")
    void cnotGateDelegation() {
        MockQuantumExecutor mockExecutor = new MockQuantumExecutor();
        QuantumState state = QuantumState.initialize(2, mockExecutor);
        QubitIndex control = new QubitIndex(0);
        QubitIndex target = new QubitIndex(1);

        state.applyCNOTGate(control, target);

        assertThat(mockExecutor.cnotGateCount).isEqualTo(1);
        assertThat(mockExecutor.lastCNOTControl).isEqualTo(control);
        assertThat(mockExecutor.lastCNOTTarget).isEqualTo(target);
    }

    @Test
    @DisplayName("확률 계산이 Executor에 위임된다")
    void probabilityDelegation() {
        MockQuantumExecutor mockExecutor = new MockQuantumExecutor();
        QuantumState state = QuantumState.initialize(1, mockExecutor);
        QubitIndex index = new QubitIndex(0);

        Probability probability = state.getProbabilityOfOne(index);

        assertThat(mockExecutor.getProbabilityCount).isEqualTo(1);
        assertThat(probability.getValue()).isEqualTo(0.75);
    }

    @Test
    @DisplayName("측정이 Executor에 위임된다")
    void measurementDelegation() {
        MockQuantumExecutor mockExecutor = new MockQuantumExecutor();
        QuantumState state = QuantumState.initialize(1, mockExecutor);
        QubitIndex index = new QubitIndex(0);

        MeasurementResult result = state.measure(index);

        assertThat(mockExecutor.measureCount).isEqualTo(1);
        assertThat(result).isEqualTo(MeasurementResult.ONE);
    }

    @Test
    @DisplayName("기본 생성자는 Strange Executor를 사용한다")
    void defaultConstructorUsesStrangeExecutor() {
        QuantumState state = QuantumState.initialize(1);
        QubitIndex index = new QubitIndex(0);

        state.applyXGate(index);
        Probability probability = state.getProbabilityOfOne(index);

        assertThat(probability.getValue()).isEqualTo(1.0);
    }

    private static class MockQuantumExecutor implements QuantumExecutor {
        int xGateCount = 0;
        int hadamardGateCount = 0;
        int zGateCount = 0;
        int cnotGateCount = 0;
        int getProbabilityCount = 0;
        int measureCount = 0;

        QubitIndex lastXGateTarget = null;
        QubitIndex lastHadamardTarget = null;
        QubitIndex lastZGateTarget = null;
        QubitIndex lastCNOTControl = null;
        QubitIndex lastCNOTTarget = null;

        @Override
        public void applyXGate(QubitIndex target) {
            xGateCount++;
            lastXGateTarget = target;
        }

        @Override
        public void applyHadamardGate(QubitIndex target) {
            hadamardGateCount++;
            lastHadamardTarget = target;
        }

        @Override
        public void applyZGate(QubitIndex target) {
            zGateCount++;
            lastZGateTarget = target;
        }

        @Override
        public void applyCNOTGate(QubitIndex control, QubitIndex target) {
            cnotGateCount++;
            lastCNOTControl = control;
            lastCNOTTarget = target;
        }

        @Override
        public Probability getProbabilityOfOne(QubitIndex index) {
            getProbabilityCount++;
            return new Probability(0.75);
        }

        @Override
        public MeasurementResult measure(QubitIndex index) {
            measureCount++;
            return MeasurementResult.ONE;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }
    }
}
