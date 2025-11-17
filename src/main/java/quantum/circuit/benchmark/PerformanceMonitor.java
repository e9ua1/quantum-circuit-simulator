package quantum.circuit.benchmark;

public interface PerformanceMonitor {

    void onBenchmarkStart(String circuitName);

    void onBenchmarkComplete(String circuitName, PerformanceMetrics metrics);

    String getMonitorName();
}
