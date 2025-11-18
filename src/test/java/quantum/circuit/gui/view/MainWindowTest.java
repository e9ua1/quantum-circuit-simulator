package quantum.circuit.gui.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("MainWindow 테스트")
class MainWindowTest {

    @Test
    @DisplayName("MainWindow 클래스가 존재한다")
    void MainWindow_클래스가_존재한다() {
        // given & when & then
        assertThat(MainWindow.class).isNotNull();
    }

    @Test
    @DisplayName("public 생성자를 가진다")
    void public_생성자를_가진다() throws Exception {
        // given & when
        var constructor = MainWindow.class.getDeclaredConstructor();

        // then
        assertThat(constructor).isNotNull();
        assertThat(Modifier.isPublic(constructor.getModifiers())).isTrue();
    }

    @Test
    @DisplayName("getRoot 메서드가 BorderPane을 반환한다")
    void getRoot_메서드가_BorderPane을_반환한다() throws Exception {
        // given
        Class<?> clazz = MainWindow.class;

        // when
        Method method = clazz.getDeclaredMethod("getRoot");

        // then
        assertThat(method).isNotNull();
        assertThat(method.getReturnType().getSimpleName()).isEqualTo("BorderPane");
    }

    @Test
    @DisplayName("getCircuitCanvasArea 메서드가 ScrollPane을 반환한다")
    void getCircuitCanvasArea_메서드가_ScrollPane을_반환한다() throws Exception {
        // given
        Class<?> clazz = MainWindow.class;

        // when
        Method method = clazz.getDeclaredMethod("getCircuitCanvasArea");

        // then
        assertThat(method).isNotNull();
        assertThat(method.getReturnType().getSimpleName()).isEqualTo("ScrollPane");
    }

    @Test
    @DisplayName("getStateInfoPanel 메서드가 StateInfoPanel을 반환한다")
    void getStateInfoPanel_메서드가_StateInfoPanel을_반환한다() throws Exception {
        // given
        Class<?> clazz = MainWindow.class;

        // when
        Method method = clazz.getDeclaredMethod("getStateInfoPanel");

        // then
        assertThat(method).isNotNull();
        assertThat(method.getReturnType().getSimpleName()).isEqualTo("StateInfoPanel");
    }

    @Test
    @DisplayName("setCircuit 메서드가 존재한다")
    void setCircuit_메서드가_존재한다() throws Exception {
        // given
        Class<?> clazz = MainWindow.class;

        // when
        Method method = clazz.getDeclaredMethod("setCircuit", quantum.circuit.domain.circuit.QuantumCircuit.class);

        // then
        assertThat(method).isNotNull();
        assertThat(method.getReturnType()).isEqualTo(void.class);
    }

    @Test
    @DisplayName("setController 메서드가 존재한다")
    void setController_메서드가_존재한다() throws Exception {
        // given
        Class<?> clazz = MainWindow.class;

        // when
        Method method = clazz.getDeclaredMethod("setController", quantum.circuit.gui.controller.CircuitController.class);

        // then
        assertThat(method).isNotNull();
        assertThat(method.getReturnType()).isEqualTo(void.class);
    }
}
