package quantum.circuit.algorithm;

import java.util.List;

import quantum.circuit.domain.circuit.CircuitStep;
import quantum.circuit.domain.circuit.QubitIndex;
import quantum.circuit.domain.circuit.QuantumCircuitBuilder;
import quantum.circuit.domain.gate.CNOTGate;
import quantum.circuit.domain.gate.HadamardGate;
import quantum.circuit.domain.gate.PauliXGate;

public class DeutschJozsaAlgorithm extends QuantumAlgorithm {

    private static final String ALGORITHM_NAME = "Deutsch-Jozsa";
    private static final String DESCRIPTION = "Deutsch-Jozsa 알고리즘을 수행합니다. "
            + "함수가 상수(constant)인지 균형(balanced)인지를 단 한 번의 질의로 판별합니다.";
    private static final int REQUIRED_QUBITS = 2;

    @Override
    protected void prepareInitialState(QuantumCircuitBuilder builder) {
        // 보조 큐비트(Q1)를 |1⟩ 상태로 준비
        builder.addStep(new CircuitStep(List.of(
                new PauliXGate(new QubitIndex(1))
        )));
    }

    @Override
    protected void applyMainAlgorithm(QuantumCircuitBuilder builder) {
        // Step 1: 모든 큐비트에 Hadamard 적용
        builder.addStep(new CircuitStep(List.of(
                new HadamardGate(new QubitIndex(0)),
                new HadamardGate(new QubitIndex(1))
        )));

        // Step 2: Oracle (간소화 - CNOT로 균형 함수 구현)
        builder.addStep(new CircuitStep(List.of(
                new CNOTGate(new QubitIndex(0), new QubitIndex(1))
        )));

        // Step 3: 입력 큐비트에 Hadamard 적용
        builder.addStep(new CircuitStep(List.of(
                new HadamardGate(new QubitIndex(0))
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
