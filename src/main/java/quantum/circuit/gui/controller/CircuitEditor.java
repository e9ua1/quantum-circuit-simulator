package quantum.circuit.gui.controller;

import quantum.circuit.domain.circuit.CircuitStep;
import quantum.circuit.domain.circuit.QubitIndex;
import quantum.circuit.domain.circuit.QuantumCircuit;
import quantum.circuit.domain.circuit.QuantumCircuitBuilder;
import quantum.circuit.domain.gate.*;
import quantum.circuit.gui.view.MainWindow;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 회로 편집 컨트롤러
 * 게이트 추가/삭제, 언두/리두 기능 제공
 */
public class CircuitEditor {

    private final MainWindow mainWindow;
    private int qubitCount;
    private List<List<QuantumGate>> gateGrid;

    // 언두/리두를 위한 히스토리
    private Stack<List<List<QuantumGate>>> undoStack;
    private Stack<List<List<QuantumGate>>> redoStack;

    public CircuitEditor(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        this.qubitCount = 3;
        this.gateGrid = new ArrayList<>();
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();

        updateCircuit();
    }

    /**
     * 큐비트 수를 설정합니다.
     */
    public void setQubitCount(int qubitCount) {
        this.qubitCount = qubitCount;
        clearCircuit();
    }

    /**
     * 게이트를 회로의 정확한 위치에 추가합니다.
     */
    public void addGate(String gateName, int qubitIndex, int stepIndex) {
        // 히스토리 저장
        saveState();

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
     * 특정 위치의 게이트를 삭제합니다.
     *
     * @param qubitIndex 큐비트 인덱스
     * @param stepIndex 단계 인덱스
     */
    public void removeGate(int qubitIndex, int stepIndex) {
        if (stepIndex < 0 || stepIndex >= gateGrid.size()) {
            return;
        }

        // 히스토리 저장
        saveState();

        // 해당 위치의 게이트 찾아서 삭제
        List<QuantumGate> gatesInStep = gateGrid.get(stepIndex);
        gatesInStep.removeIf(gate -> {
            // 게이트가 해당 큐비트에 영향을 주는지 확인
            for (var affectedQubit : gate.getAffectedQubits()) {
                if (affectedQubit.getValue() == qubitIndex) {
                    return true;
                }
            }
            return false;
        });

        // 빈 단계 제거
        if (gatesInStep.isEmpty()) {
            gateGrid.remove(stepIndex);
        }

        // 회로 업데이트
        updateCircuit();

        System.out.println(String.format("게이트 삭제: Q%d, Step %d", qubitIndex, stepIndex));
    }

    /**
     * 회로를 초기화합니다.
     */
    public void clearCircuit() {
        saveState();
        gateGrid.clear();
        updateCircuit();
    }

    /**
     * 실행 취소 (Undo)
     */
    public void undo() {
        if (!canUndo()) {
            return;
        }

        // 현재 상태를 리두 스택에 저장
        redoStack.push(copyGrid(gateGrid));

        // 이전 상태로 복원
        gateGrid = undoStack.pop();

        updateCircuit();
        System.out.println("언두 실행");
    }

    /**
     * 다시 실행 (Redo)
     */
    public void redo() {
        if (!canRedo()) {
            return;
        }

        // 현재 상태를 언두 스택에 저장
        undoStack.push(copyGrid(gateGrid));

        // 다음 상태로 복원
        gateGrid = redoStack.pop();

        updateCircuit();
        System.out.println("리두 실행");
    }

    /**
     * 언두 가능 여부
     */
    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    /**
     * 리두 가능 여부
     */
    public boolean canRedo() {
        return !redoStack.isEmpty();
    }

    /**
     * 현재 상태를 히스토리에 저장합니다.
     */
    private void saveState() {
        undoStack.push(copyGrid(gateGrid));
        redoStack.clear();  // 새 액션 후 리두 스택 클리어
    }

    /**
     * gateGrid의 깊은 복사본을 생성합니다.
     */
    private List<List<QuantumGate>> copyGrid(List<List<QuantumGate>> original) {
        List<List<QuantumGate>> copy = new ArrayList<>();
        for (List<QuantumGate> step : original) {
            copy.add(new ArrayList<>(step));
        }
        return copy;
    }

    /**
     * 게이트 이름으로부터 QuantumGate 객체를 생성합니다.
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
        List<CircuitStep> steps = new ArrayList<>();

        for (List<QuantumGate> gatesInStep : gateGrid) {
            if (!gatesInStep.isEmpty()) {
                steps.add(new CircuitStep(new ArrayList<>(gatesInStep)));
            }
        }

        if (steps.isEmpty()) {
            mainWindow.clearCircuit();
            return;
        }

        QuantumCircuitBuilder builder = new QuantumCircuitBuilder()
                .withQubits(qubitCount)
                .addSteps(steps);

        QuantumCircuit circuit = builder.build();
        mainWindow.setCircuit(circuit);
    }

    public int getQubitCount() {
        return qubitCount;
    }
}
