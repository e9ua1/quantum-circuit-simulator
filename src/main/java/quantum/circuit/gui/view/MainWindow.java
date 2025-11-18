package quantum.circuit.gui.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import quantum.circuit.algorithm.AlgorithmType;
import quantum.circuit.domain.circuit.QuantumCircuit;
import quantum.circuit.domain.state.QuantumState;
import quantum.circuit.gui.controller.CircuitController;
import quantum.circuit.gui.controller.CircuitEditor;
import quantum.circuit.gui.controller.StepController;
import quantum.circuit.gui.renderer.CircuitCanvas;

import java.util.Optional;

public class MainWindow {

    private static final String TITLE_TEXT = "⚛️ Quantum Circuit Simulator";
    private static final double BUTTON_WIDTH = 200.0;
    private static final double BUTTON_HEIGHT = 50.0;
    private static final double SPACING = 15.0;
    private static final double PADDING = 20.0;

    private final BorderPane root;
    private final BorderPane centerPane;
    private final ScrollPane circuitCanvasArea;
    private final StepControlPanel stepControlPanel;
    private final StateInfoPanel stateInfoPanel;
    private final CircuitCanvas renderer;

    private CircuitController circuitController;
    private StepController stepController;
    private QuantumCircuit circuit;

    public MainWindow() {
        this.root = new BorderPane();
        this.centerPane = new BorderPane();
        this.circuitCanvasArea = createCircuitCanvasArea();
        this.stepControlPanel = new StepControlPanel();
        this.stateInfoPanel = new StateInfoPanel();
        this.renderer = new CircuitCanvas();

        setupLayout();
        applyStyles();
        setupStepControl();
    }

    private void setupLayout() {
        root.setTop(createHeader());
        root.setLeft(createModeSelectionPanel());

        centerPane.setCenter(circuitCanvasArea);
        centerPane.setBottom(stepControlPanel.getRoot());
        root.setCenter(centerPane);

        root.setRight(stateInfoPanel.getRoot());
    }

    private void setupStepControl() {
        stepControlPanel.setOnNextStep(() -> {
            if (stepController != null) {
                stepController.nextStep();
            }
        });

        stepControlPanel.setOnPreviousStep(() -> {
            if (stepController != null) {
                stepController.previousStep();
            }
        });

        stepControlPanel.setOnReset(() -> {
            if (stepController != null) {
                stepController.reset();
            }
        });

        stepControlPanel.setOnRunToEnd(() -> {
            if (stepController != null) {
                stepController.runToEnd();
            }
        });
    }

