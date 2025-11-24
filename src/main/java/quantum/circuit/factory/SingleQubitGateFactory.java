package quantum.circuit.factory;

import quantum.circuit.domain.circuit.QubitIndex;
import quantum.circuit.domain.gate.HadamardGate;
import quantum.circuit.domain.gate.PauliXGate;
import quantum.circuit.domain.gate.PauliZGate;
import quantum.circuit.domain.gate.QuantumGate;

public class SingleQubitGateFactory {

    private static final String GATE_X = "X";
    private static final String GATE_H = "H";
    private static final String GATE_Z = "Z";
    private static final String ERROR_NULL_OR_EMPTY = "게이트 타입은 null이거나 비어있을 수 없습니다.";
    private static final String ERROR_UNSUPPORTED_GATE = "지원하지 않는 게이트입니다.";

    private SingleQubitGateFactory() {
    }

    public static QuantumGate create(String gateType, QubitIndex target) {
        validateGateType(gateType);
        String normalizedType = gateType.strip().toUpperCase();

        if (GATE_X.equals(normalizedType)) {
            return new PauliXGate(target);
        }
        if (GATE_H.equals(normalizedType)) {
            return new HadamardGate(target);
        }
        if (GATE_Z.equals(normalizedType)) {
            return new PauliZGate(target);
        }
        throw new IllegalArgumentException(ERROR_UNSUPPORTED_GATE);
    }

    private static void validateGateType(String gateType) {
        if (gateType == null || gateType.isBlank()) {
            throw new IllegalArgumentException(ERROR_NULL_OR_EMPTY);
        }
    }
}
