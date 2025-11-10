package quantum.circuit.algorithm;

import java.util.List;

import quantum.circuit.domain.circuit.CircuitStep;
import quantum.circuit.domain.circuit.QubitIndex;
import quantum.circuit.domain.circuit.QuantumCircuitBuilder;
import quantum.circuit.domain.gate.HadamardGate;
import quantum.circuit.domain.gate.XGate;
import quantum.circuit.domain.gate.ZGate;

public class GroverAlgorithm extends QuantumAlgorithm {

    private static final String ALGORITHM_NAME = "Grover's Search";
    private static final String DESCRIPTION = "Grover의 검색 알고리즘을 수행합니다. "
            + "2큐비트에 대한 간소화된 버전으로, 특정 상태를 증폭합니다.";
    private static final int REQUIRED_QUBITS = 2;

    @Override
    protected void applyMainAlgorithm(QuantumCircuitBuilder builder) {
        // Step 1: 초기 중첩 상태 생성 (Hadamard)
        builder.addStep(new CircuitStep(List.of(
                new HadamardGate(new QubitIndex(0)),
                new HadamardGate(new QubitIndex(1))
        )));

        // Step 2: Oracle (간소화 - Z 게이트로 위상 반전)
        builder.addStep(new CircuitStep(List.of(
                new ZGate(new QubitIndex(0))
        )));

        // Step 3: Diffusion operator (간소화)
        builder.addStep(new CircuitStep(List.of(
                new HadamardGate(new QubitIndex(0)),
                new HadamardGate(new QubitIndex(1))
        )));

        builder.addStep(new CircuitStep(List.of(
                new XGate(new QubitIndex(0)),
                new XGate(new QubitIndex(1))
        )));

        builder.addStep(new CircuitStep(List.of(
                new HadamardGate(new QubitIndex(0)),
                new HadamardGate(new QubitIndex(1))
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
