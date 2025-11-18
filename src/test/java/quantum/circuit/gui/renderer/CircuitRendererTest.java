package quantum.circuit.gui.renderer;

import org.junit.jupiter.api.Test;
import quantum.circuit.domain.circuit.QuantumCircuit;

import static org.assertj.core.api.Assertions.assertThat;

class CircuitRendererTest {

    @Test
    void CircuitRenderer_인터페이스가_존재한다() {
        // given & when
        Class<?> clazz = CircuitRenderer.class;

        // then
        assertThat(clazz).isNotNull();
        assertThat(clazz.isInterface()).isTrue();
    }

    @Test
    void render_메서드가_QuantumCircuit을_파라미터로_받는다() throws Exception {
        // given & when
        var method = CircuitRenderer.class.getDeclaredMethod("render", QuantumCircuit.class);

        // then
        assertThat(method).isNotNull();
        assertThat(method.getReturnType().getSimpleName()).isEqualTo("Pane");
    }
}
