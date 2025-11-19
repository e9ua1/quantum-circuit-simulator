package quantum.circuit.gui.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import quantum.circuit.domain.state.QuantumState;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SystemStateHistogram {

    private static final double SPACING = 8.0;
    private static final double PADDING = 10.0;
    private static final double BAR_HEIGHT = 25.0;

    private final VBox root;
    private final List<StateBar> stateBars;

    public SystemStateHistogram() {
        this.root = new VBox(SPACING);
        this.stateBars = new ArrayList<>();

        setupLayout();
        applyStyles();
    }

    private void setupLayout() {
        root.setAlignment(Pos.CENTER_LEFT);
        root.setPadding(new Insets(PADDING));

        Label placeholder = new Label("회로를 실행하면\n전체 상태 분포가 표시됩니다");
        placeholder.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 12px;");
        placeholder.setAlignment(Pos.CENTER);
        root.getChildren().add(placeholder);
    }

    private void applyStyles() {
        root.setStyle("-fx-background-color: white; -fx-background-radius: 5;");
    }

    public void updateState(QuantumState state) {
        root.getChildren().clear();
        stateBars.clear();

        Map<String, Double> probabilities = state.getBasisStateProbabilities();

        for (Map.Entry<String, Double> entry : probabilities.entrySet()) {
            String basisState = entry.getKey();
            Double probability = entry.getValue();

            StateBar stateBar = new StateBar(basisState, probability);
            stateBars.add(stateBar);
            root.getChildren().add(stateBar.getRoot());
        }
    }

    public VBox getRoot() {
        return root;
    }

    private static class StateBar {
        private final HBox root;
        private final Label stateLabel;
        private final ProgressBar progressBar;
        private final Label probabilityLabel;

        public StateBar(String basisState, Double probability) {
            this.root = new HBox(10);
            this.stateLabel = createStateLabel(basisState);
            this.progressBar = createProgressBar(probability);
            this.probabilityLabel = createProbabilityLabel(probability);

            setupLayout();
        }

        private Label createStateLabel(String basisState) {
            Label label = new Label("|" + basisState + "⟩");
            label.setFont(Font.font("Monospace", FontWeight.BOLD, 14));
            label.setStyle("-fx-text-fill: #2c3e50;");
            label.setMinWidth(80);
            return label;
        }

        private ProgressBar createProgressBar(Double probability) {
            ProgressBar bar = new ProgressBar(probability);
            bar.setPrefHeight(BAR_HEIGHT);
            bar.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(bar, Priority.ALWAYS);

            String color = probability > 0.01 ? "#3498db" : "#ecf0f1";
            bar.setStyle(
                    "-fx-accent: " + color + ";" +
                            "-fx-control-inner-background: #ecf0f1;"
            );

            return bar;
        }

        private Label createProbabilityLabel(Double probability) {
            Label label = new Label(String.format("%.1f%%", probability * 100));
            label.setFont(Font.font("Monospace", 12));
            label.setStyle("-fx-text-fill: #7f8c8d;");
            label.setMinWidth(60);
            label.setAlignment(Pos.CENTER_RIGHT);
            return label;
        }

        private void setupLayout() {
            root.setAlignment(Pos.CENTER_LEFT);
            root.getChildren().addAll(stateLabel, progressBar, probabilityLabel);
        }

        public HBox getRoot() {
            return root;
        }
    }
}
