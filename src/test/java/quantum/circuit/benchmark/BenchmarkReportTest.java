package quantum.circuit.benchmark;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BenchmarkReportTest {

    @Test
    @DisplayName("벤치마크 리포트를 생성한다")
    void createBenchmarkReport() {
        Map<String, PerformanceMetrics> results = createSampleResults();

        BenchmarkReport report = new BenchmarkReport(results);

        assertThat(report).isNotNull();
    }

    @Test
    @DisplayName("모든 결과를 반환한다")
    void getAllResults() {
        Map<String, PerformanceMetrics> results = createSampleResults();
        BenchmarkReport report = new BenchmarkReport(results);

        Map<String, PerformanceMetrics> allResults = report.getAllResults();

        assertThat(allResults).hasSize(2);
        assertThat(allResults).containsKeys("Circuit A", "Circuit B");
    }

    @Test
    @DisplayName("특정 회로의 결과를 반환한다")
    void getResultByName() {
        Map<String, PerformanceMetrics> results = createSampleResults();
        BenchmarkReport report = new BenchmarkReport(results);

        PerformanceMetrics metrics = report.getResult("Circuit A");

        assertThat(metrics).isNotNull();
        assertThat(metrics.gateCount()).isEqualTo(5);
    }

    @Test
    @DisplayName("존재하지 않는 회로는 null을 반환한다")
    void getNonExistentResult() {
        Map<String, PerformanceMetrics> results = createSampleResults();
        BenchmarkReport report = new BenchmarkReport(results);

        PerformanceMetrics metrics = report.getResult("NonExistent");

        assertThat(metrics).isNull();
    }

    @Test
    @DisplayName("회로 개수를 반환한다")
    void getCircuitCount() {
        Map<String, PerformanceMetrics> results = createSampleResults();
        BenchmarkReport report = new BenchmarkReport(results);

        int count = report.getCircuitCount();

        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("빈 리포트를 생성한다")
    void createEmptyReport() {
        BenchmarkReport report = new BenchmarkReport(Map.of());

        assertThat(report.getCircuitCount()).isEqualTo(0);
        assertThat(report.getAllResults()).isEmpty();
    }

    @Test
    @DisplayName("리포트 문자열을 생성한다")
    void generateReportString() {
        Map<String, PerformanceMetrics> results = createSampleResults();
        BenchmarkReport report = new BenchmarkReport(results);

        String reportString = report.toString();

        assertThat(reportString).contains("Circuit A");
        assertThat(reportString).contains("Circuit B");
        assertThat(reportString).contains("게이트");
        assertThat(reportString).contains("깊이");
    }

    @Test
    @DisplayName("가장 빠른 회로를 찾는다")
    void findFastestCircuit() {
        Map<String, PerformanceMetrics> results = Map.of(
                "Circuit A", new PerformanceMetrics(5, 3, 100),
                "Circuit B", new PerformanceMetrics(8, 4, 50)
        );
        BenchmarkReport report = new BenchmarkReport(results);

        String fastest = report.getFastestCircuit();

        assertThat(fastest).isEqualTo("Circuit B");
    }

    @Test
    @DisplayName("가장 효율적인 회로를 찾는다")
    void findMostEfficientCircuit() {
        Map<String, PerformanceMetrics> results = Map.of(
                "Circuit A", new PerformanceMetrics(5, 3, 100),
                "Circuit B", new PerformanceMetrics(3, 2, 150)
        );
        BenchmarkReport report = new BenchmarkReport(results);

        String efficient = report.getMostEfficientCircuit();

        assertThat(efficient).isEqualTo("Circuit B");
    }

    private Map<String, PerformanceMetrics> createSampleResults() {
        return Map.of(
                "Circuit A", new PerformanceMetrics(5, 3, 100),
                "Circuit B", new PerformanceMetrics(8, 4, 150)
        );
    }
}
