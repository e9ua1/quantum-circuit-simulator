package quantum.circuit.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import quantum.circuit.algorithm.AlgorithmFactory;
import quantum.circuit.algorithm.QuantumAlgorithm;
import quantum.circuit.domain.circuit.QuantumCircuit;
import quantum.circuit.gui.controller.CircuitController;
import quantum.circuit.gui.controller.StepController;
import quantum.circuit.gui.view.MainWindow;

public class GuiApplication extends Application {

    private static final String WINDOW_TITLE = "Quantum Circuit Simulator";
    private static final double MIN_WIDTH = 1000.0;
    private static final double MIN_HEIGHT = 700.0;

    @Override
    public void start(Stage primaryStage) {
        MainWindow mainWindow = new MainWindow();

        // CircuitController 생성 및 설정
        CircuitController circuitController = new CircuitController(mainWindow);
        mainWindow.setCircuitController(circuitController);

        // StepController 생성 및 설정
        StepController stepController = new StepController(mainWindow);
        mainWindow.setStepController(stepController);

        // 샘플 회로 표시 (Bell State)
        showSampleCircuit(mainWindow);

        Scene scene = new Scene(mainWindow.getRoot(), MIN_WIDTH, MIN_HEIGHT);

        primaryStage.setTitle(WINDOW_TITLE);
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(MIN_WIDTH);
        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.show();
    }

    private void showSampleCircuit(MainWindow mainWindow) {
        AlgorithmFactory factory = new AlgorithmFactory();
        QuantumAlgorithm algorithm = factory.create("BELL_STATE");
        QuantumCircuit circuit = algorithm.build(2);
        mainWindow.setCircuit(circuit);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
