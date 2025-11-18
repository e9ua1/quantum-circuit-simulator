package quantum.circuit.gui.renderer;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import quantum.circuit.domain.circuit.QuantumCircuit;

import java.util.function.BiConsumer;

/**
 * 인터랙티브 회로 캔버스
 * 게이트 클릭 처리를 추가한 CircuitCanvas
 */
public class InteractiveCircuitCanvas extends CircuitCanvas {

    private static final double QUBIT_LINE_Y_SPACING = 80.0;
    private static final double GATE_X_SPACING = 100.0;
    private static final double LEFT_MARGIN = 50.0;
    private static final double TOP_MARGIN = 50.0;
    private static final double GATE_SIZE = 50.0;

    private BiConsumer<Integer, Integer> onGateClicked;

    /**
     * 게이트 클릭 이벤트 핸들러 설정
     *
     * @param handler (qubitIndex, stepIndex)를 받는 핸들러
     */
    public void setOnGateClicked(BiConsumer<Integer, Integer> handler) {
        this.onGateClicked = handler;
    }

    @Override
    public Pane render(QuantumCircuit circuit, int currentStep) {
        Pane pane = super.render(circuit, currentStep);
        
        // 클릭 이벤트 추가
        pane.setOnMouseClicked(event -> handleClick(event, circuit));
        
        return pane;
    }

    /**
     * 마우스 클릭을 처리합니다.
     *
     * @param event 마우스 이벤트
     * @param circuit 현재 회로
     */
    private void handleClick(MouseEvent event, QuantumCircuit circuit) {
        double x = event.getX();
        double y = event.getY();

        // 클릭 위치로부터 게이트 위치 계산
        int qubitIndex = calculateQubitIndex(y);
        int stepIndex = calculateStepIndex(x);

        // 유효성 검사
        if (qubitIndex >= 0 && qubitIndex < circuit.getQubitCount() &&
            stepIndex >= 0 && stepIndex < circuit.getSteps().size()) {
            
            // 게이트가 실제로 해당 위치에 있는지 확인
            if (isGateAt(circuit, qubitIndex, stepIndex)) {
                if (onGateClicked != null) {
                    onGateClicked.accept(qubitIndex, stepIndex);
                }
                System.out.println(String.format(
                    "게이트 클릭: Q%d, Step %d (위치: %.1f, %.1f)",
                    qubitIndex, stepIndex, x, y
                ));
            }
        }
    }

    /**
     * Y 좌표로부터 큐비트 인덱스를 계산합니다.
     */
    private int calculateQubitIndex(double y) {
        double relativeY = y - TOP_MARGIN;
        if (relativeY < 0) return -1;
        
        int qubitIndex = (int) Math.round(relativeY / QUBIT_LINE_Y_SPACING);
        return qubitIndex;
    }

    /**
     * X 좌표로부터 단계 인덱스를 계산합니다.
     */
    private int calculateStepIndex(double x) {
        double relativeX = x - LEFT_MARGIN;
        if (relativeX < 0) return -1;
        
        // 게이트 중심 기준으로 클릭 감지
        int stepIndex = (int) Math.round(relativeX / GATE_X_SPACING) - 1;
        return stepIndex;
    }

    /**
     * 특정 위치에 게이트가 있는지 확인합니다.
     */
    private boolean isGateAt(QuantumCircuit circuit, int qubitIndex, int stepIndex) {
        if (stepIndex < 0 || stepIndex >= circuit.getSteps().size()) {
            return false;
        }

        var step = circuit.getSteps().get(stepIndex);
        for (var gate : step.getGates()) {
            // 게이트가 해당 큐비트에 영향을 주는지 확인
            for (var affectedQubit : gate.getAffectedQubits()) {
                if (affectedQubit.getValue() == qubitIndex) {
                    return true;
                }
            }
        }
        
        return false;
    }
}
