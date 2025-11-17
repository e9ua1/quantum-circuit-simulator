package quantum.circuit.benchmark;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ResultCollectorTest {

    @Test
    @DisplayName("결과 수집기를 생성한다")
    void createResultCollector() {
        ResultCollector collector = new ResultCollector();

        assertThat(collector).isNotNull();
    }

    @Test
    @DisplayName("벤치마크 시작 이벤트를 처리한다")
    void handleBenchmarkStart() {
        ResultCollector collector = new ResultCollector();

        collector.onBenchmarkStart("Circuit A");

        assertThat(collector.getResults()).isEmpty();
    }

    @Test
    @DisplayName("벤치마크 완료 시 결과를 수집한다")
    void collectResultOnComplete() {
        ResultCollector collector = new ResultCollector();
        PerformanceMetrics metrics = new PerformanceMetrics(5, 3, 100);

        collector.onBenchmarkComplete("Circuit A", metrics);

        Map<String, PerformanceMetrics> results = collector.getResults();
        assertThat(results).hasSize(1);
        assertThat(results).containsKey("Circuit A");
    }

    @Test
    @DisplayName("여러 회로의 결과를 수집한다")
    void collectMultipleResults() {
        ResultCollector collector = new ResultCollector();
        PerformanceMetrics metrics1 = new PerformanceMetrics(5, 3, 100);
        PerformanceMetrics metrics2 = new PerformanceMetrics(8, 4, 150);

        collector.onBenchmarkComplete("Circuit A", metrics1);
        collector.onBenchmarkComplete("Circuit B", metrics2);

        Map<String, PerformanceMetrics> results = collector.getResults();
        assertThat(results).hasSize(2);
        assertThat(results).containsKeys("Circuit A", "Circuit B");
    }

    @Test
    @DisplayName("수집된 결과를 반환한다")
    void returnCollectedResults() {
        ResultCollector collector = new ResultCollector();
        PerformanceMetrics metrics = new PerformanceMetrics(5, 3, 100);
        collector.onBenchmarkComplete("Circuit A", metrics);

        Map<String, PerformanceMetrics> results = collector.getResults();

        assertThat(results.get("Circuit A")).isEqualTo(metrics);
    }

    @Test
    @DisplayName("모니터 이름을 반환한다")
    void returnMonitorName() {
        ResultCollector collector = new ResultCollector();

        String name = collector.getMonitorName();

        assertThat(name).isEqualTo("Result Collector");
    }

    @Test
    @DisplayName("초기 상태에서는 빈 결과를 반환한다")
    void returnEmptyResultsInitially() {
        ResultCollector collector = new ResultCollector();

        Map<String, PerformanceMetrics> results = collector.getResults();

        assertThat(results).isEmpty();
    }

    @Test
    @DisplayName("같은 이름의 회로는 덮어쓴다")
    void overwriteSameCircuitName() {
        ResultCollector collector = new ResultCollector();
        PerformanceMetrics metrics1 = new PerformanceMetrics(5, 3, 100);
        PerformanceMetrics metrics2 = new PerformanceMetrics(8, 4, 150);

        collector.onBenchmarkComplete("Circuit A", metrics1);
        collector.onBenchmarkComplete("Circuit A", metrics2);

        Map<String, PerformanceMetrics> results = collector.getResults();
        assertThat(results).hasSize(1);
        assertThat(results.get("Circuit A")).isEqualTo(metrics2);
    }
}
