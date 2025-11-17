package quantum.circuit.infrastructure.executor;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import quantum.circuit.domain.circuit.QubitIndex;
import quantum.circuit.domain.state.MeasurementResult;
import quantum.circuit.domain.state.Probability;
import quantum.circuit.domain.state.executor.QuantumExecutor;

class StrangeQuantumExecutorTest {

    @Test
    @DisplayName("Strange Executor를 생성한다")
    void createStrangeExecutor() {
        QuantumExecutor executor = new StrangeQuantumExecutor(2);

        assertThat(executor).isNotNull();
    }

    @Test
    @DisplayName("초기 상태는 비어있다")
    void initialStateIsEmpty() {
        QuantumExecutor executor = new StrangeQuantumExecutor(1);

        assertThat(executor.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("게이트 적용 후 상태는 비어있지 않다")
    void notEmptyAfterApplyingGate() {
        QuantumExecutor executor = new StrangeQuantumExecutor(1);
        QubitIndex index = new QubitIndex(0);

        executor.applyXGate(index);

        assertThat(executor.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("초기 상태의 확률은 0이다")
    void initialProbabilityIsZero() {
        QuantumExecutor executor = new StrangeQuantumExecutor(1);
        QubitIndex index = new QubitIndex(0);

        Probability probability = executor.getProbabilityOfOne(index);

        assertThat(probability.getValue()).isEqualTo(0.0);
    }

    @Test
    @DisplayName("X 게이트 적용 후 확률은 1이다")
    void xGateProbability() {
        QuantumExecutor executor = new StrangeQuantumExecutor(1);
        QubitIndex index = new QubitIndex(0);

        executor.applyXGate(index);
        Probability probability = executor.getProbabilityOfOne(index);

        assertThat(probability.getValue()).isEqualTo(1.0);
    }

    @Test
    @DisplayName("초기 상태 측정은 0이다")
    void measureInitialState() {
        QuantumExecutor executor = new StrangeQuantumExecutor(1);
        QubitIndex index = new QubitIndex(0);

        MeasurementResult result = executor.measure(index);

        assertThat(result).isEqualTo(MeasurementResult.ZERO);
    }

    @Test
    @DisplayName("X 게이트 적용 후 측정은 1이다")
    void measureAfterXGate() {
        QuantumExecutor executor = new StrangeQuantumExecutor(1);
        QubitIndex index = new QubitIndex(0);

        executor.applyXGate(index);
        MeasurementResult result = executor.measure(index);

        assertThat(result).isEqualTo(MeasurementResult.ONE);
    }

    @Test
    @DisplayName("Hadamard 게이트를 적용한다")
    void applyHadamardGate() {
        QuantumExecutor executor = new StrangeQuantumExecutor(1);
        QubitIndex index = new QubitIndex(0);

        executor.applyHadamardGate(index);
        Probability probability = executor.getProbabilityOfOne(index);

        assertThat(probability.getValue()).isCloseTo(0.5, org.assertj.core.data.Offset.offset(0.01));
    }

    @Test
    @DisplayName("Z 게이트를 적용한다")
    void applyZGate() {
        QuantumExecutor executor = new StrangeQuantumExecutor(1);
        QubitIndex index = new QubitIndex(0);

        executor.applyZGate(index);

        assertThat(executor.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("CNOT 게이트를 적용한다")
    void applyCNOTGate() {
        QuantumExecutor executor = new StrangeQuantumExecutor(2);
        QubitIndex control = new QubitIndex(0);
        QubitIndex target = new QubitIndex(1);

        executor.applyXGate(control);
        executor.applyCNOTGate(control, target);
        Probability probability = executor.getProbabilityOfOne(target);

        assertThat(probability.getValue()).isEqualTo(1.0);
    }

    @Test
    @DisplayName("여러 큐비트를 다룬다")
    void handleMultipleQubits() {
        QuantumExecutor executor = new StrangeQuantumExecutor(3);

        executor.applyXGate(new QubitIndex(0));
        executor.applyHadamardGate(new QubitIndex(1));
        executor.applyZGate(new QubitIndex(2));

        assertThat(executor.isEmpty()).isFalse();
    }
}
