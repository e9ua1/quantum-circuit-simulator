package quantum.circuit.gui.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CircuitEditor 테스트")
class CircuitEditorTest {

    @Test
    @DisplayName("CircuitEditor 클래스가 존재한다")
    void CircuitEditor_클래스가_존재한다() {
        // given & when & then
        assertThat(CircuitEditor.class).isNotNull();
    }

    @Test
    @DisplayName("public 생성자를 가진다")
    void public_생성자를_가진다() throws Exception {
        // given & when
        var constructor = CircuitEditor.class.getDeclaredConstructor(
                quantum.circuit.gui.view.MainWindow.class
        );

        // then
        assertThat(constructor).isNotNull();
        assertThat(Modifier.isPublic(constructor.getModifiers())).isTrue();
    }

    @Test
    @DisplayName("addGate 메서드가 존재한다")
    void addGate_메서드가_존재한다() throws Exception {
        // given
        Class<?> clazz = CircuitEditor.class;

        // when
        Method method = clazz.getDeclaredMethod("addGate", 
                String.class, 
                int.class, 
                int.class);

        // then
        assertThat(method).isNotNull();
        assertThat(method.getReturnType()).isEqualTo(void.class);
    }

    @Test
    @DisplayName("clearCircuit 메서드가 존재한다")
    void clearCircuit_메서드가_존재한다() throws Exception {
        // given
        Class<?> clazz = CircuitEditor.class;

        // when
        Method method = clazz.getDeclaredMethod("clearCircuit");

        // then
        assertThat(method).isNotNull();
        assertThat(method.getReturnType()).isEqualTo(void.class);
    }

    @Test
    @DisplayName("setQubitCount 메서드가 존재한다")
    void setQubitCount_메서드가_존재한다() throws Exception {
        // given
        Class<?> clazz = CircuitEditor.class;

        // when
        Method method = clazz.getDeclaredMethod("setQubitCount", int.class);

        // then
        assertThat(method).isNotNull();
        assertThat(method.getReturnType()).isEqualTo(void.class);
    }
}
