package quantum.circuit.domain.state.executor;

import java.util.Map;

import quantum.circuit.domain.circuit.QubitIndex;
import quantum.circuit.domain.state.MeasurementResult;
import quantum.circuit.domain.state.Probability;

public interface QuantumExecutor {

    void applyXGate(QubitIndex target);

    void applyHadamardGate(QubitIndex target);

    void applyZGate(QubitIndex target);

    void applyCNOTGate(QubitIndex control, QubitIndex target);

    Probability getProbabilityOfOne(QubitIndex index);

    MeasurementResult measure(QubitIndex index);

    boolean isEmpty();

    /**
     * 모든 basis state의 정확한 확률을 반환
     *
     * @return basis state(이진 문자열) -> 확률 매핑
     */
    default Map<String, Double> getStateProbabilities() {
        throw new UnsupportedOperationException("getStateProbabilities is not implemented");
    }
}