    private VBox createHeader() {
        Label titleLabel = new Label(TITLE_TEXT);
        titleLabel.setId("titleLabel");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 28));

        VBox header = new VBox(titleLabel);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(PADDING));
        header.setStyle("-fx-background-color: #2c3e50; -fx-text-fill: white;");
        titleLabel.setStyle("-fx-text-fill: white;");

        return header;
    }

    private VBox createModeSelectionPanel() {
        Label modeLabel = new Label("모드 선택");
        modeLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        modeLabel.setStyle("-fx-text-fill: #2c3e50;");

        Button freeModeButton = createModeButton("자유 모드", "freeModeButton");
        Button algorithmModeButton = createModeButton("알고리즘 라이브러리", "algorithmModeButton");
        Button optimizationModeButton = createModeButton("최적화 모드", "optimizationModeButton");
        Button benchmarkModeButton = createModeButton("벤치마크 모드", "benchmarkModeButton");

        freeModeButton.setOnAction(e -> handleFreeMode());
        algorithmModeButton.setOnAction(e -> handleAlgorithmMode());
        optimizationModeButton.setOnAction(e -> handleOptimizationMode());
        benchmarkModeButton.setOnAction(e -> handleBenchmarkMode());

        VBox modePanel = new VBox(SPACING);
        modePanel.getChildren().addAll(
                modeLabel,
                freeModeButton,
                algorithmModeButton,
                optimizationModeButton,
                benchmarkModeButton
        );
        modePanel.setPadding(new Insets(PADDING));
        modePanel.setAlignment(Pos.TOP_CENTER);
        modePanel.setStyle("-fx-background-color: #ecf0f1;");

        return modePanel;
    }

    private Button createModeButton(String text, String id) {
        Button button = new Button(text);
        button.setId(id);
        button.setPrefWidth(BUTTON_WIDTH);
        button.setPrefHeight(BUTTON_HEIGHT);
        button.setStyle(
                "-fx-background-color: #3498db; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-background-radius: 5; " +
                        "-fx-cursor: hand;"
        );

        button.setOnMouseEntered(e ->
                button.setStyle(
                        "-fx-background-color: #2980b9; " +
                                "-fx-text-fill: white; " +
                                "-fx-font-size: 14px; " +
                                "-fx-background-radius: 5; " +
                                "-fx-cursor: hand;"
                )
        );
        button.setOnMouseExited(e ->
                button.setStyle(
                        "-fx-background-color: #3498db; " +
                                "-fx-text-fill: white; " +
                                "-fx-font-size: 14px; " +
                                "-fx-background-radius: 5; " +
                                "-fx-cursor: hand;"
                )
        );

        return button;
    }

    private ScrollPane createCircuitCanvasArea() {
        VBox placeholder = new VBox();
        placeholder.setAlignment(Pos.CENTER);
        placeholder.setStyle("-fx-background-color: white;");

        Label placeholderLabel = new Label("양자 회로가 여기에 표시됩니다");
        placeholderLabel.setFont(Font.font("System", 16));
        placeholderLabel.setStyle("-fx-text-fill: #95a5a6;");

        placeholder.getChildren().add(placeholderLabel);

        ScrollPane scrollPane = new ScrollPane(placeholder);
        scrollPane.setId("circuitCanvasArea");
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7;");

        return scrollPane;
    }

    private void applyStyles() {
        root.setStyle("-fx-background-color: #f5f6fa;");
    }

    private void handleFreeMode() {
        // 새 창에서 자유 모드 열기
        Stage freeModeStage = new Stage();
        freeModeStage.setTitle("자유 모드 - 회로 편집기");

        FreeModeWindow freeModeWindow = new FreeModeWindow();
        CircuitEditor circuitEditor = new CircuitEditor(freeModeWindow.getMainWindow());
        freeModeWindow.setCircuitEditor(circuitEditor);

        Scene scene = new Scene(freeModeWindow.getRoot(), 1200, 800);
        freeModeStage.setScene(scene);
        freeModeStage.show();
    }

    private void handleAlgorithmMode() {
        if (circuitController == null) {
            showErrorAlert("컨트롤러가 설정되지 않았습니다.");
            return;
        }

        AlgorithmSelectionDialog dialog = new AlgorithmSelectionDialog();
        Optional<AlgorithmType> selected = dialog.show();

        selected.ifPresent(algorithmType -> {
            circuitController.loadAlgorithm(algorithmType);
        });
    }

    private void handleOptimizationMode() {
        showNotImplementedAlert("최적화 모드");
    }

    private void handleBenchmarkMode() {
        showNotImplementedAlert("벤치마크 모드");
    }

    private void showNotImplementedAlert(String modeName) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("준비 중");
        alert.setHeaderText(modeName + " 준비 중");
        alert.setContentText("이 기능은 아직 구현되지 않았습니다.");
        alert.showAndWait();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("오류");
        alert.setHeaderText("오류 발생");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setCircuit(QuantumCircuit circuit) {
        this.circuit = circuit;

        Pane circuitPane = renderer.render(circuit);
        circuitCanvasArea.setContent(circuitPane);

        if (stepController != null) {
            stepController.loadCircuit(circuit);
        } else {
            QuantumState state = circuit.execute();
            stateInfoPanel.updateState(state);
        }
    }

    public void clearCircuit() {
        this.circuit = null;

        // 플레이스홀더 복원
        VBox placeholder = new VBox();
        placeholder.setAlignment(Pos.CENTER);
        placeholder.setStyle("-fx-background-color: white;");

        Label placeholderLabel = new Label("게이트를 드래그하여 회로를 만드세요");
        placeholderLabel.setFont(Font.font("System", 16));
        placeholderLabel.setStyle("-fx-text-fill: #95a5a6;");

        placeholder.getChildren().add(placeholderLabel);
        circuitCanvasArea.setContent(placeholder);
    }

    public void updateStateOnly(QuantumState state, int currentStep) {
        if (circuit != null) {
            Pane circuitPane = renderer.render(circuit, currentStep);
            circuitCanvasArea.setContent(circuitPane);
        }

        stateInfoPanel.updateState(state);

        if (stepController != null) {
            stepControlPanel.updateStepInfo(currentStep, stepController.getTotalSteps());
        }
    }

    public void setCircuitController(CircuitController controller) {
        this.circuitController = controller;
    }

    public void setStepController(StepController controller) {
        this.stepController = controller;
    }

    public BorderPane getRoot() {
        return root;
    }

    public ScrollPane getCircuitCanvasArea() {
        return circuitCanvasArea;
    }

    public StateInfoPanel getStateInfoPanel() {
        return stateInfoPanel;
    }
}
