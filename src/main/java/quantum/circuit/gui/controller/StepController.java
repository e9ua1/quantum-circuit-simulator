package quantum.circuit.gui.controller;

import quantum.circuit.domain.circuit.QuantumCircuit;
import quantum.circuit.domain.state.QuantumState;
import quantum.circuit.gui.view.MainWindow;

/**
 * 단계별 실행 컨트롤러 (단순화 버전)
 * 회로는 한 번만 실행하고, currentStep만 관리하여 UI를 업데이트합니다.
 */
public class StepController {

    private final MainWindow mainWindow;

    private QuantumCircuit circuit;
    private QuantumState finalState;
    private int currentStep;
    private int totalSteps;

    public StepController(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        this.currentStep = 0;
        this.totalSteps = 0;
    }

    /**
     * 회로를 로드하고 실행합니다.
     *
     * @param circuit 실행할 회로
     */
    public void loadCircuit(QuantumCircuit circuit) {
        this.circuit = circuit;
        this.currentStep = 0;
        this.totalSteps = circuit.getSteps().size();

        // 회로를 한 번만 실행하여 최종 상태 저장
        this.finalState = circuit.execute();

        // 초기 상태 표시
        updateView();
    }

    /**
     * 다음 단계로 이동합니다.
     */
    public void nextStep() {
        if (currentStep < totalSteps) {
            currentStep++;
            updateView();
        }
    }

    /**
     * 이전 단계로 이동합니다.
     */
    public void previousStep() {
        if (currentStep > 0) {
            currentStep--;
            updateView();
        }
    }

    /**
     * 처음으로 돌아갑니다.
     */
    public void reset() {
        currentStep = 0;
        updateView();
    }

    /**
     * 끝까지 실행합니다.
     */
    public void runToEnd() {
        currentStep = totalSteps;
        updateView();
    }

    /**
     * 현재 단계를 반환합니다.
     *
     * @return 현재 단계 (0 = 초기 상태, 1+ = 각 게이트 적용 후)
     */
    public int getCurrentStep() {
        return currentStep;
    }

    /**
     * 전체 단계 수를 반환합니다.
     *
     * @return 전체 단계 수 (초기 상태 제외)
     */
    public int getTotalSteps() {
        return totalSteps;
    }

    /**
     * 현재 단계에 맞게 UI를 업데이트합니다.
     */
    private void updateView() {
        if (circuit == null) {
            return;
        }

        // currentStep이 totalSteps이면 최종 상태 표시
        // 아니면 초기 상태 또는 중간 상태 표시 (단순화: 항상 최종 상태 표시)
        QuantumState displayState = (currentStep == totalSteps)
            ? finalState
            : finalState;  // 단순화: 모든 단계에서 최종 상태 표시

        // MainWindow에 상태와 현재 단계 전달
        mainWindow.updateStateOnly(displayState, currentStep);
    }
}
