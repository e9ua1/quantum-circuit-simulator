package quantum.circuit.gui.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import quantum.circuit.domain.circuit.QuantumCircuit;
import quantum.circuit.gui.controller.CircuitEditor;
import quantum.circuit.gui.renderer.InteractiveCircuitCanvas;

/**
 * 자유 모드 윈도우
 * 게이트 추가/삭제, 언두/리두 지원
 */
public class FreeModeWindow {

    private static final String TITLE_TEXT = "⚛️ 자유 모드 - 회로 편집기";
    private static final double SPACING = 10.0;
    private static final double PADDING = 20.0;
    private static final double QUBIT_LINE_Y_SPACING = 80.0;
    private static final double GATE_X_SPACING = 100.0;
    private static final double LEFT_MARGIN = 50.0;
    private static final double TOP_MARGIN = 50.0;

    private final BorderPane root;
    private final GatePalette gatePalette;
    private final MainWindow mainWindow;
    private final VBox centerPane;
    private final Label instructionLabel;
    private final HBox toolBar;
    private final InteractiveCircuitCanvas interactiveCanvas;

    private Button undoButton;
    private Button redoButton;

    private CircuitEditor circuitEditor;
    private String currentDraggedGate;

    public FreeModeWindow() {
        this.root = new BorderPane();
        this.gatePalette = new GatePalette();
        this.mainWindow = new MainWindow();
        this.centerPane = new VBox(SPACING);
        this.instructionLabel = createInstructionLabel();
        this.toolBar = createToolBar();
        this.interactiveCanvas = new InteractiveCircuitCanvas();

        setupLayout();
        setupDragAndDrop();
        setupKeyboardShortcuts();
    }

    private void setupLayout() {
        root.setTop(createHeader());
        root.setLeft(gatePalette.getRoot());

        centerPane.setPadding(new Insets(PADDING));
        centerPane.getChildren().addAll(
                instructionLabel,
                toolBar,
                mainWindow.getRoot()
        );
        root.setCenter(centerPane);

        root.setStyle("-fx-background-color: #f5f6fa;");
    }

    private VBox createHeader() {
        Label titleLabel = new Label(TITLE_TEXT);
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
        titleLabel.setStyle("-fx-text-fill: white;");

        VBox header = new VBox(titleLabel);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(PADDING));
        header.setStyle("-fx-background-color: #2c3e50;");

