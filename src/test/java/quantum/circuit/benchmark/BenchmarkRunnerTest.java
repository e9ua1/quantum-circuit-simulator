package quantum.circuit.benchmark;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import quantum.circuit.domain.circuit.CircuitStep;
import quantum.circuit.domain.circuit.QubitIndex;
import quantum.circuit.domain.circuit.QuantumCircuit;
import quantum.circuit.domain.circuit.QuantumCircuitBuilder;
import quantum.circuit.domain.gate.HadamardGate;
import quantum.circuit.domain.gate.PauliXGate;

class BenchmarkRunnerTest {

    @Test
    @DisplayName("벤치마크 러너를 생성한다")
    void createBenchmarkRunner() {
        BenchmarkRunner runner = new BenchmarkRunner(List.of());

        assertThat(runner).isNotNull();
    }

    @Test
    @DisplayName("단일 회로를 벤치마크한다")
    void benchmarkSingleCircuit() {
        ResultCollector collector = new ResultCollector();
        BenchmarkRunner runner = new BenchmarkRunner(List.of(collector));
        Map<String, QuantumCircuit> circuits = Map.of(
                "Circuit A", createSimpleCircuit()
        );

        BenchmarkReport report = runner.runBenchmark(circuits);

        assertThat(report).isNotNull();
    }

    @Test
    @DisplayName("여러 회로를 벤치마크한다")
    void benchmarkMultipleCircuits() {
        ResultCollector collector = new ResultCollector();
        BenchmarkRunner runner = new BenchmarkRunner(List.of(collector));
        Map<String, QuantumCircuit> circuits = Map.of(
                "Circuit A", createSimpleCircuit(),
                "Circuit B", createComplexCircuit()
        );

        BenchmarkReport report = runner.runBenchmark(circuits);

        assertThat(report).isNotNull();
        assertThat(collector.getResults()).hasSize(2);
    }

    @Test
    @DisplayName("모니터에게 시작 이벤트를 통지한다")
    void notifyMonitorsOnStart() {
        TestMonitor monitor = new TestMonitor();
        BenchmarkRunner runner = new BenchmarkRunner(List.of(monitor));
        Map<String, QuantumCircuit> circuits = Map.of(
                "Circuit A", createSimpleCircuit()
        );

        runner.runBenchmark(circuits);

        assertThat(monitor.getStartCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("모니터에게 완료 이벤트를 통지한다")
    void notifyMonitorsOnComplete() {
        TestMonitor monitor = new TestMonitor();
        BenchmarkRunner runner = new BenchmarkRunner(List.of(monitor));
        Map<String, QuantumCircuit> circuits = Map.of(
                "Circuit A", createSimpleCircuit()
        );

        runner.runBenchmark(circuits);

        assertThat(monitor.getCompleteCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("여러 모니터에게 이벤트를 통지한다")
    void notifyMultipleMonitors() {
        TestMonitor monitor1 = new TestMonitor();
        TestMonitor monitor2 = new TestMonitor();
        BenchmarkRunner runner = new BenchmarkRunner(List.of(monitor1, monitor2));
        Map<String, QuantumCircuit> circuits = Map.of(
                "Circuit A", createSimpleCircuit()
        );

        runner.runBenchmark(circuits);

        assertThat(monitor1.getCompleteCount()).isEqualTo(1);
        assertThat(monitor2.getCompleteCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("빈 회로 맵을 처리한다")
    void handleEmptyCircuits() {
        ResultCollector collector = new ResultCollector();
        BenchmarkRunner runner = new BenchmarkRunner(List.of(collector));

        BenchmarkReport report = runner.runBenchmark(Map.of());

        assertThat(report).isNotNull();
        assertThat(collector.getResults()).isEmpty();
    }

    @Test
    @DisplayName("모니터 없이 벤치마크를 실행한다")
    void runWithoutMonitors() {
        BenchmarkRunner runner = new BenchmarkRunner(List.of());
        Map<String, QuantumCircuit> circuits = Map.of(
                "Circuit A", createSimpleCircuit()
        );

        BenchmarkReport report = runner.runBenchmark(circuits);

        assertThat(report).isNotNull();
    }

    private QuantumCircuit createSimpleCircuit() {
        return new QuantumCircuitBuilder()
                .withQubits(1)
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .build();
    }

    private QuantumCircuit createComplexCircuit() {
        return new QuantumCircuitBuilder()
                .withQubits(2)
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(1)))))
                .build();
    }

    private static class TestMonitor implements PerformanceMonitor {
        private int startCount = 0;
        private int completeCount = 0;

        @Override
        public void onBenchmarkStart(String circuitName) {
            startCount++;
        }

        @Override
        public void onBenchmarkComplete(String circuitName, PerformanceMetrics metrics) {
            completeCount++;
        }

        @Override
        public String getMonitorName() {
            return "Test Monitor";
        }

        public int getStartCount() {
            return startCount;
        }

        public int getCompleteCount() {
            return completeCount;
        }
    }
}
