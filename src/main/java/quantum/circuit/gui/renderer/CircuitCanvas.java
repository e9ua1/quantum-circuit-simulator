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

    private static final Color WIRE_COLOR = Color.rgb(52, 73, 94);
    private static final Color GATE_COLOR = Color.rgb(52, 152, 219);
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Color CNOT_COLOR = Color.rgb(231, 76, 60);

    @Override
    public Pane render(QuantumCircuit circuit) {
        VisualizationData data = VisualizationData.from(circuit);
        
        double canvasWidth = LEFT_MARGIN + (data.getStepCount() + 1) * GATE_X_SPACING;
        double canvasHeight = TOP_MARGIN + data.getQubitCount() * QUBIT_LINE_Y_SPACING;

        Canvas canvas = new Canvas(canvasWidth, canvasHeight);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        drawCircuit(gc, data);

        Pane pane = new Pane(canvas);
        pane.setStyle("-fx-background-color: white;");
        return pane;
    }

    private void drawCircuit(GraphicsContext gc, VisualizationData data) {
        drawQubitLines(gc, data);
        drawQubitLabels(gc, data);
        drawGates(gc, data);
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

    private void drawGates(GraphicsContext gc, VisualizationData data) {
        for (int step = 0; step < data.getStepCount(); step++) {
            double x = getStepX(step);
            
            for (var gate : data.getStep(step).getGates()) {
                if (gate instanceof CNOTGate) {
                    drawCNOTGate(gc, (CNOTGate) gate, x);
                } else {
                    drawSingleQubitGate(gc, gate, x);
                }
            }
        }
    }

    private void drawSingleQubitGate(GraphicsContext gc, QuantumGate gate, double x) {
        int qubit = gate.getAffectedQubits().iterator().next().getValue();
        double y = getQubitY(qubit);

        // 게이트 박스
        gc.setFill(GATE_COLOR);
        gc.fillRect(x - GATE_SIZE / 2, y - GATE_SIZE / 2, GATE_SIZE, GATE_SIZE);
        
        // 게이트 테두리
        gc.setStroke(WIRE_COLOR);
        gc.setLineWidth(2);
        gc.strokeRect(x - GATE_SIZE / 2, y - GATE_SIZE / 2, GATE_SIZE, GATE_SIZE);

        // 게이트 이름
        gc.setFill(TEXT_COLOR);
        gc.setFont(Font.font("Arial", 20));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText(gate.getName(), x, y + 7);
    }

    private void drawCNOTGate(GraphicsContext gc, CNOTGate gate, double x) {
        int control = gate.getControl().getValue();
        int target = gate.getTarget().getValue();

        double controlY = getQubitY(control);
        double targetY = getQubitY(target);

        // 제어선
        gc.setStroke(CNOT_COLOR);
        gc.setLineWidth(LINE_WIDTH);
        gc.strokeLine(x, controlY, x, targetY);

        // 제어점 (●)
        gc.setFill(CNOT_COLOR);
        gc.fillOval(x - CONTROL_RADIUS, controlY - CONTROL_RADIUS, 
                    CONTROL_RADIUS * 2, CONTROL_RADIUS * 2);

        // 타겟 (⊕)
        gc.setStroke(CNOT_COLOR);
        gc.setLineWidth(LINE_WIDTH);
        gc.strokeOval(x - TARGET_RADIUS, targetY - TARGET_RADIUS, 
                      TARGET_RADIUS * 2, TARGET_RADIUS * 2);
        gc.strokeLine(x, targetY - TARGET_RADIUS, x, targetY + TARGET_RADIUS); // 세로선
        gc.strokeLine(x - TARGET_RADIUS, targetY, x + TARGET_RADIUS, targetY); // 가로선
    }

    private double getQubitY(int qubit) {
        return TOP_MARGIN + qubit * QUBIT_LINE_Y_SPACING;
    }

    private double getStepX(int step) {
        return LEFT_MARGIN + (step + 1) * GATE_X_SPACING;
    }
}
