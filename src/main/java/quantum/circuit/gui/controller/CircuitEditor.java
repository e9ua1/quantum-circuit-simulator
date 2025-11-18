package quantum.circuit.gui.controller;

import quantum.circuit.domain.circuit.CircuitStep;
import quantum.circuit.domain.circuit.QubitIndex;
import quantum.circuit.domain.circuit.QuantumCircuit;
import quantum.circuit.domain.circuit.QuantumCircuitBuilder;
import quantum.circuit.domain.gate.*;
import quantum.circuit.gui.view.MainWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * 회로 편집 컨트롤러
 * 자유 모드에서 게이트를 정확한 위치에 추가/삭제하며 회로를 편집합니다.
 */
public class CircuitEditor {

    private final MainWindow mainWindow;
    private int qubitCount;
    private List<List<QuantumGate>> gateGrid;  // [stepIndex][gateIndex]

    public CircuitEditor(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        this.qubitCount = 3;
        this.gateGrid = new ArrayList<>();

        // 초기 회로 표시
        updateCircuit();
    }

    /**
     * 큐비트 수를 설정합니다.
     *
     * @param qubitCount 큐비트 수 (2-5)
     */
    public void setQubitCount(int qubitCount) {
        this.qubitCount = qubitCount;
        clearCircuit();
    }

    /**
     * 게이트를 회로의 정확한 위치에 추가합니다.
     *
     * @param gateName 게이트 이름 (H, X, Z, CNOT)
     * @param qubitIndex 큐비트 인덱스
     * @param stepIndex 추가할 단계 인덱스
     */
    public void addGate(String gateName, int qubitIndex, int stepIndex) {
        // 새 게이트 생성
        QuantumGate newGate = createGate(gateName, qubitIndex);
        if (newGate == null) {
            System.out.println("게이트 생성 실패: " + gateName);
            return;
        }

        // gateGrid 확장 (필요시)
        while (gateGrid.size() <= stepIndex) {
            gateGrid.add(new ArrayList<>());
        }

        // 해당 단계에 게이트 추가
        gateGrid.get(stepIndex).add(newGate);

        // 회로 업데이트
        updateCircuit();
    }

    /**
     * 회로를 초기화합니다.
     */
    public void clearCircuit() {
        gateGrid.clear();
        updateCircuit();
    }

    /**
     * 게이트 이름으로부터 QuantumGate 객체를 생성합니다.
     *
     * @param gateName 게이트 이름
     * @param qubitIndex 큐비트 인덱스
     * @return QuantumGate 객체
     */
    private QuantumGate createGate(String gateName, int qubitIndex) {
        QubitIndex target = new QubitIndex(qubitIndex);

        switch (gateName) {
            case "H":
                return new HadamardGate(target);
            case "X":
                return new PauliXGate(target);
            case "Z":
                return new PauliZGate(target);
            case "CNOT":
                // CNOT: 이전 큐비트를 제어로 사용
                int controlIndex = (qubitIndex > 0) ? qubitIndex - 1 : qubitIndex + 1;
                if (controlIndex >= 0 && controlIndex < qubitCount) {
                    QubitIndex control = new QubitIndex(controlIndex);
                    return new CNOTGate(control, target);
                }
                System.out.println("CNOT 제어 큐비트 범위 초과");
                return null;
            default:
                return null;
        }
    }

    /**
     * 현재 gateGrid로부터 회로를 빌드하고 화면에 표시합니다.
     */
    private void updateCircuit() {
        // gateGrid를 CircuitStep 리스트로 변환
        List<CircuitStep> steps = new ArrayList<>();

        for (List<QuantumGate> gatesInStep : gateGrid) {
            if (!gatesInStep.isEmpty()) {
                steps.add(new CircuitStep(new ArrayList<>(gatesInStep)));
            }
        }

        if (steps.isEmpty()) {
            // 빈 회로: 플레이스홀더 표시
            mainWindow.clearCircuit();
            return;
        }

        // 회로 빌드
        QuantumCircuitBuilder builder = new QuantumCircuitBuilder()
                .withQubits(qubitCount)
                .addSteps(steps);

        QuantumCircuit circuit = builder.build();
        mainWindow.setCircuit(circuit);
    }

    /**
     * 현재 큐비트 수를 반환합니다.
     *
     * @return 큐비트 수
     */
    public int getQubitCount() {
        return qubitCount;
    }
}
