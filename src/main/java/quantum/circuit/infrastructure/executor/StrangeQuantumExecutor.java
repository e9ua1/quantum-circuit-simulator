package quantum.circuit.infrastructure.executor;

import org.redfx.strange.Program;
import org.redfx.strange.Result;
import org.redfx.strange.Step;
import org.redfx.strange.gate.Cnot;
import org.redfx.strange.gate.Hadamard;
import org.redfx.strange.gate.X;
import org.redfx.strange.gate.Z;
import org.redfx.strange.local.SimpleQuantumExecutionEnvironment;

import quantum.circuit.domain.circuit.QubitIndex;
import quantum.circuit.domain.state.MeasurementResult;
import quantum.circuit.domain.state.Probability;
import quantum.circuit.domain.state.executor.QuantumExecutor;

import java.util.HashMap;
import java.util.Map;

public class StrangeQuantumExecutor implements QuantumExecutor {

    private static final int SAMPLE_COUNT = 10000;

    private final Program program;
    private final SimpleQuantumExecutionEnvironment environment;
    private final int qubitCount;

    public StrangeQuantumExecutor(int qubitCount) {
        this.program = new Program(qubitCount);
        this.environment = new SimpleQuantumExecutionEnvironment();
        this.qubitCount = qubitCount;
    }

    @Override
    public void applyXGate(QubitIndex target) {
        Step step = new Step();
        step.addGate(new X(target.getValue()));
        program.addStep(step);
    }

    @Override
    public void applyHadamardGate(QubitIndex target) {
        Step step = new Step();
        step.addGate(new Hadamard(target.getValue()));
        program.addStep(step);
    }

    @Override
    public void applyZGate(QubitIndex target) {
        Step step = new Step();
        step.addGate(new Z(target.getValue()));
        program.addStep(step);
    }

    @Override
    public void applyCNOTGate(QubitIndex control, QubitIndex target) {
        Step step = new Step();
        step.addGate(new Cnot(control.getValue(), target.getValue()));
        program.addStep(step);
    }

    @Override
    public Probability getProbabilityOfOne(QubitIndex index) {
        if (isEmpty()) {
            return new Probability(0.0);
        }
        Result result = environment.runProgram(copyProgram());
        double probability = result.getQubits()[index.getValue()].getProbability();
        return new Probability(probability);
    }

    @Override
    public MeasurementResult measure(QubitIndex index) {
        if (isEmpty()) {
            return MeasurementResult.ZERO;
        }
        Result result = environment.runProgram(copyProgram());
        int measured = result.getQubits()[index.getValue()].measure();
        return MeasurementResult.from(measured);
    }

    @Override
    public boolean isEmpty() {
        return program.getSteps().isEmpty();
    }

    @Override
    public Map<String, Double> getBasisStateProbabilities() {
        int numStates = 1 << qubitCount;
        Map<String, Double> probabilities = new HashMap<>();

        for (int i = 0; i < numStates; i++) {
            String basisState = toBinaryString(i, qubitCount);
            probabilities.put(basisState, 0.0);
        }

        if (isEmpty()) {
            String initialState = "0".repeat(qubitCount);
            probabilities.put(initialState, 1.0);
            return probabilities;
        }

        Map<String, Integer> counts = new HashMap<>();
        for (int i = 0; i < numStates; i++) {
            String basisState = toBinaryString(i, qubitCount);
            counts.put(basisState, 0);
        }

        for (int sample = 0; sample < SAMPLE_COUNT; sample++) {
            String measuredState = measureAllQubits();
            counts.put(measuredState, counts.get(measuredState) + 1);
        }

        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            double probability = (double) entry.getValue() / SAMPLE_COUNT;
            probabilities.put(entry.getKey(), probability);
        }

        return probabilities;
    }

    private String measureAllQubits() {
        Result result = environment.runProgram(copyProgram());
        StringBuilder measured = new StringBuilder();

        for (int i = 0; i < qubitCount; i++) {
            int bit = result.getQubits()[i].measure();
            measured.append(bit);
        }

        return measured.toString();
    }

    private String toBinaryString(int value, int length) {
        String binary = Integer.toBinaryString(value);
        return String.format("%" + length + "s", binary).replace(' ', '0');
    }

    private Program copyProgram() {
        Program copy = new Program(program.getNumberQubits());
        program.getSteps().forEach(copy::addStep);
        return copy;
    }
}
