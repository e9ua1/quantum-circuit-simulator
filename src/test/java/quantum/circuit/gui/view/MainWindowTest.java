package quantum.circuit.gui.view;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MainWindowTest {

    @Test
    void MainWindow_클래스가_존재한다() {
        // given & when
        Class<?> clazz = MainWindow.class;

        // then
        assertThat(clazz).isNotNull();
    }

    @Test
    void MainWindow는_public_생성자를_가진다() throws Exception {
        // given & when
        var constructor = MainWindow.class.getDeclaredConstructor();

        // then
        assertThat(constructor).isNotNull();
        assertThat(java.lang.reflect.Modifier.isPublic(constructor.getModifiers())).isTrue();
    }

    @Test
    void MainWindow는_getRoot_메서드를_가진다() throws Exception {
        // given & when
        var method = MainWindow.class.getDeclaredMethod("getRoot");

        // then
        assertThat(method).isNotNull();
        assertThat(method.getReturnType().getSimpleName()).isEqualTo("BorderPane");
    }

    @Test
    void MainWindow는_getCircuitCanvasArea_메서드를_가진다() throws Exception {
        // given & when
        var method = MainWindow.class.getDeclaredMethod("getCircuitCanvasArea");

        // then
        assertThat(method).isNotNull();
        assertThat(method.getReturnType().getSimpleName()).isEqualTo("Pane");
    }

    @Test
    void MainWindow는_getStateInfoPanel_메서드를_가진다() throws Exception {
        // given & when
        var method = MainWindow.class.getDeclaredMethod("getStateInfoPanel");

        // then
        assertThat(method).isNotNull();
        assertThat(method.getReturnType().getSimpleName()).isEqualTo("Pane");
    }
}
