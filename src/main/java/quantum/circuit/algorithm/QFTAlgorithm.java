package quantum.circuit.algorithm;

import java.util.List;

import quantum.circuit.domain.circuit.CircuitStep;
import quantum.circuit.domain.circuit.QubitIndex;
import quantum.circuit.domain.circuit.QuantumCircuitBuilder;
import quantum.circuit.domain.gate.HadamardGate;

public class QFTAlgorithm extends QuantumAlgorithm {

    private static final String ALGORITHM_NAME = "QFT";
    private static final String DESCRIPTION = "양자 푸리에 변환(Quantum Fourier Transform)을 수행합니다. "
            + "2큐비트에 대한 간소화된 QFT를 구현합니다.";
    private static final int REQUIRED_QUBITS = 2;

    @Override
    protected void applyMainAlgorithm(QuantumCircuitBuilder builder) {
        // 2-qubit QFT (simplified)
        // Q0에 Hadamard
        builder.addStep(new CircuitStep(List.of(
                new HadamardGate(new QubitIndex(0))
        )));

        // Q1에 Hadamard
        builder.addStep(new CircuitStep(List.of(
                new HadamardGate(new QubitIndex(1))
        )));

        // 실제 QFT는 위상 회전(rotation) 게이트가 필요하지만,
        // 여기서는 Hadamard만으로 간소화된 버전 구현
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
