package quantum.circuit.gui;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GuiApplicationTest {

    @Test
    void GuiApplication_클래스가_존재한다() {
        // given & when
        Class<?> clazz = GuiApplication.class;

        // then
        assertThat(clazz).isNotNull();
    }

    @Test
    void GuiApplication은_JavaFX_Application을_상속한다() {
        // given & when
        Class<?> clazz = GuiApplication.class;

        // then
        assertThat(javafx.application.Application.class)
                .isAssignableFrom(clazz);
    }

    @Test
    void GuiApplication은_start_메서드를_가진다() throws Exception {
        // given & when
        var method = GuiApplication.class.getDeclaredMethod("start", javafx.stage.Stage.class);

        // then
        assertThat(method).isNotNull();
    }

    @Test
    void GuiApplication은_main_메서드를_가진다() throws Exception {
        // given & when
        var method = GuiApplication.class.getDeclaredMethod("main", String[].class);

        // then
        assertThat(method).isNotNull();
        assertThat(java.lang.reflect.Modifier.isStatic(method.getModifiers())).isTrue();
        assertThat(java.lang.reflect.Modifier.isPublic(method.getModifiers())).isTrue();
    }
}
