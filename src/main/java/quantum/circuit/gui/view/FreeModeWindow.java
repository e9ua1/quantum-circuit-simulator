package quantum.circuit.gui.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import quantum.circuit.gui.controller.CircuitEditor;

/**
 * 자유 모드 윈도우
 * 게이트 팔레트 + 정확한 드래그 앤 드롭 회로 편집
 */
public class FreeModeWindow {

    private static final String TITLE_TEXT = "⚛️ 자유 모드 - 회로 편집기";
    private static final double SPACING = 10.0;
    private static final double PADDING = 20.0;

    // CircuitCanvas 좌표 시스템 (CircuitCanvas와 동일하게 유지)
    private static final double QUBIT_LINE_Y_SPACING = 80.0;
    private static final double GATE_X_SPACING = 100.0;
    private static final double LEFT_MARGIN = 50.0;
    private static final double TOP_MARGIN = 50.0;

    private final BorderPane root;
    private final GatePalette gatePalette;
    private final MainWindow mainWindow;
    private final VBox centerPane;
    private final Label instructionLabel;
    private final HBox controlPanel;
    private final Pane dropOverlay;

    private CircuitEditor circuitEditor;
    private String currentDraggedGate;

    public FreeModeWindow() {
        this.root = new BorderPane();
        this.gatePalette = new GatePalette();
        this.mainWindow = new MainWindow();
        this.centerPane = new VBox(SPACING);
        this.instructionLabel = createInstructionLabel();
        this.controlPanel = createControlPanel();
        this.dropOverlay = new Pane();

        setupLayout();
        setupDragAndDrop();
    }

    private void setupLayout() {
        root.setTop(createHeader());
        root.setLeft(gatePalette.getRoot());

        // 중앙 영역
        centerPane.setPadding(new Insets(PADDING));
        centerPane.getChildren().addAll(
                instructionLabel,
                controlPanel,
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
                "💡 사용법: 좌측의 게이트를 드래그하여 회로에 추가하세요. " +
                        "큐비트 라인(Q0, Q1, ...)과 단계(세로 그리드)를 선택할 수 있습니다."
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

    private HBox createControlPanel() {
        Label qubitLabel = new Label("큐비트 수:");
        qubitLabel.setFont(Font.font("System", FontWeight.BOLD, 14));

        Spinner<Integer> qubitSpinner = new Spinner<>(2, 5, 3);
        qubitSpinner.setPrefWidth(80);
        qubitSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (circuitEditor != null) {
                circuitEditor.setQubitCount(newVal);
            }
        });

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
            }
        });

        HBox panel = new HBox(SPACING);
        panel.setAlignment(Pos.CENTER_LEFT);
        panel.setPadding(new Insets(SPACING, 0, SPACING, 0));
        panel.getChildren().addAll(qubitLabel, qubitSpinner, clearButton);

        return panel;
    }

    private void setupDragAndDrop() {
        // 게이트 팔레트의 드래그 이벤트 처리
        gatePalette.setOnGateDragDetected((gateName, event) -> {
            currentDraggedGate = gateName;
        });

        // MainWindow의 circuitCanvasArea에 드롭 영역 설정
        ScrollPane canvasArea = mainWindow.getCircuitCanvasArea();

        canvasArea.setOnDragOver(event -> {
            if (event.getGestureSource() != canvasArea &&
                    event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.COPY);

                // 드롭 위치 시각적 표시 (선택사항)
                showDropIndicator(event.getX(), event.getY());
            }
            event.consume();
        });

        canvasArea.setOnDragExited(event -> {
            hideDropIndicator();
            event.consume();
        });

        canvasArea.setOnDragDropped(event -> {
            boolean success = false;
            if (event.getDragboard().hasString()) {
                String gateName = event.getDragboard().getString();

                // 드롭 위치로부터 큐비트와 단계 계산
                double x = event.getX();
                double y = event.getY();

                int qubitIndex = calculateQubitIndex(y);
                int stepIndex = calculateStepIndex(x);

                // 유효성 검사
                if (circuitEditor != null &&
                        qubitIndex >= 0 && qubitIndex < circuitEditor.getQubitCount() &&
                        stepIndex >= 0) {

                    circuitEditor.addGate(gateName, qubitIndex, stepIndex);
                    success = true;

                    System.out.println(String.format(
                            "게이트 추가: %s → Q%d, Step %d (위치: %.1f, %.1f)",
                            gateName, qubitIndex, stepIndex, x, y
                    ));
                }
            }

            hideDropIndicator();
            event.setDropCompleted(success);
            event.consume();
        });
    }

    /**
     * Y 좌표로부터 큐비트 인덱스를 계산합니다.
     *
     * @param y 마우스 Y 좌표
     * @return 큐비트 인덱스 (0부터 시작)
     */
    private int calculateQubitIndex(double y) {
        // CircuitCanvas의 좌표 시스템:
        // qubitY = TOP_MARGIN + qubit * QUBIT_LINE_Y_SPACING

        // 역계산:
        // qubit = (y - TOP_MARGIN) / QUBIT_LINE_Y_SPACING

        double relativeY = y - TOP_MARGIN;
        if (relativeY < 0) {
            return 0;  // 상단 여백
        }

        // 가장 가까운 큐비트 라인으로 스냅
        int qubitIndex = (int) Math.round(relativeY / QUBIT_LINE_Y_SPACING);

        return qubitIndex;
    }

    /**
     * X 좌표로부터 단계 인덱스를 계산합니다.
     *
     * @param x 마우스 X 좌표
     * @return 단계 인덱스 (0부터 시작)
     */
    private int calculateStepIndex(double x) {
        // CircuitCanvas의 좌표 시스템:
        // stepX = LEFT_MARGIN + (step + 1) * GATE_X_SPACING

        // 역계산:
        // step = (x - LEFT_MARGIN) / GATE_X_SPACING - 1

        double relativeX = x - LEFT_MARGIN;
        if (relativeX < 0) {
            return 0;  // 좌측 여백
        }

        // 게이트는 GATE_X_SPACING 간격으로 배치
        // step 0: x = LEFT_MARGIN + GATE_X_SPACING
        // step 1: x = LEFT_MARGIN + 2 * GATE_X_SPACING

        int stepIndex = (int) Math.floor(relativeX / GATE_X_SPACING);

        return stepIndex;
    }

    /**
     * 드롭 위치에 시각적 인디케이터를 표시합니다.
     *
     * @param x 마우스 X 좌표
     * @param y 마우스 Y 좌표
     */
    private void showDropIndicator(double x, double y) {
        // 선택사항: 드롭 위치에 반투명 사각형 표시
        // 현재는 구현하지 않음 (CircuitCanvas 위에 오버레이 필요)
    }

    /**
     * 드롭 인디케이터를 숨깁니다.
     */
    private void hideDropIndicator() {
        // 선택사항
    }

    public void setCircuitEditor(CircuitEditor editor) {
        this.circuitEditor = editor;
    }

    public MainWindow getMainWindow() {
        return mainWindow;
    }

    public BorderPane getRoot() {
        return root;
    }
}
