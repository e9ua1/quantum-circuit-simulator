package quantum.circuit.domain.state.executor;

import quantum.circuit.domain.circuit.QubitIndex;
import quantum.circuit.domain.state.MeasurementResult;
import quantum.circuit.domain.state.Probability;

import java.util.Map;

public interface QuantumExecutor {

    void applyXGate(QubitIndex target);

    void applyHadamardGate(QubitIndex target);

    void applyZGate(QubitIndex target);

    void applyCNOTGate(QubitIndex control, QubitIndex target);

    Probability getProbabilityOfOne(QubitIndex index);

    MeasurementResult measure(QubitIndex index);

    boolean isEmpty();

    Map<String, Double> getBasisStateProbabilities();
}
