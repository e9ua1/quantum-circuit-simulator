package quantum.circuit.gui.renderer;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import quantum.circuit.domain.circuit.QuantumCircuit;
import quantum.circuit.domain.gate.CNOTGate;
import quantum.circuit.domain.gate.QuantumGate;
import quantum.circuit.gui.model.VisualizationData;

public class CircuitCanvas implements CircuitRenderer {

    private static final double QUBIT_LINE_Y_SPACING = 80.0;
    private static final double GATE_X_SPACING = 100.0;
    private static final double LEFT_MARGIN = 50.0;
    private static final double TOP_MARGIN = 50.0;
    private static final double GATE_SIZE = 50.0;
    private static final double LINE_WIDTH = 2.0;
    private static final double CONTROL_RADIUS = 8.0;
    private static final double TARGET_RADIUS = 20.0;
    private static final double HIGHLIGHT_LINE_WIDTH = 4.0;

    private static final Color WIRE_COLOR = Color.rgb(52, 73, 94);
    private static final Color GATE_COLOR = Color.rgb(52, 152, 219);
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Color CNOT_COLOR = Color.rgb(231, 76, 60);

    // 하이라이팅 색상
    private static final Color EXECUTED_COLOR = Color.rgb(46, 204, 113);  // 초록색 (실행됨)
    private static final Color CURRENT_COLOR = Color.rgb(241, 196, 15);   // 노란색 (현재)
    private static final Color PENDING_COLOR = Color.rgb(189, 195, 199);  // 회색 (대기중)

    @Override
    public Pane render(QuantumCircuit circuit) {
        return render(circuit, -1);  // 하이라이팅 없음
    }

    /**
     * 회로를 렌더링하며 현재 단계의 게이트를 하이라이팅합니다.
     *
     * @param circuit 렌더링할 회로
     * @param currentStep 현재 실행 단계 (0 = 초기, 1+ = 각 단계)
     * @return 렌더링된 Pane
     */
    public Pane render(QuantumCircuit circuit, int currentStep) {
        VisualizationData data = VisualizationData.from(circuit);

        double canvasWidth = LEFT_MARGIN + (data.getStepCount() + 1) * GATE_X_SPACING;
        double canvasHeight = TOP_MARGIN + data.getQubitCount() * QUBIT_LINE_Y_SPACING;

        Canvas canvas = new Canvas(canvasWidth, canvasHeight);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        drawCircuit(gc, data, currentStep);

        Pane pane = new Pane(canvas);
        pane.setStyle("-fx-background-color: white;");
        return pane;
    }

    private void drawCircuit(GraphicsContext gc, VisualizationData data, int currentStep) {
        drawQubitLines(gc, data);
        drawQubitLabels(gc, data);
        drawGates(gc, data, currentStep);
    }

    private void drawQubitLines(GraphicsContext gc, VisualizationData data) {
        gc.setStroke(WIRE_COLOR);
        gc.setLineWidth(LINE_WIDTH);

        for (int qubit = 0; qubit < data.getQubitCount(); qubit++) {
            double y = getQubitY(qubit);
            double endX = LEFT_MARGIN + (data.getStepCount() + 1) * GATE_X_SPACING;
            gc.strokeLine(LEFT_MARGIN, y, endX, y);
        }
    }

    private void drawQubitLabels(GraphicsContext gc, VisualizationData data) {
        gc.setFill(WIRE_COLOR);
        gc.setFont(Font.font("Monospace", 16));
        gc.setTextAlign(TextAlignment.RIGHT);

        for (int qubit = 0; qubit < data.getQubitCount(); qubit++) {
            double y = getQubitY(qubit);
            gc.fillText("Q" + qubit, LEFT_MARGIN - 10, y + 5);
        }
    }

    private void drawGates(GraphicsContext gc, VisualizationData data, int currentStep) {
        for (int step = 0; step < data.getStepCount(); step++) {
            double x = getStepX(step);
            GateHighlightState highlightState = getHighlightState(step, currentStep);

            for (var gate : data.getStep(step).getGates()) {
                if (gate instanceof CNOTGate) {
                    drawCNOTGate(gc, (CNOTGate) gate, x, highlightState);
                } else {
                    drawSingleQubitGate(gc, gate, x, highlightState);
                }
            }
        }
    }

    private GateHighlightState getHighlightState(int gateStep, int currentStep) {
        if (currentStep < 0) {
            return GateHighlightState.NORMAL;  // 하이라이팅 없음
        }

        // gateStep은 0부터 시작, currentStep도 0부터 시작
        // currentStep 0 = 초기 상태 (아무것도 실행 안 됨)
        // currentStep 1 = 첫 번째 게이트(step 0) 실행됨
        // currentStep 2 = 두 번째 게이트(step 1)까지 실행됨

        if (gateStep < currentStep) {
            return GateHighlightState.EXECUTED;  // 이미 실행됨
        } else if (gateStep == currentStep) {
            return GateHighlightState.CURRENT;   // 현재 실행 중
        } else {
            return GateHighlightState.PENDING;   // 아직 실행 안 됨
        }
    }

