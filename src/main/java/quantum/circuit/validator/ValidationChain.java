package quantum.circuit.validator;

import java.util.List;

import quantum.circuit.domain.circuit.QuantumCircuit;

public class ValidationChain {

    private final List<CircuitValidator> validators;

    public ValidationChain(List<CircuitValidator> validators) {
        this.validators = List.copyOf(validators);
    }

    public ValidationResult validate(QuantumCircuit circuit) {
        for (CircuitValidator validator : validators) {
            ValidationResult result = validator.validate(circuit);
            if (!result.isValid()) {
                return result;
            }
        }
        return ValidationResult.success();
    }

    public int getValidatorCount() {
        return validators.size();
    }
}
