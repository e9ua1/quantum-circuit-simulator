package quantum.circuit.domain.state;

import org.redfx.strange.Program;
import org.redfx.strange.Result;
import org.redfx.strange.Step;
import org.redfx.strange.gate.Cnot;
import org.redfx.strange.gate.Hadamard;
import org.redfx.strange.gate.X;
import org.redfx.strange.gate.Z;
import org.redfx.strange.local.SimpleQuantumExecutionEnvironment;

import quantum.circuit.domain.circuit.QubitIndex;

public class QuantumState {

    private static final String ERROR_INVALID_QUBIT_COUNT = "[ERROR] 큐비트 개수는 1 이상 10 이하여야 합니다.";
    private static final String ERROR_INVALID_INDEX = "[ERROR] 큐비트 인덱스가 범위를 벗어났습니다.";
    private static final int MIN_QUBIT_COUNT = 1;
    private static final int MAX_QUBIT_COUNT = 10;

    private final Program program;
    private final int qubitCount;
    private final SimpleQuantumExecutionEnvironment environment;

    private QuantumState(int qubitCount) {
        validateQubitCount(qubitCount);
        this.qubitCount = qubitCount;
        this.program = new Program(qubitCount);
        this.environment = new SimpleQuantumExecutionEnvironment();
    }

    public static QuantumState initialize(int qubitCount) {
        return new QuantumState(qubitCount);
    }

    private void validateQubitCount(int count) {
        if (count < MIN_QUBIT_COUNT || count > MAX_QUBIT_COUNT) {
            throw new IllegalArgumentException(ERROR_INVALID_QUBIT_COUNT);
        }
    }

    public void applyXGate(QubitIndex target) {
        validateIndex(target);
        Step step = new Step();
        step.addGate(new X(target.getValue()));
        program.addStep(step);
    }

    public void applyHadamardGate(QubitIndex target) {
        validateIndex(target);
        Step step = new Step();
        step.addGate(new Hadamard(target.getValue()));
        program.addStep(step);
    }

    public void applyZGate(QubitIndex target) {
        validateIndex(target);
        Step step = new Step();
        step.addGate(new Z(target.getValue()));
        program.addStep(step);
    }

    public void applyCNOTGate(QubitIndex control, QubitIndex target) {
        validateIndex(control);
        validateIndex(target);
        Step step = new Step();
        step.addGate(new Cnot(control.getValue(), target.getValue()));
        program.addStep(step);
    }

    private void validateIndex(QubitIndex index) {
        if (index.getValue() >= qubitCount) {
            throw new IllegalArgumentException(ERROR_INVALID_INDEX);
        }
    }

    public Probability getProbabilityOfOne(QubitIndex index) {
        validateIndex(index);
        if (program.getSteps().isEmpty()) {
            return new Probability(0.0);
        }
        Result result = environment.runProgram(copyProgram());
        double probability = result.getQubits()[index.getValue()].getProbability();
        return new Probability(probability);
    }

    public MeasurementResult measure(QubitIndex index) {
        validateIndex(index);
        if (program.getSteps().isEmpty()) {
            return MeasurementResult.ZERO;
        }
        Result result = environment.runProgram(copyProgram());
        int measured = result.getQubits()[index.getValue()].measure();
        return MeasurementResult.from(measured);
    }

    private Program copyProgram() {
        Program copy = new Program(qubitCount);
        program.getSteps().forEach(copy::addStep);
        return copy;
    }

    public int getQubitCount() {
        return qubitCount;
    }
}
