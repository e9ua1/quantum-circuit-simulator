package quantum.circuit.gui.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("FreeModeWindow 테스트")
class FreeModeWindowTest {

    @Test
    @DisplayName("FreeModeWindow 클래스가 존재한다")
    void FreeModeWindow_클래스가_존재한다() {
        // given & when & then
        assertThat(FreeModeWindow.class).isNotNull();
    }

    @Test
    @DisplayName("public 생성자를 가진다")
    void public_생성자를_가진다() throws Exception {
        // given & when
        var constructor = FreeModeWindow.class.getDeclaredConstructor();

        // then
        assertThat(constructor).isNotNull();
        assertThat(Modifier.isPublic(constructor.getModifiers())).isTrue();
    }

    @Test
    @DisplayName("getRoot 메서드가 BorderPane을 반환한다")
    void getRoot_메서드가_BorderPane을_반환한다() throws Exception {
        // given
        Class<?> clazz = FreeModeWindow.class;

        // when
        Method method = clazz.getDeclaredMethod("getRoot");

        // then
        assertThat(method).isNotNull();
        assertThat(method.getReturnType().getSimpleName()).isEqualTo("BorderPane");
    }

    @Test
    @DisplayName("setCircuitEditor 메서드가 존재한다")
    void setCircuitEditor_메서드가_존재한다() throws Exception {
        // given
        Class<?> clazz = FreeModeWindow.class;

        // when
        Method method = clazz.getDeclaredMethod("setCircuitEditor",
                quantum.circuit.gui.controller.CircuitEditor.class);

        // then
        assertThat(method).isNotNull();
        assertThat(method.getReturnType()).isEqualTo(void.class);
    }

    @Test
    @DisplayName("getMainWindow 메서드가 MainWindow를 반환한다")
    void getMainWindow_메서드가_MainWindow를_반환한다() throws Exception {
        // given
        Class<?> clazz = FreeModeWindow.class;

        // when
        Method method = clazz.getDeclaredMethod("getMainWindow");

        // then
        assertThat(method).isNotNull();
        assertThat(method.getReturnType().getSimpleName()).isEqualTo("MainWindow");
    }
}
