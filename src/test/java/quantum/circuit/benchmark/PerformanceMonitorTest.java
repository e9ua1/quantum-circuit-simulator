package quantum.circuit.benchmark;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PerformanceMonitorTest {

    @Test
    @DisplayName("벤치마크 시작 이벤트를 처리한다")
    void handleBenchmarkStart() {
        TestMonitor monitor = new TestMonitor();

        monitor.onBenchmarkStart("Test Circuit");

        assertThat(monitor.isStartCalled()).isTrue();
    }

    @Test
    @DisplayName("벤치마크 완료 이벤트를 처리한다")
    void handleBenchmarkComplete() {
        TestMonitor monitor = new TestMonitor();
        PerformanceMetrics metrics = new PerformanceMetrics(5, 3, 100);

        monitor.onBenchmarkComplete("Test Circuit", metrics);

        assertThat(monitor.isCompleteCalled()).isTrue();
    }

    @Test
    @DisplayName("모니터 이름을 반환한다")
    void returnMonitorName() {
        TestMonitor monitor = new TestMonitor();

        String name = monitor.getMonitorName();

        assertThat(name).isEqualTo("Test Monitor");
    }

    private static class TestMonitor implements PerformanceMonitor {
        private boolean startCalled = false;
        private boolean completeCalled = false;

        @Override
        public void onBenchmarkStart(String circuitName) {
            this.startCalled = true;
        }

        @Override
        public void onBenchmarkComplete(String circuitName, PerformanceMetrics metrics) {
            this.completeCalled = true;
        }

        @Override
        public String getMonitorName() {
            return "Test Monitor";
        }

        public boolean isStartCalled() {
            return startCalled;
        }

        public boolean isCompleteCalled() {
            return completeCalled;
        }
    }
}
