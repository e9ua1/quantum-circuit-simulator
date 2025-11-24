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
        return readIntegerWithPrompt(PROMPT_QUBIT_COUNT);
    }

    public static int readInt() {
        return parseInteger(Console.readLine());
    }

    public static String readAlgorithmName() {
        String input = Console.readLine();
        validateNotEmpty(input);
        return input.trim();
    }

    public static String readGateType() {
        System.out.println(PROMPT_GATE_TYPE);
        String input = Console.readLine();
        validateNotEmpty(input);
        return input.trim().toUpperCase();
    }

    public static int readTargetQubit() {
        return readIntegerWithPrompt(PROMPT_TARGET_QUBIT);
    }

    public static int readControlQubit() {
        return readIntegerWithPrompt(PROMPT_CONTROL_QUBIT);
    }

    private static int readIntegerWithPrompt(String prompt) {
        System.out.println(prompt);
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
