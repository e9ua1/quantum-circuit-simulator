package quantum.circuit.algorithm;

import java.util.List;

import quantum.circuit.domain.circuit.CircuitStep;
import quantum.circuit.domain.circuit.QubitIndex;
import quantum.circuit.domain.circuit.QuantumCircuitBuilder;
import quantum.circuit.domain.gate.CNOTGate;
import quantum.circuit.domain.gate.HadamardGate;

public class BellStateAlgorithm extends QuantumAlgorithm {

    private static final String ALGORITHM_NAME = "Bell State";
    private static final String DESCRIPTION = "2큐비트 최대 얽힘 상태를 생성합니다. "
            + "H 게이트로 중첩 상태를 만든 후 CNOT 게이트로 얽힘 상태를 생성합니다.";
    private static final int REQUIRED_QUBITS = 2;

    @Override
    protected void applyMainAlgorithm(QuantumCircuitBuilder builder) {
        // Step 1: H(Q0)
        builder.addStep(new CircuitStep(List.of(
                new HadamardGate(new QubitIndex(0))
        )));

        // Step 2: CNOT(Q0→Q1)
        builder.addStep(new CircuitStep(List.of(
                new CNOTGate(new QubitIndex(0), new QubitIndex(1))
        )));
    }

    @Override
    public String getName() {
        return ALGORITHM_NAME;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public int getRequiredQubits() {
        return REQUIRED_QUBITS;
    }
}
