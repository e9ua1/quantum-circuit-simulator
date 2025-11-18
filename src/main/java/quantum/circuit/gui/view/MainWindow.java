package quantum.circuit.gui.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MainWindow {

    private static final String TITLE_TEXT = "⚛️ Quantum Circuit Simulator";
    private static final double BUTTON_WIDTH = 200.0;
    private static final double BUTTON_HEIGHT = 50.0;
    private static final double SPACING = 15.0;
    private static final double PADDING = 20.0;

    private final BorderPane root;
    private final Pane circuitCanvasArea;
    private final Pane stateInfoPanel;

    public MainWindow() {
        this.root = new BorderPane();
        this.circuitCanvasArea = createCircuitCanvasArea();
        this.stateInfoPanel = createStateInfoPanel();

        setupLayout();
        applyStyles();
    }

    private void setupLayout() {
        root.setTop(createHeader());
        root.setLeft(createModeSelectionPanel());
        root.setCenter(circuitCanvasArea);
        root.setRight(stateInfoPanel);
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

    private Pane createCircuitCanvasArea() {
        VBox canvasArea = new VBox();
        canvasArea.setId("circuitCanvasArea");
        canvasArea.setAlignment(Pos.CENTER);
        canvasArea.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7;");

        Label placeholder = new Label("양자 회로가 여기에 표시됩니다");
        placeholder.setFont(Font.font("System", 16));
        placeholder.setStyle("-fx-text-fill: #95a5a6;");

        canvasArea.getChildren().add(placeholder);
        return canvasArea;
    }

    private Pane createStateInfoPanel() {
        VBox statePanel = new VBox(SPACING);
        statePanel.setId("stateInfoPanel");
        statePanel.setPadding(new Insets(PADDING));
        statePanel.setStyle("-fx-background-color: #ecf0f1;");
        statePanel.setPrefWidth(300);

        Label titleLabel = new Label("양자 상태 정보");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        titleLabel.setStyle("-fx-text-fill: #2c3e50;");

        Label placeholder = new Label("상태 정보가 여기에 표시됩니다");
        placeholder.setFont(Font.font("System", 14));
        placeholder.setStyle("-fx-text-fill: #7f8c8d;");

        statePanel.getChildren().addAll(titleLabel, placeholder);
        return statePanel;
    }

    private void applyStyles() {
        root.setStyle("-fx-background-color: #f5f6fa;");
    }

    public BorderPane getRoot() {
        return root;
    }

    public Pane getCircuitCanvasArea() {
        return circuitCanvasArea;
    }

    public Pane getStateInfoPanel() {
        return stateInfoPanel;
    }
}
