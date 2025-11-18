package quantum.circuit.gui.controller;

import quantum.circuit.algorithm.AlgorithmFactory;
import quantum.circuit.algorithm.AlgorithmType;
import quantum.circuit.algorithm.QuantumAlgorithm;
import quantum.circuit.domain.circuit.QuantumCircuit;
import quantum.circuit.gui.view.MainWindow;

/**
 * 회로 조작 컨트롤러
 * 알고리즘 로드, 회로 업데이트 등을 담당합니다.
 */
public class CircuitController {

    private final MainWindow mainWindow;
    private final AlgorithmFactory algorithmFactory;
    private QuantumCircuit currentCircuit;

    public CircuitController(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        this.algorithmFactory = new AlgorithmFactory();
        this.currentCircuit = null;
    }

    /**
     * 알고리즘을 로드하여 회로를 생성하고 표시합니다.
     *
     * @param algorithmType 로드할 알고리즘 타입
     */
    public void loadAlgorithm(AlgorithmType algorithmType) {
        QuantumAlgorithm algorithm = algorithmFactory.create(algorithmType.name());
        QuantumCircuit circuit = algorithm.build(algorithmType.getRequiredQubits());

        this.currentCircuit = circuit;
        mainWindow.setCircuit(circuit);
    }

    /**
     * 현재 표시된 회로를 반환합니다.
     *
     * @return 현재 회로 (없으면 null)
     */
    public QuantumCircuit getCurrentCircuit() {
        return currentCircuit;
    }

    /**
     * 회로를 직접 업데이트합니다.
     *
     * @param circuit 새로운 회로
     */
    public void updateCircuit(QuantumCircuit circuit) {
        this.currentCircuit = circuit;
        mainWindow.setCircuit(circuit);
    }
}
