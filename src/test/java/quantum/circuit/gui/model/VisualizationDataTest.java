package quantum.circuit.gui.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class VisualizationDataTest {

    @Test
    void VisualizationData_클래스가_존재한다() {
        // given & when
        Class<?> clazz = VisualizationData.class;

        // then
        assertThat(clazz).isNotNull();
    }

    @Test
    void getQubitCount_메서드가_존재한다() throws Exception {
        // given & when
        var method = VisualizationData.class.getDeclaredMethod("getQubitCount");

        // then
        assertThat(method).isNotNull();
        assertThat(method.getReturnType()).isEqualTo(int.class);
    }

    @Test
    void getStepCount_메서드가_존재한다() throws Exception {
        // given & when
        var method = VisualizationData.class.getDeclaredMethod("getStepCount");

        // then
        assertThat(method).isNotNull();
        assertThat(method.getReturnType()).isEqualTo(int.class);
    }

    @Test
    void getGateAt_메서드가_존재한다() throws Exception {
        // given & when
        var method = VisualizationData.class.getDeclaredMethod("getGateAt", int.class, int.class);

        // then
        assertThat(method).isNotNull();
    }
}
