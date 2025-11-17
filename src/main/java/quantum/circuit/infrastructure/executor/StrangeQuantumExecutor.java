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

public class StrangeQuantumExecutor implements QuantumExecutor {

    private final Program program;
    private final SimpleQuantumExecutionEnvironment environment;

    public StrangeQuantumExecutor(int qubitCount) {
        this.program = new Program(qubitCount);
        this.environment = new SimpleQuantumExecutionEnvironment();
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

    private Program copyProgram() {
        Program copy = new Program(program.getNumberQubits());
        program.getSteps().forEach(copy::addStep);
        return copy;
    }
}
