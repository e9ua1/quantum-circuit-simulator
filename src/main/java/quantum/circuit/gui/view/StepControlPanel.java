package quantum.circuit.gui.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * 단계별 실행 제어 패널
 * ⏮ ◀ ▶ ⏭ 버튼과 현재 단계 표시
 */
public class StepControlPanel {

    private static final double BUTTON_SIZE = 40.0;
    private static final double SPACING = 10.0;
    private static final double PADDING = 10.0;

    private final HBox root;
    private final Button resetButton;
    private final Button previousButton;
    private final Button nextButton;
    private final Button runToEndButton;
    private final Label stepLabel;

    public StepControlPanel() {
        this.root = new HBox(SPACING);
        this.resetButton = createControlButton("⏮");
        this.previousButton = createControlButton("◀");
        this.nextButton = createControlButton("▶");
        this.runToEndButton = createControlButton("⏭");
        this.stepLabel = createStepLabel();

        setupLayout();
        applyStyles();
    }

    private void setupLayout() {
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(PADDING));

        root.getChildren().addAll(
                resetButton,
                previousButton,
                stepLabel,
                nextButton,
                runToEndButton
        );
    }

    private Button createControlButton(String symbol) {
        Button button = new Button(symbol);
        button.setPrefSize(BUTTON_SIZE, BUTTON_SIZE);
        button.setStyle(
                "-fx-background-color: #3498db; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 18px; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 5; " +
                "-fx-cursor: hand;"
        );

        button.setOnMouseEntered(e ->
                button.setStyle(
                        "-fx-background-color: #2980b9; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 18px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 5; " +
                        "-fx-cursor: hand;"
                )
        );

        button.setOnMouseExited(e ->
                button.setStyle(
                        "-fx-background-color: #3498db; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 18px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 5; " +
                        "-fx-cursor: hand;"
                )
        );

        return button;
    }

    private Label createStepLabel() {
        Label label = new Label("Step 0 / 0");
        label.setFont(Font.font("System", FontWeight.BOLD, 16));
        label.setStyle("-fx-text-fill: #2c3e50;");
        label.setMinWidth(120);
        label.setAlignment(Pos.CENTER);
        return label;
    }

    private void applyStyles() {
        root.setStyle("-fx-background-color: #ecf0f1; -fx-background-radius: 10;");
    }

    /**
     * 다음 단계 버튼 클릭 이벤트 설정
     */
    public void setOnNextStep(Runnable action) {
        nextButton.setOnAction(e -> action.run());
    }

    /**
     * 이전 단계 버튼 클릭 이벤트 설정
     */
    public void setOnPreviousStep(Runnable action) {
        previousButton.setOnAction(e -> action.run());
    }

    /**
     * 처음으로 버튼 클릭 이벤트 설정
     */
    public void setOnReset(Runnable action) {
        resetButton.setOnAction(e -> action.run());
    }

    /**
     * 끝까지 버튼 클릭 이벤트 설정
     */
    public void setOnRunToEnd(Runnable action) {
        runToEndButton.setOnAction(e -> action.run());
    }

    /**
     * 현재 단계 정보 업데이트
     *
     * @param currentStep 현재 단계 (0부터 시작)
     * @param totalSteps 전체 단계 수
     */
    public void updateStepInfo(int currentStep, int totalSteps) {
        stepLabel.setText(String.format("Step %d / %d", currentStep, totalSteps));

        // 버튼 활성화/비활성화
        resetButton.setDisable(currentStep == 0);
        previousButton.setDisable(currentStep == 0);
        nextButton.setDisable(currentStep >= totalSteps);
        runToEndButton.setDisable(currentStep >= totalSteps);
    }

    public HBox getRoot() {
        return root;
    }
}