        return header;
    }

    private Label createInstructionLabel() {
        Label label = new Label(
                "💡 사용법: " +
                        "좌측 게이트를 드래그하여 추가, " +
                        "게이트 클릭하여 삭제, " +
                        "Ctrl+Z/Y로 언두/리두"
        );
        label.setWrapText(true);
        label.setFont(Font.font("System", 14));
        label.setStyle(
                "-fx-background-color: #e8f4f8; " +
                        "-fx-padding: 10; " +
                        "-fx-background-radius: 5; " +
                        "-fx-text-fill: #2c3e50;"
        );
        return label;
    }

    private HBox createToolBar() {
        // 큐비트 수 조절
        Label qubitLabel = new Label("큐비트:");
        qubitLabel.setFont(Font.font("System", FontWeight.BOLD, 14));

        Spinner<Integer> qubitSpinner = new Spinner<>(2, 5, 3);
        qubitSpinner.setPrefWidth(70);
        qubitSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (circuitEditor != null) {
                circuitEditor.setQubitCount(newVal);
            }
        });

        // 언두 버튼
        undoButton = new Button("↶ 언두");
        undoButton.setStyle(
                "-fx-background-color: #95a5a6; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-cursor: hand;"
        );
        undoButton.setDisable(true);
        undoButton.setOnAction(e -> {
            if (circuitEditor != null) {
                circuitEditor.undo();
                updateUndoRedoButtons();
            }
        });

        // 리두 버튼
        redoButton = new Button("↷ 리두");
        redoButton.setStyle(
                "-fx-background-color: #95a5a6; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-cursor: hand;"
        );
        redoButton.setDisable(true);
        redoButton.setOnAction(e -> {
            if (circuitEditor != null) {
                circuitEditor.redo();
                updateUndoRedoButtons();
            }
        });

        // 초기화 버튼
        Button clearButton = new Button("🗑️ 초기화");
        clearButton.setStyle(
                "-fx-background-color: #e74c3c; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-cursor: hand;"
        );
        clearButton.setOnAction(e -> {
            if (circuitEditor != null) {
                circuitEditor.clearCircuit();
                updateUndoRedoButtons();
            }
        });

        HBox toolbar = new HBox(SPACING);
        toolbar.setAlignment(Pos.CENTER_LEFT);
        toolbar.setPadding(new Insets(SPACING, 0, SPACING, 0));
        toolbar.getChildren().addAll(
                qubitLabel, qubitSpinner,
                new Separator(javafx.geometry.Orientation.VERTICAL),
                undoButton, redoButton,
                new Separator(javafx.geometry.Orientation.VERTICAL),
                clearButton
        );

        return toolbar;
    }

    private void setupDragAndDrop() {
        gatePalette.setOnGateDragDetected((gateName, event) -> {
            currentDraggedGate = gateName;
        });

        ScrollPane canvasArea = mainWindow.getCircuitCanvasArea();

        canvasArea.setOnDragOver(event -> {
            if (event.getGestureSource() != canvasArea &&
                    event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });

        canvasArea.setOnDragDropped(event -> {
            boolean success = false;
            if (event.getDragboard().hasString()) {
                String gateName = event.getDragboard().getString();

                double x = event.getX();
                double y = event.getY();

                int qubitIndex = calculateQubitIndex(y);
                int stepIndex = calculateStepIndex(x);

                if (circuitEditor != null &&
                        qubitIndex >= 0 && qubitIndex < circuitEditor.getQubitCount() &&
                        stepIndex >= 0) {

                    circuitEditor.addGate(gateName, qubitIndex, stepIndex);
                    updateUndoRedoButtons();
                    success = true;
                }
            }

            event.setDropCompleted(success);
            event.consume();
        });

        // InteractiveCircuitCanvas에 게이트 클릭 핸들러 설정
        interactiveCanvas.setOnGateClicked((qubitIndex, stepIndex) -> {
            if (circuitEditor != null) {
                circuitEditor.removeGate(qubitIndex, stepIndex);
                updateUndoRedoButtons();
            }
        });
    }

    private void setupKeyboardShortcuts() {
        // Ctrl+Z: 언두
        KeyCombination undoCombo = new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN);
        root.setOnKeyPressed(event -> {
            if (undoCombo.match(event)) {
                if (circuitEditor != null && circuitEditor.canUndo()) {
                    circuitEditor.undo();
                    updateUndoRedoButtons();
                }
            }
        });

        // Ctrl+Y: 리두
        KeyCombination redoCombo = new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN);
        root.setOnKeyPressed(event -> {
            if (redoCombo.match(event)) {
                if (circuitEditor != null && circuitEditor.canRedo()) {
                    circuitEditor.redo();
                    updateUndoRedoButtons();
                }
            }
        });
    }

    private void updateUndoRedoButtons() {
        if (circuitEditor != null) {
            undoButton.setDisable(!circuitEditor.canUndo());
            redoButton.setDisable(!circuitEditor.canRedo());
        }
    }

    private int calculateQubitIndex(double y) {
        double relativeY = y - TOP_MARGIN;
        if (relativeY < 0) return 0;
        return (int) Math.round(relativeY / QUBIT_LINE_Y_SPACING);
    }

    private int calculateStepIndex(double x) {
        double relativeX = x - LEFT_MARGIN;
        if (relativeX < 0) return 0;
        return (int) Math.floor(relativeX / GATE_X_SPACING);
    }

    public void setCircuitEditor(CircuitEditor editor) {
        this.circuitEditor = editor;
        updateUndoRedoButtons();
    }

    public MainWindow getMainWindow() {
        return mainWindow;
    }

    public InteractiveCircuitCanvas getInteractiveCanvas() {
        return interactiveCanvas;
    }

    public BorderPane getRoot() {
        return root;
    }
}
