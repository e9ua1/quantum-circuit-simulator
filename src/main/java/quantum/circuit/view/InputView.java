package quantum.circuit.view;

import camp.nextstep.edu.missionutils.Console;

public class InputView {

    private static final String ERROR_INVALID_NUMBER = "숫자를 입력해주세요.";
    private static final String ERROR_EMPTY_INPUT = "입력값이 비어있습니다.";
    private static final String PROMPT_QUBIT_COUNT = "큐비트 개수를 입력하세요:";
    private static final String PROMPT_GATE_TYPE = "게이트 종류를 입력하세요 (X, H, Z, CNOT):";
    private static final String PROMPT_TARGET_QUBIT = "타겟 큐비트 인덱스를 입력하세요 (0부터 시작):";
    private static final String PROMPT_CONTROL_QUBIT = "제어 큐비트 인덱스를 입력하세요 (0부터 시작):";

    public static int readQubitCount() {
        System.out.println(PROMPT_QUBIT_COUNT);
        return parseInteger(Console.readLine());
    }

    public static String readGateType() {
        System.out.println(PROMPT_GATE_TYPE);
        String input = Console.readLine();
        validateNotEmpty(input);
        return input.trim().toUpperCase();
    }

    public static int readTargetQubit() {
        System.out.println(PROMPT_TARGET_QUBIT);
        return parseInteger(Console.readLine());
    }

    public static int readControlQubit() {
        System.out.println(PROMPT_CONTROL_QUBIT);
        return parseInteger(Console.readLine());
    }

    private static int parseInteger(String input) {
        validateNotEmpty(input);
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ERROR_INVALID_NUMBER);
        }
    }

    private static void validateNotEmpty(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException(ERROR_EMPTY_INPUT);
        }
    }
}
