package quantum.circuit.benchmark;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PerformanceMetricsTest {

    @Test
    @DisplayName("성능 지표를 생성한다")
    void createPerformanceMetrics() {
        PerformanceMetrics metrics = new PerformanceMetrics(5, 3, 100);

        assertThat(metrics).isNotNull();
    }

    @Test
    @DisplayName("게이트 개수를 반환한다")
    void getGateCount() {
        PerformanceMetrics metrics = new PerformanceMetrics(5, 3, 100);

        assertThat(metrics.getGateCount()).isEqualTo(5);
    }

    @Test
    @DisplayName("회로 깊이를 반환한다")
    void getDepth() {
        PerformanceMetrics metrics = new PerformanceMetrics(5, 3, 100);

        assertThat(metrics.getDepth()).isEqualTo(3);
    }

    @Test
    @DisplayName("실행 시간을 반환한다")
    void getExecutionTime() {
        PerformanceMetrics metrics = new PerformanceMetrics(5, 3, 100);

        assertThat(metrics.getExecutionTime()).isEqualTo(100);
    }

    @Test
    @DisplayName("0 값으로 생성할 수 있다")
    void createWithZeroValues() {
        PerformanceMetrics metrics = new PerformanceMetrics(0, 0, 0);

        assertThat(metrics.getGateCount()).isEqualTo(0);
        assertThat(metrics.getDepth()).isEqualTo(0);
        assertThat(metrics.getExecutionTime()).isEqualTo(0);
    }
}
