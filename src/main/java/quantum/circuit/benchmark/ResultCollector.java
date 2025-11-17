package quantum.circuit.benchmark;

import java.util.HashMap;
import java.util.Map;

public class ResultCollector implements PerformanceMonitor {

    private static final String MONITOR_NAME = "Result Collector";

    private final Map<String, PerformanceMetrics> results;

    public ResultCollector() {
        this.results = new HashMap<>();
    }

    @Override
    public void onBenchmarkStart(String circuitName) {
        // 시작 이벤트는 처리하지 않음
    }

    @Override
    public void onBenchmarkComplete(String circuitName, PerformanceMetrics metrics) {
        results.put(circuitName, metrics);
    }

    @Override
    public String getMonitorName() {
        return MONITOR_NAME;
    }

    public Map<String, PerformanceMetrics> getResults() {
        return new HashMap<>(results);
    }
}
