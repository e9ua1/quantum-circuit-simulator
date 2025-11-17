package quantum.circuit.validator;

import quantum.circuit.analyzer.CircuitDepth;
import quantum.circuit.domain.circuit.QuantumCircuit;

public class DepthLimitValidator implements CircuitValidator {

    private static final String VALIDATION_NAME = "Depth Limit Validator";
    private static final String ERROR_DEPTH_EXCEEDED = "회로 깊이가 제한을 초과했습니다";

    private final int maxDepth;

    public DepthLimitValidator(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    @Override
    public ValidationResult validate(QuantumCircuit circuit) {
        int depth = CircuitDepth.calculate(circuit);

        if (depth > maxDepth) {
            return ValidationResult.failure(ERROR_DEPTH_EXCEEDED);
        }

        return ValidationResult.success();
    }

    @Override
    public String getValidationName() {
        return VALIDATION_NAME;
    }
}
