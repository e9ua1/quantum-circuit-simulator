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
    @DisplayName("updateStateOnly 메서드가 존재한다")
    void updateStateOnly_메서드가_존재한다() throws Exception {
        // given
        Class<?> clazz = MainWindow.class;

        // when
        Method method = clazz.getDeclaredMethod("updateStateOnly", quantum.circuit.domain.state.QuantumState.class, int.class);

        // then
        assertThat(method).isNotNull();
        assertThat(method.getReturnType()).isEqualTo(void.class);
    }

    @Test
    @DisplayName("setCircuitController 메서드가 존재한다")
    void setCircuitController_메서드가_존재한다() throws Exception {
        // given
        Class<?> clazz = MainWindow.class;

        // when
        Method method = clazz.getDeclaredMethod("setCircuitController", quantum.circuit.gui.controller.CircuitController.class);

        // then
        assertThat(method).isNotNull();
        assertThat(method.getReturnType()).isEqualTo(void.class);
    }

    @Test
    @DisplayName("setStepController 메서드가 존재한다")
    void setStepController_메서드가_존재한다() throws Exception {
        // given
        Class<?> clazz = MainWindow.class;

        // when
        Method method = clazz.getDeclaredMethod("setStepController", quantum.circuit.gui.controller.StepController.class);

        // then
        assertThat(method).isNotNull();
        assertThat(method.getReturnType()).isEqualTo(void.class);
    }
}