    private void drawSingleQubitGate(GraphicsContext gc, QuantumGate gate, double x, GateHighlightState highlightState) {
        int qubit = gate.getAffectedQubits().iterator().next().getValue();
        double y = getQubitY(qubit);

        // 게이트 색상 결정
        Color gateColor = getGateColor(highlightState);
        double opacity = getGateOpacity(highlightState);

        // 게이트 박스
        gc.setGlobalAlpha(opacity);
        gc.setFill(gateColor);
        gc.fillRect(x - GATE_SIZE / 2, y - GATE_SIZE / 2, GATE_SIZE, GATE_SIZE);
        gc.setGlobalAlpha(1.0);

        // 게이트 테두리
        Color borderColor = getBorderColor(highlightState);
        double borderWidth = getBorderWidth(highlightState);
        gc.setStroke(borderColor);
        gc.setLineWidth(borderWidth);
        gc.strokeRect(x - GATE_SIZE / 2, y - GATE_SIZE / 2, GATE_SIZE, GATE_SIZE);

        // 게이트 이름
        gc.setFill(TEXT_COLOR);
        gc.setFont(Font.font("Arial", 20));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText(gate.getName(), x, y + 7);
    }

    private void drawCNOTGate(GraphicsContext gc, CNOTGate gate, double x, GateHighlightState highlightState) {
        int control = gate.getControl().getValue();
        int target = gate.getTarget().getValue();

        double controlY = getQubitY(control);
        double targetY = getQubitY(target);

        // 색상 및 투명도
        Color gateColor = getCNOTColor(highlightState);
        double opacity = getGateOpacity(highlightState);
        double lineWidth = getBorderWidth(highlightState);

        // 제어선
        gc.setGlobalAlpha(opacity);
        gc.setStroke(gateColor);
        gc.setLineWidth(lineWidth);
        gc.strokeLine(x, controlY, x, targetY);

        // 제어점 (●)
        gc.setFill(gateColor);
        gc.fillOval(x - CONTROL_RADIUS, controlY - CONTROL_RADIUS,
                CONTROL_RADIUS * 2, CONTROL_RADIUS * 2);

        // 타겟 (⊕)
        gc.setStroke(gateColor);
        gc.strokeOval(x - TARGET_RADIUS, targetY - TARGET_RADIUS,
                TARGET_RADIUS * 2, TARGET_RADIUS * 2);
        gc.strokeLine(x, targetY - TARGET_RADIUS, x, targetY + TARGET_RADIUS); // 세로선
        gc.strokeLine(x - TARGET_RADIUS, targetY, x + TARGET_RADIUS, targetY); // 가로선

        gc.setGlobalAlpha(1.0);
    }

    private Color getGateColor(GateHighlightState state) {
        switch (state) {
            case EXECUTED:
                return GATE_COLOR;  // 실행된 게이트는 원래 색상
            case CURRENT:
                return GATE_COLOR;  // 현재 게이트도 원래 색상
            case PENDING:
                return Color.rgb(189, 195, 199);  // 회색
            default:
                return GATE_COLOR;
        }
    }

    private Color getCNOTColor(GateHighlightState state) {
        switch (state) {
            case EXECUTED:
                return CNOT_COLOR;  // 실행된 CNOT은 원래 색상
            case CURRENT:
                return CNOT_COLOR;  // 현재 CNOT도 원래 색상
            case PENDING:
                return Color.rgb(189, 195, 199);  // 회색
            default:
                return CNOT_COLOR;
        }
    }

    private Color getBorderColor(GateHighlightState state) {
        switch (state) {
            case EXECUTED:
                return EXECUTED_COLOR;  // 초록색 테두리
            case CURRENT:
                return CURRENT_COLOR;   // 노란색 테두리
            case PENDING:
                return PENDING_COLOR;   // 회색 테두리
            default:
                return WIRE_COLOR;
        }
    }

    private double getBorderWidth(GateHighlightState state) {
        switch (state) {
            case CURRENT:
                return HIGHLIGHT_LINE_WIDTH;  // 현재 게이트는 굵은 테두리
            default:
                return LINE_WIDTH;
        }
    }

    private double getGateOpacity(GateHighlightState state) {
        switch (state) {
            case PENDING:
                return 0.3;  // 대기 중인 게이트는 반투명
            default:
                return 1.0;
        }
    }

    private double getQubitY(int qubit) {
        return TOP_MARGIN + qubit * QUBIT_LINE_Y_SPACING;
    }

    private double getStepX(int step) {
        return LEFT_MARGIN + (step + 1) * GATE_X_SPACING;
    }

    /**
     * 게이트 하이라이팅 상태
     */
    private enum GateHighlightState {
        NORMAL,    // 하이라이팅 없음
        EXECUTED,  // 이미 실행됨 (초록색 테두리)
        CURRENT,   // 현재 실행 중 (노란색 테두리, 굵음)
        PENDING    // 아직 실행 안 됨 (회색, 반투명)
    }
}
