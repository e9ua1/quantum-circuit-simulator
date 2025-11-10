package quantum.circuit.validator;

import java.util.HashSet;
import java.util.Set;

import quantum.circuit.domain.circuit.QuantumCircuit;
import quantum.circuit.domain.gate.CNOTGate;
import quantum.circuit.domain.gate.QuantumGate;

public class GateCompatibilityValidator implements CircuitValidator {

    private static final String VALIDATION_NAME = "Gate Compatibility Validator";
    private static final String ERROR_INCOMPATIBLE = "게이트 호환성 오류: 한 Step에서 같은 큐비트에 여러 게이트가 적용됩니다";

    @Override
    public ValidationResult validate(QuantumCircuit circuit) {
        for (var step : circuit.getSteps()) {
            if (!isCompatibleStep(step.getGates())) {
                return ValidationResult.failure(ERROR_INCOMPATIBLE);
            }
        }

        return ValidationResult.success();
    }

    private boolean isCompatibleStep(java.util.List<QuantumGate> gates) {
        Set<Integer> usedQubits = new HashSet<>();

        for (QuantumGate gate : gates) {
            Set<Integer> gateQubits = getAffectedQubits(gate);

            for (Integer qubit : gateQubits) {
                if (usedQubits.contains(qubit)) {
                    return false;
                }
                usedQubits.add(qubit);
            }
        }

        return true;
    }

    private Set<Integer> getAffectedQubits(QuantumGate gate) {
        Set<Integer> qubits = new HashSet<>();

        if (gate instanceof CNOTGate) {
            CNOTGate cnot = (CNOTGate) gate;
            qubits.add(cnot.getControl().getValue());
            qubits.add(cnot.getTarget().getValue());
        } else {
            qubits.add(getSingleQubitTarget(gate));
        }

        return qubits;
    }

    private int getSingleQubitTarget(QuantumGate gate) {
        try {
            Object target = gate.getClass().getMethod("getTarget").invoke(gate);
            return (int) target.getClass().getMethod("getValue").invoke(target);
        } catch (Exception e) {
            return -1;
        }
    }

    @Override
    public String getValidationName() {
        return VALIDATION_NAME;
    }
}
