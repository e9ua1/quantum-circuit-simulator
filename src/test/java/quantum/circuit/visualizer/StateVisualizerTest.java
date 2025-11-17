package quantum.circuit.visualizer;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import quantum.circuit.domain.circuit.QubitIndex;
import quantum.circuit.domain.state.Probability;
import quantum.circuit.domain.state.QuantumState;

class StateVisualizerTest {

    @Test
    @DisplayName("확률을 막대그래프로 표시한다")
    void visualizeProbabilityBar() {
        Probability probability = new Probability(0.5);

        String bar = StateVisualizer.visualizeProbabilityBar(probability);

        assertThat(bar).contains("█");
        assertThat(bar).contains("50.0%");
    }

    @Test
    @DisplayName("0% 확률을 막대그래프로 표시한다")
    void visualizeZeroProbability() {
        Probability probability = new Probability(0.0);

        String bar = StateVisualizer.visualizeProbabilityBar(probability);

        assertThat(bar).contains("0.0%");
    }

    @Test
    @DisplayName("100% 확률을 막대그래프로 표시한다")
    void visualizeFullProbability() {
        Probability probability = new Probability(1.0);

        String bar = StateVisualizer.visualizeProbabilityBar(probability);

        assertThat(bar).contains("100.0%");
    }

    @Test
    @DisplayName("개별 큐비트 확률을 시각화한다")
    void visualizeQubitProbabilities() {
        QuantumState state = QuantumState.initialize(2);
        state.applyXGate(new QubitIndex(0));

        String visualization = StateVisualizer.visualizeQubitProbabilities(state);

        assertThat(visualization).contains("Qubit 0");
        assertThat(visualization).contains("Qubit 1");
        assertThat(visualization).contains("|0⟩:");
        assertThat(visualization).contains("|1⟩:");
    }

    @Test
    @DisplayName("양자 상태 전체를 시각화한다")
    void visualizeState() {
        QuantumState state = QuantumState.initialize(1);

        String visualization = StateVisualizer.visualize(state);

        assertThat(visualization).isNotEmpty();
        assertThat(visualization).contains("Qubit");
    }

    @Test
    @DisplayName("막대그래프 길이가 확률에 비례한다")
    void barLengthProportionalToProbability() {
        Probability halfProbability = new Probability(0.5);
        Probability fullProbability = new Probability(1.0);

        String halfBar = StateVisualizer.visualizeProbabilityBar(halfProbability);
        String fullBar = StateVisualizer.visualizeProbabilityBar(fullProbability);

        int halfBarLength = countBarCharacters(halfBar);
        int fullBarLength = countBarCharacters(fullBar);

        assertThat(fullBarLength).isGreaterThan(halfBarLength);
    }

    private int countBarCharacters(String bar) {
        return (int) bar.chars()
                .filter(c -> c == '█')
                .count();
    }
}
