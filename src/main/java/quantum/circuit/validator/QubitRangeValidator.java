package quantum.circuit.validator;

import quantum.circuit.domain.circuit.QubitIndex;
import quantum.circuit.domain.circuit.QuantumCircuit;
import quantum.circuit.domain.gate.QuantumGate;

public class QubitRangeValidator implements CircuitValidator {

    private static final String VALIDATION_NAME = "Qubit Range Validator";
    private static final String ERROR_OUT_OF_RANGE = "[ERROR] 큐비트 인덱스가 범위를 벗어났습니다.";

    @Override
    public ValidationResult validate(QuantumCircuit circuit) {
        int qubitCount = circuit.getQubitCount();

        for (var step : circuit.getSteps()) {
            for (QuantumGate gate : step.getGates()) {
                for (QubitIndex index : gate.getAffectedQubits()) {
                    if (isOutOfRange(index, qubitCount)) {
                        return ValidationResult.failure(ERROR_OUT_OF_RANGE);
                    }
                }
            }
        }

        return ValidationResult.success();
    }

    private boolean isOutOfRange(QubitIndex index, int qubitCount) {
        return index.value() < 0 || index.value() >= qubitCount;
    }

    @Override
    public String getValidationName() {
        return VALIDATION_NAME;
    }
}
