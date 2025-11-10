package quantum.circuit.benchmark;

import java.util.Map;

public class BenchmarkReport {

    private static final String REPORT_HEADER = "=== 벤치마크 결과 ===";
    private static final String REPORT_FORMAT = "%-20s | 게이트: %3d | 깊이: %3d | 시간: %10d ns";

    private final Map<String, PerformanceMetrics> results;

    public BenchmarkReport(Map<String, PerformanceMetrics> results) {
        this.results = Map.copyOf(results);
    }

    public Map<String, PerformanceMetrics> getAllResults() {
        return Map.copyOf(results);
    }

    public PerformanceMetrics getResult(String circuitName) {
        return results.get(circuitName);
    }

    public int getCircuitCount() {
        return results.size();
    }

    public String getFastestCircuit() {
        return results.entrySet().stream()
                .min((e1, e2) -> Long.compare(
                        e1.getValue().getExecutionTime(),
                        e2.getValue().getExecutionTime()
                ))
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    public String getMostEfficientCircuit() {
        return results.entrySet().stream()
                .min((e1, e2) -> Integer.compare(
                        e1.getValue().getGateCount(),
                        e2.getValue().getGateCount()
                ))
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    @Override
    public String toString() {
        if (results.isEmpty()) {
            return REPORT_HEADER + "\n(결과 없음)";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(REPORT_HEADER).append("\n");

        for (Map.Entry<String, PerformanceMetrics> entry : results.entrySet()) {
            String name = entry.getKey();
            PerformanceMetrics metrics = entry.getValue();
            sb.append(String.format(REPORT_FORMAT,
                    name,
                    metrics.getGateCount(),
                    metrics.getDepth(),
                    metrics.getExecutionTime()
            )).append("\n");
        }

        return sb.toString();
    }
}
