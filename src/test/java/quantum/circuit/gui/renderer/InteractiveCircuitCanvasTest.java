package quantum.circuit.gui.renderer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("InteractiveCircuitCanvas 테스트")
class InteractiveCircuitCanvasTest {

    @Test
    @DisplayName("InteractiveCircuitCanvas 클래스가 존재한다")
    void InteractiveCircuitCanvas_클래스가_존재한다() {
        // given & when & then
        assertThat(InteractiveCircuitCanvas.class).isNotNull();
    }

    @Test
    @DisplayName("setOnGateClicked 메서드가 존재한다")
    void setOnGateClicked_메서드가_존재한다() throws Exception {
        // given
        Class<?> clazz = InteractiveCircuitCanvas.class;

        // when
        Method method = clazz.getDeclaredMethod("setOnGateClicked",
                java.util.function.BiConsumer.class);

        // then
        assertThat(method).isNotNull();
        assertThat(method.getReturnType()).isEqualTo(void.class);
    }

    @Test
    @DisplayName("render 메서드가 존재한다")
    void render_메서드가_존재한다() throws Exception {
        // given
        Class<?> clazz = InteractiveCircuitCanvas.class;

        // when
        Method method = clazz.getDeclaredMethod("render",
                quantum.circuit.domain.circuit.QuantumCircuit.class,
                int.class);

        // then
        assertThat(method).isNotNull();
        assertThat(method.getReturnType().getSimpleName()).isEqualTo("Pane");
    }
}
