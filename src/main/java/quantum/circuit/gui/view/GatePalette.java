package quantum.circuit.gui.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.function.BiConsumer;

/**
 * 게이트 팔레트 컴포넌트
 * 좌측에 드래그 가능한 게이트 버튼들을 표시합니다.
 */
public class GatePalette {

    private static final double BUTTON_WIDTH = 80.0;
    private static final double BUTTON_HEIGHT = 60.0;
    private static final double SPACING = 10.0;
    private static final double PADDING = 15.0;

    private final VBox root;
    private BiConsumer<String, javafx.scene.input.MouseEvent> onGateDragDetected;

    public GatePalette() {
        this.root = new VBox(SPACING);
        setupLayout();
    }

    private void setupLayout() {
        Label titleLabel = new Label("게이트");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        titleLabel.setStyle("-fx-text-fill: #2c3e50;");

        Button hButton = createGateButton("H", "아다마르\n게이트");
        Button xButton = createGateButton("X", "파울리 X\n게이트");
        Button zButton = createGateButton("Z", "파울리 Z\n게이트");
        Button cnotButton = createGateButton("CNOT", "제어\nNOT");

        root.getChildren().addAll(
                titleLabel,
                hButton,
                xButton,
                zButton,
                cnotButton
        );

        root.setPadding(new Insets(PADDING));
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color: #ecf0f1;");
    }

    private Button createGateButton(String gateName, String description) {
        Button button = new Button(gateName);
        button.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        button.setStyle(
                "-fx-background-color: #3498db; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 16px; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 5; " +
                "-fx-cursor: hand;"
        );

        // 툴팁 설정
        javafx.scene.control.Tooltip tooltip = new javafx.scene.control.Tooltip(description);
        button.setTooltip(tooltip);

        // 드래그 시작
        button.setOnDragDetected(event -> {
            Dragboard dragboard = button.startDragAndDrop(TransferMode.COPY);
            ClipboardContent content = new ClipboardContent();
            content.putString(gateName);
            dragboard.setContent(content);

            // 외부 핸들러 호출
            if (onGateDragDetected != null) {
                onGateDragDetected.accept(gateName, event);
            }

            event.consume();
        });

        // 호버 효과
        button.setOnMouseEntered(e ->
                button.setStyle(
                        "-fx-background-color: #2980b9; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 16px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 5; " +
                        "-fx-cursor: hand;"
                )
        );

        button.setOnMouseExited(e ->
                button.setStyle(
                        "-fx-background-color: #3498db; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 16px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 5; " +
                        "-fx-cursor: hand;"
                )
        );

        return button;
    }

    /**
     * 게이트 드래그 시작 이벤트 핸들러 설정
     *
     * @param handler 게이트 이름과 마우스 이벤트를 받는 핸들러
     */
    public void setOnGateDragDetected(BiConsumer<String, javafx.scene.input.MouseEvent> handler) {
        this.onGateDragDetected = handler;
    }

    public VBox getRoot() {
        return root;
    }
}
