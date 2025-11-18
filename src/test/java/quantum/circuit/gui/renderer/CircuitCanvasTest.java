package quantum.circuit.gui.renderer;

import org.junit.jupiter.api.Test;
import quantum.circuit.domain.circuit.QuantumCircuit;

import static org.assertj.core.api.Assertions.assertThat;

class CircuitCanvasTest {

    @Test
    void CircuitCanvas_클래스가_존재한다() {
        // given & when
        Class<?> clazz = CircuitCanvas.class;

        // then
        assertThat(clazz).isNotNull();
    }

    @Test
    void CircuitCanvas는_CircuitRenderer를_구현한다() {
        // given & when
        Class<?> clazz = CircuitCanvas.class;

        // then
        assertThat(CircuitRenderer.class.isAssignableFrom(clazz)).isTrue();
    }

    @Test
    void render_메서드가_존재한다() throws Exception {
        // given & when
        var method = CircuitCanvas.class.getDeclaredMethod("render", QuantumCircuit.class);

        // then
        assertThat(method).isNotNull();
        assertThat(method.getReturnType().getSimpleName()).isEqualTo("Pane");
    }

    @Test
    void public_기본_생성자를_가진다() throws Exception {
        // given & when
        var constructor = CircuitCanvas.class.getDeclaredConstructor();

        // then
        assertThat(constructor).isNotNull();
        assertThat(java.lang.reflect.Modifier.isPublic(constructor.getModifiers())).isTrue();
    }
}
