package quantum.circuit.validator;

import quantum.circuit.domain.circuit.QuantumCircuit;
import quantum.circuit.domain.gate.CNOTGate;
import quantum.circuit.domain.gate.QuantumGate;

public class GateCompatibilityValidator implements CircuitValidator {

    private static final String VALIDATION_NAME = "Gate Compatibility Validator";
    private static final String ERROR_INCOMPATIBLE = "게이트 호환성 오류: 제어 큐비트와 타겟 큐비트가 동일합니다";

    @Override
    public ValidationResult validate(QuantumCircuit circuit) {
        for (var step : circuit.getSteps()) {
            for (QuantumGate gate : step.getGates()) {
                if (!isCompatibleGate(gate)) {
                    return ValidationResult.failure(ERROR_INCOMPATIBLE);
                }
            }
        }

        return ValidationResult.success();
    }

    private boolean isCompatibleGate(QuantumGate gate) {
        if (gate instanceof CNOTGate) {
            CNOTGate cnot = (CNOTGate) gate;
            return !cnot.getControl().equals(cnot.getTarget());
        }
        return true;
    }

    @Override
    public String getValidationName() {
        return VALIDATION_NAME;
    }
}
