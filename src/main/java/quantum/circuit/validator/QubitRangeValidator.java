package quantum.circuit.validator;

import quantum.circuit.domain.circuit.QuantumCircuit;
import quantum.circuit.domain.gate.CNOTGate;
import quantum.circuit.domain.gate.QuantumGate;

public class QubitRangeValidator implements CircuitValidator {

    private static final String VALIDATION_NAME = "Qubit Range Validator";
    private static final String ERROR_OUT_OF_RANGE = "큐비트 인덱스가 범위를 벗어났습니다";

    @Override
    public ValidationResult validate(QuantumCircuit circuit) {
        int qubitCount = circuit.getQubitCount();

        for (var step : circuit.getSteps()) {
            for (QuantumGate gate : step.getGates()) {
                if (!isValidGate(gate, qubitCount)) {
                    return ValidationResult.failure(ERROR_OUT_OF_RANGE);
                }
            }
        }

        return ValidationResult.success();
    }

    private boolean isValidGate(QuantumGate gate, int qubitCount) {
        if (gate instanceof CNOTGate) {
            CNOTGate cnot = (CNOTGate) gate;
            return isValidQubitIndex(cnot.getControl().getValue(), qubitCount)
                    && isValidQubitIndex(cnot.getTarget().getValue(), qubitCount);
        }
        return isValidSingleQubitGate(gate, qubitCount);
    }

    private boolean isValidSingleQubitGate(QuantumGate gate, int qubitCount) {
        try {
            Object target = gate.getClass().getMethod("getTarget").invoke(gate);
            int targetIndex = (int) target.getClass().getMethod("getValue").invoke(target);
            return isValidQubitIndex(targetIndex, qubitCount);
        } catch (Exception e) {
            return true;
        }
    }

    private boolean isValidQubitIndex(int index, int qubitCount) {
        return index >= 0 && index < qubitCount;
    }

    @Override
    public String getValidationName() {
        return VALIDATION_NAME;
    }
}
