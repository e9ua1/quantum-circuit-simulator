package quantum.circuit.infrastructure.executor;

import java.util.HashMap;
import java.util.Map;

import org.redfx.strange.Complex;
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
    private final int qubitCount;

    public StrangeQuantumExecutor(int qubitCount) {
        this.program = new Program(qubitCount);
        this.environment = new SimpleQuantumExecutionEnvironment();
        this.qubitCount = qubitCount;
    }

    @Override
    public void applyXGate(QubitIndex target) {
        Step step = new Step();
        step.addGate(new X(target.value()));
        program.addStep(step);
    }

    @Override
    public void applyHadamardGate(QubitIndex target) {
        Step step = new Step();
        step.addGate(new Hadamard(target.value()));
        program.addStep(step);
    }

    @Override
    public void applyZGate(QubitIndex target) {
        Step step = new Step();
        step.addGate(new Z(target.value()));
        program.addStep(step);
    }

    @Override
    public void applyCNOTGate(QubitIndex control, QubitIndex target) {
        Step step = new Step();
        step.addGate(new Cnot(control.value(), target.value()));
        program.addStep(step);
    }

    @Override
    public Probability getProbabilityOfOne(QubitIndex index) {
        if (isEmpty()) {
            return new Probability(0.0);
        }
        Result result = environment.runProgram(copyProgram());
        double probability = result.getQubits()[index.value()].getProbability();
        return new Probability(probability);
    }

    @Override
    public MeasurementResult measure(QubitIndex index) {
        if (isEmpty()) {
            return MeasurementResult.ZERO;
        }
        Result result = environment.runProgram(copyProgram());
        int measured = result.getQubits()[index.value()].measure();
        return MeasurementResult.from(measured);
    }

    @Override
    public boolean isEmpty() {
        return program.getSteps().isEmpty();
    }

    /**
     * 모든 basis state의 정확한 확률을 반환
     *
     * @return basis state(이진 문자열) -> 확률 매핑
     */
    public Map<String, Double> getStateProbabilities() {
        if (isEmpty()) {
            Map<String, Double> initialState = new HashMap<>();
            String zeroState = "0".repeat(qubitCount);

            int numStates = 1 << qubitCount;
            for (int i = 0; i < numStates; i++) {
                String binaryState = toBinaryString(i, qubitCount);
                initialState.put(binaryState, binaryState.equals(zeroState) ? 1.0 : 0.0);
            }
            return initialState;
        }

        Result result = environment.runProgram(copyProgram());
        return extractStateProbabilities(result);
    }

    /**
     * Strange의 Result에서 각 basis state의 확률을 추출
     */
    private Map<String, Double> extractStateProbabilities(Result result) {
        Map<String, Double> probabilities = new HashMap<>();
        int numStates = 1 << qubitCount;

        Complex[] amplitudes = getAmplitudesFromResult(result);

        for (int i = 0; i < numStates; i++) {
            String binaryState = toBinaryString(i, qubitCount);

            if (amplitudes != null && i < amplitudes.length) {
                double probability = amplitudes[i].abssqr();
                probabilities.put(binaryState, probability);
            } else {
                probabilities.put(binaryState, 0.0);
            }
        }

        return probabilities;
    }

    /**
     * Strange Result에서 amplitude 배열을 추출
     * Reflection을 사용하여 내부 필드에 접근
     */
    private Complex[] getAmplitudesFromResult(Result result) {
        try {
            java.lang.reflect.Field field = result.getClass().getDeclaredField("probability");
            field.setAccessible(true);
            return (Complex[]) field.get(result);
        } catch (NoSuchFieldException e) {
            return tryAlternativeFieldNames(result);
        } catch (Exception e) {
            System.err.println("Warning: Could not extract amplitudes from Strange Result: " + e.getMessage());
            return null;
        }
    }

    private Complex[] tryAlternativeFieldNames(Result result) {
        String[] possibleNames = {"probability", "probabilities", "amplitude", "amplitudes", "state"};

        for (String fieldName : possibleNames) {
            try {
                java.lang.reflect.Field field = result.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                Object value = field.get(result);
                if (value instanceof Complex[]) {
                    return (Complex[]) value;
                }
            } catch (Exception e) {
                // 다음 이름 시도
            }
        }

        return null;
    }

    private String toBinaryString(int value, int length) {
        StringBuilder binary = new StringBuilder();
        for (int i = length - 1; i >= 0; i--) {
            binary.append((value >> i) & 1);
        }
        return binary.toString();
    }

    private Program copyProgram() {
        Program copy = new Program(program.getNumberQubits());
        program.getSteps().forEach(copy::addStep);
        return copy;
    }
}
