package quantum.circuit.gui.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import quantum.circuit.algorithm.AlgorithmFactory;
import quantum.circuit.algorithm.AlgorithmType;

import java.util.Optional;

/**
 * 알고리즘 선택 다이얼로그
 * 사용 가능한 양자 알고리즘 목록을 표시하고 선택할 수 있습니다.
 */
public class AlgorithmSelectionDialog {

    private static final String DIALOG_TITLE = "알고리즘 선택";
    private static final double SPACING = 10.0;
    private static final double PADDING = 20.0;

    private final Dialog<AlgorithmType> dialog;
    private final ToggleGroup algorithmGroup;

    public AlgorithmSelectionDialog() {
        this.dialog = new Dialog<>();
        this.algorithmGroup = new ToggleGroup();

        setupDialog();
    }

    private void setupDialog() {
        dialog.setTitle(DIALOG_TITLE);
        dialog.setHeaderText("사용할 양자 알고리즘을 선택하세요");

        // 다이얼로그 콘텐츠
        VBox content = createContent();
        dialog.getDialogPane().setContent(content);

        // 버튼 추가
        ButtonType confirmButtonType = new ButtonType("선택", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("취소", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, cancelButtonType);

        // 결과 변환
        dialog.setResultConverter(buttonType -> {
            if (buttonType == confirmButtonType) {
                RadioButton selected = (RadioButton) algorithmGroup.getSelectedToggle();
                if (selected != null) {
                    return (AlgorithmType) selected.getUserData();
                }
            }
            return null;
        });
    }

    private VBox createContent() {
        VBox content = new VBox(SPACING);
        content.setPadding(new Insets(PADDING));
        content.setAlignment(Pos.CENTER_LEFT);
        content.setPrefWidth(400);

        // 알고리즘 목록
        AlgorithmFactory factory = new AlgorithmFactory();
        for (AlgorithmType type : factory.getAlgorithmTypes()) {
            RadioButton radioButton = createAlgorithmRadioButton(type);
            content.getChildren().add(radioButton);
        }

        // 첫 번째 항목 기본 선택
        if (!content.getChildren().isEmpty()) {
            ((RadioButton) content.getChildren().get(0)).setSelected(true);
        }

        return content;
    }

    private RadioButton createAlgorithmRadioButton(AlgorithmType type) {
        RadioButton radioButton = new RadioButton();
        radioButton.setToggleGroup(algorithmGroup);
        radioButton.setUserData(type);

        // 레이블 구성
        VBox labelBox = new VBox(3);
        labelBox.setAlignment(Pos.CENTER_LEFT);

        Label nameLabel = new Label(type.getDisplayName());
        nameLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        nameLabel.setStyle("-fx-text-fill: #2c3e50;");

        Label descLabel = new Label(type.getDescription());
        descLabel.setWrapText(true);
        descLabel.setMaxWidth(350);
        descLabel.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 11px;");

        Label qubitLabel = new Label(String.format("필요 큐비트: %d", type.getRequiredQubits()));
        qubitLabel.setStyle("-fx-text-fill: #3498db; -fx-font-size: 10px;");

        labelBox.getChildren().addAll(nameLabel, descLabel, qubitLabel);
        radioButton.setGraphic(labelBox);

        return radioButton;
    }

    /**
     * 다이얼로그를 표시하고 선택된 알고리즘을 반환합니다.
     *
     * @return 선택된 알고리즘 (취소 시 empty)
     */
    public Optional<AlgorithmType> show() {
        return dialog.showAndWait();
    }

    /**
     * 선택된 알고리즘을 반환합니다 (다이얼로그를 표시하지 않음).
     *
     * @return 선택된 알고리즘 (없으면 empty)
     */
    public Optional<AlgorithmType> getSelectedAlgorithm() {
        RadioButton selected = (RadioButton) algorithmGroup.getSelectedToggle();
        if (selected != null) {
            return Optional.of((AlgorithmType) selected.getUserData());
        }
        return Optional.empty();
    }
}
