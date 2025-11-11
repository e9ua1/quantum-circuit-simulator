package quantum.circuit.validator;

import quantum.circuit.domain.circuit.QuantumCircuit;

public class ResourceValidator implements CircuitValidator {

    private static final String VALIDATION_NAME = "Resource Validator";
    private static final String ERROR_RESOURCE_EXCEEDED = "리소스 제한을 초과했습니다";

    private final int maxQubits;

    public ResourceValidator(int maxQubits) {
        this.maxQubits = maxQubits;
    }

    @Override
    public ValidationResult validate(QuantumCircuit circuit) {
        int qubitCount = circuit.getQubitCount();

        if (qubitCount > maxQubits) {
            return ValidationResult.failure(ERROR_RESOURCE_EXCEEDED);
        }

        return ValidationResult.success();
    }

    @Override
    public String getValidationName() {
        return VALIDATION_NAME;
    }
}
