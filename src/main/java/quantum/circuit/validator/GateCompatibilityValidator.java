package quantum.circuit.validator;

import java.util.HashSet;
import java.util.Set;

import quantum.circuit.domain.circuit.QubitIndex;
import quantum.circuit.domain.circuit.QuantumCircuit;
import quantum.circuit.domain.gate.QuantumGate;

public class GateCompatibilityValidator implements CircuitValidator {

    private static final String VALIDATION_NAME = "Gate Compatibility Validator";
    private static final String ERROR_INCOMPATIBLE = "[ERROR] 같은 Step에서 동일한 큐비트에 여러 게이트를 적용할 수 없습니다. 호환되지 않는 게이트 조합입니다.";

    @Override
    public ValidationResult validate(QuantumCircuit circuit) {
        for (var step : circuit.getSteps()) {
            if (hasDuplicateQubits(step.getGates())) {
                return ValidationResult.failure(ERROR_INCOMPATIBLE);
            }
        }

        return ValidationResult.success();
    }

    private boolean hasDuplicateQubits(java.util.List<QuantumGate> gates) {
        Set<QubitIndex> usedQubits = new HashSet<>();

        for (QuantumGate gate : gates) {
            Set<QubitIndex> affectedQubits = gate.getAffectedQubits();

            for (QubitIndex qubit : affectedQubits) {
                if (usedQubits.contains(qubit)) {
                    return true;
                }
                usedQubits.add(qubit);
            }
        }

        return false;
    }

    @Override
    public String getValidationName() {
        return VALIDATION_NAME;
    }
}
