package quantum.circuit.gui.renderer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CircuitCanvas 테스트")
class CircuitCanvasTest {

    @Test
    @DisplayName("CircuitCanvas 클래스가 존재한다")
    void CircuitCanvas_클래스가_존재한다() {
        // given & when & then
        assertThat(CircuitCanvas.class).isNotNull();
    }

    @Test
    @DisplayName("render 메서드가 QuantumCircuit과 currentStep을 받는다")
    void render_메서드가_QuantumCircuit과_currentStep을_받는다() throws Exception {
        // given
        Class<?> clazz = CircuitCanvas.class;

        // when
        Method method = clazz.getDeclaredMethod(
                "render",
                quantum.circuit.domain.circuit.QuantumCircuit.class,
                int.class
        );

        // then
        assertThat(method).isNotNull();
        assertThat(method.getReturnType().getSimpleName()).isEqualTo("Pane");
    }

    @Test
    @DisplayName("기존 render 메서드도 여전히 존재한다")
    void 기존_render_메서드도_여전히_존재한다() throws Exception {
        // given
        Class<?> clazz = CircuitCanvas.class;

        // when
        Method method = clazz.getDeclaredMethod(
                "render",
                quantum.circuit.domain.circuit.QuantumCircuit.class
        );

        // then
        assertThat(method).isNotNull();
        assertThat(method.getReturnType().getSimpleName()).isEqualTo("Pane");
    }
}
