package quantum.circuit;

import java.util.List;

import quantum.circuit.domain.circuit.CircuitStep;
import quantum.circuit.domain.circuit.QubitIndex;
import quantum.circuit.domain.circuit.QuantumCircuit;
import quantum.circuit.domain.circuit.QuantumCircuitBuilder;
import quantum.circuit.domain.gate.CNOTGate;
import quantum.circuit.domain.gate.HadamardGate;
import quantum.circuit.domain.state.QuantumState;
import quantum.circuit.visualizer.PythonVisualizer;

public class VisualizationDemo {

    public static void main(String[] args) {
        System.out.println("=== Bell State 시각화 데모 ===\n");

        // Bell State 회로 생성
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(2)
                .addStep(new CircuitStep(List.of(
                        new HadamardGate(new QubitIndex(0))
                )))
                .addStep(new CircuitStep(List.of(
                        new CNOTGate(new QubitIndex(0), new QubitIndex(1))
                )))
                .build();

        // 실행
        QuantumState state = circuit.execute();

        // Python 시각화 자동 실행
        System.out.println("Python 시각화 시작...\n");
        PythonVisualizer.visualize(circuit, state, "Bell State");

        System.out.println("\n시각화 완료!");
        System.out.println("결과 확인: open output/bloch_sphere.png");
        System.out.println("결과 확인: open output/histogram.png");
    }
}
