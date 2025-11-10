package quantum.circuit.validator;

import quantum.circuit.domain.circuit.QuantumCircuit;

public interface CircuitValidator {

    ValidationResult validate(QuantumCircuit circuit);

    String getValidationName();
}
