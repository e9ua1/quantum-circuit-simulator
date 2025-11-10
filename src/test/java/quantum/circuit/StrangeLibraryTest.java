package quantum.circuit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redfx.strange.Program;
import org.redfx.strange.Step;
import org.redfx.strange.gate.Hadamard;
import org.redfx.strange.gate.X;
import org.redfx.strange.local.SimpleQuantumExecutionEnvironment;
import org.redfx.strange.Qubit;
import org.redfx.strange.Result;

import static org.assertj.core.api.Assertions.assertThat;

class StrangeLibraryTest {

    @Test
    @DisplayName("Strange 라이브러리가 정상적으로 동작한다")
    void strangeLibraryWorks() {
        // given
        Program program = new Program(1);
        Step step = new Step();
        step.addGate(new X(0));
        program.addStep(step);

        // when
        SimpleQuantumExecutionEnvironment env = new SimpleQuantumExecutionEnvironment();
        Result result = env.runProgram(program);
        Qubit[] qubits = result.getQubits();

        // then
        assertThat(qubits[0].measure()).isEqualTo(1);
    }

    @Test
    @DisplayName("Hadamard 게이트는 중첩 상태를 만든다")
    void hadamardGateCreatesSuperposition() {
        // given
        Program program = new Program(1);
        Step step = new Step();
        step.addGate(new Hadamard(0));
        program.addStep(step);

        // when
        SimpleQuantumExecutionEnvironment env = new SimpleQuantumExecutionEnvironment();
        Result result = env.runProgram(program);
        Qubit qubit = result.getQubits()[0];

        // then - 확률이 약 50%
        double probability = qubit.getProbability();
        assertThat(probability).isBetween(0.45, 0.55);
    }
}
