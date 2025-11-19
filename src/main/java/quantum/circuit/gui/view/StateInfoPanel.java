package quantum.circuit.gui.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import quantum.circuit.domain.circuit.QubitIndex;
import quantum.circuit.domain.state.Probability;
import quantum.circuit.domain.state.QuantumState;
import quantum.circuit.gui.renderer.BlochSphereRenderer;

public class StateInfoPanel {

    private static final double SPACING = 15.0;
    private static final double PADDING = 20.0;

    private final VBox root;
    private final BlochSphereRenderer blochSphereRenderer;
    private final VBox probabilityInfoBox;
    private final SystemStateHistogram histogram;

    public StateInfoPanel() {
        this.root = new VBox(SPACING);
        this.blochSphereRenderer = new BlochSphereRenderer();
        this.probabilityInfoBox = new VBox(5);
        this.histogram = new SystemStateHistogram();

        setupLayout();
        applyStyles();
    }

    private void setupLayout() {
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(PADDING));

        Label titleLabel = new Label("양자 상태 정보");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        titleLabel.setStyle("-fx-text-fill: #2c3e50;");

        Label blochLabel = new Label("블로흐 구면 (Qubit 0)");
        blochLabel.setFont(Font.font("System", FontWeight.SEMI_BOLD, 14));
        blochLabel.setStyle("-fx-text-fill: #34495e;");

        SubScene blochSphere = blochSphereRenderer.render(0.0);

        Label histogramLabel = new Label("전체 시스템 상태");
        histogramLabel.setFont(Font.font("System", FontWeight.SEMI_BOLD, 14));
        histogramLabel.setStyle("-fx-text-fill: #34495e;");

        Label probabilityLabel = new Label("개별 큐비트 확률");
        probabilityLabel.setFont(Font.font("System", FontWeight.SEMI_BOLD, 14));
        probabilityLabel.setStyle("-fx-text-fill: #34495e;");

        probabilityInfoBox.setAlignment(Pos.CENTER_LEFT);
        probabilityInfoBox.setPadding(new Insets(10));
        probabilityInfoBox.setStyle("-fx-background-color: white; -fx-background-radius: 5;");

        Label placeholderLabel = new Label("회로를 실행하면\n확률 정보가 표시됩니다");
        placeholderLabel.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 12px;");
        placeholderLabel.setAlignment(Pos.CENTER);
        probabilityInfoBox.getChildren().add(placeholderLabel);

        root.getChildren().addAll(
                titleLabel,
                new Separator(),
                blochLabel,
                blochSphere,
                new Separator(),
                histogramLabel,
                histogram.getRoot(),
                new Separator(),
                probabilityLabel,
                probabilityInfoBox
        );
    }

    private void applyStyles() {
        root.setStyle("-fx-background-color: #ecf0f1;");
        root.setPrefWidth(300);
    }

    public void updateState(QuantumState state) {
        Probability probOne = state.getProbabilityOfOne(new QubitIndex(0));
        SubScene updatedBlochSphere = blochSphereRenderer.render(probOne.getValue());

        root.getChildren().removeIf(node -> node instanceof SubScene);
        int blochIndex = root.getChildren().indexOf(
                root.getChildren().stream()
                        .filter(node -> node instanceof Label && ((Label) node).getText().contains("블로흐 구면"))
                        .findFirst()
                        .orElse(null)
        );
        if (blochIndex >= 0 && blochIndex + 1 < root.getChildren().size()) {
            root.getChildren().add(blochIndex + 1, updatedBlochSphere);
        }

        histogram.updateState(state);

        probabilityInfoBox.getChildren().clear();

        for (int i = 0; i < state.getQubitCount(); i++) {
            QubitIndex qubitIndex = new QubitIndex(i);
            Probability probOfOne = state.getProbabilityOfOne(qubitIndex);
            Probability probOfZero = new Probability(1.0 - probOfOne.getValue());

            VBox qubitInfo = new VBox(3);
            qubitInfo.setAlignment(Pos.CENTER_LEFT);

            Label qubitLabel = new Label(String.format("Qubit %d", i));
            qubitLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
            qubitLabel.setStyle("-fx-text-fill: #2c3e50;");

            Label probZeroLabel = new Label(String.format("|0⟩: %.1f%%", probOfZero.toPercentage()));
            probZeroLabel.setStyle("-fx-text-fill: #27ae60; -fx-font-size: 11px;");

            Label probOneLabel = new Label(String.format("|1⟩: %.1f%%", probOfOne.toPercentage()));
            probOneLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 11px;");

            qubitInfo.getChildren().addAll(qubitLabel, probZeroLabel, probOneLabel);

            probabilityInfoBox.getChildren().add(qubitInfo);

            if (i < state.getQubitCount() - 1) {
                probabilityInfoBox.getChildren().add(new Separator());
            }
        }
    }

    public VBox getRoot() {
        return root;
    }
}
