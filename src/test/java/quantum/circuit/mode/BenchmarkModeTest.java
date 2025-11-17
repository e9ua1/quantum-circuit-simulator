package quantum.circuit.mode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BenchmarkModeTest {

    @Test
    @DisplayName("벤치마크 모드를 생성한다")
    void createBenchmarkMode() {
        BenchmarkMode mode = new BenchmarkMode();

        assertThat(mode).isNotNull();
    }

    @Test
    @DisplayName("모드 이름을 반환한다")
    void getModeName() {
        BenchmarkMode mode = new BenchmarkMode();

        String name = mode.getModeName();

        assertThat(name).isEqualTo("Benchmark Mode");
    }

    @Test
    @DisplayName("모드 설명을 반환한다")
    void getModeDescription() {
        BenchmarkMode mode = new BenchmarkMode();

        String description = mode.getModeDescription();

        assertThat(description).contains("벤치마크");
    }
}
