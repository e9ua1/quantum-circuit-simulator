package quantum.circuit.gui.controller;

import quantum.circuit.domain.circuit.QuantumCircuit;
import quantum.circuit.domain.state.QuantumState;
import quantum.circuit.gui.view.MainWindow;

/**
 * 단계별 실행 컨트롤러
 * executeUntilStep을 사용하여 실제 단계별 상태를 계산합니다.
 */
public class StepController {

    private final MainWindow mainWindow;

    private QuantumCircuit circuit;
    private int currentStep;
    private int totalSteps;

    public StepController(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        this.currentStep = 0;
        this.totalSteps = 0;
    }

    /**
     * 회로를 로드합니다.
     *
     * @param circuit 실행할 회로
     */
    public void loadCircuit(QuantumCircuit circuit) {
        this.circuit = circuit;
        this.currentStep = 0;
        this.totalSteps = circuit.getSteps().size();

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

    public int getCurrentStep() {
        return currentStep;
    }

    public int getTotalSteps() {
        return totalSteps;
    }

    /**
     * 현재 단계에 맞는 실제 상태를 계산하여 UI를 업데이트합니다.
     */
    private void updateView() {
        if (circuit == null) {
            return;
        }

        // 현재 단계까지 실행한 실제 상태 계산
        QuantumState currentState = circuit.executeUntilStep(currentStep);

        // MainWindow에 상태와 현재 단계 전달
        mainWindow.updateStateOnly(currentState, currentStep);
    }
}
