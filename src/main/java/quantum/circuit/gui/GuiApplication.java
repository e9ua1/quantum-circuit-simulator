package quantum.circuit.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import quantum.circuit.gui.view.MainWindow;

public class GuiApplication extends Application {

    private static final String WINDOW_TITLE = "Quantum Circuit Simulator";
    private static final double MIN_WIDTH = 1000.0;
    private static final double MIN_HEIGHT = 700.0;

    @Override
    public void start(Stage primaryStage) {
        MainWindow mainWindow = new MainWindow();
        Scene scene = new Scene(mainWindow.getRoot(), MIN_WIDTH, MIN_HEIGHT);

        primaryStage.setTitle(WINDOW_TITLE);
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(MIN_WIDTH);
        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
